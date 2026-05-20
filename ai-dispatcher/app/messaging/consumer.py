import logging
import json
import asyncio
import numpy as np
from datetime import date
from aiokafka import AIOKafkaConsumer
from app.db.session import get_db
from app.db.repository.embedding_repository import EmbeddingRepository
from app.service.embedding_service import extract_embedding
from app.core.config import settings
from app.messaging.producer import publish_classification_response

logger = logging.getLogger(__name__)

_consumer: AIOKafkaConsumer | None = None

async def start_consumer() -> None:
    """
    Kafka 컨슈머를 시작하고 메시지 수신 루프를 실행
    FastAPI lifespan startup 시 호출됨
    """
    
    global _consumer
    
    _consumer = AIOKafkaConsumer(
        settings.kafka_topic_embedding_trigger,
        settings.kafka_topic_classification_request,
        bootstrap_servers=settings.kafka_bootstrap_servers,
        group_id=settings.kafka_consumer_group_id,
        auto_offset_reset="earliest",
        value_deserializer=lambda v: v.decode("utf-8"),
    )
    
    await _consumer.start()
    logger.info(
        f"Kafka 컨슈머 시작: topic={settings.kafka_topic_embedding_trigger} 수신, "
        f"group={settings.kafka_consumer_group_id}"
    )
    
    async for message in _consumer:
        await _handle_message(message)

async def stop_consumer() -> None:
    """
    Kafka 컨슈머를 정상 종료
    FastAPI lifespan shutdown 시 호출됨
    """
    
    global _consumer
    
    if _consumer:
        await _consumer.stop()
        logger.info("Kafka 컨슈머 종료 완료")
        _consumer = None

async def _handle_message(message) -> None:
    if(message.topic == settings.kafka_topic_embedding_trigger):
        logger.info(f"토픽: {message.topic}, 값: {message.value}")
        try:
            with get_db() as db:
                logger.info("DB 연결 성공")
                repo = EmbeddingRepository(db)
                logger.info("features 조회 시작")
                features = repo.get_all_features()
                logger.info(f"features 개수: {len(features)}")
                for feature in features:
                    embedding = extract_embedding(feature.content)
                    repo.insert_feature_embedding(feature.id, embedding.tolist())
                db.commit()
                logger.info("feature 임베딩 완료")

                process_code_types = repo.get_all_process_code_types()
                logger.info("처리코드 개수: " + str(len(process_code_types)))
                for pct in process_code_types:
                    embedding = extract_embedding(pct.text)
                    repo.insert_process_code_type_embedding(pct.code, embedding.tolist())

                db.commit()
                logger.info("process_code_type 임베딩 완료")
        except Exception as e:
                logger.error(f"에러 발생: {e}", exc_info=True)
                
    elif(message.topic == settings.kafka_topic_classification_request):
        def cosine_similarity(a: np.ndarray, b: np.ndarray) -> float:
            return float(np.dot(a, b) / (np.linalg.norm(a) * np.linalg.norm(b)))
        
        file_id = int(message.value)
        
        try:
            with get_db() as db:
                repo = EmbeddingRepository(db)

                complaints = repo.get_all_complaints_in_the_file(file_id)
                process_code_types = repo.get_all_process_code_types()
                
                for complaint in complaints:
                    try:
                        embedding = extract_embedding(complaint.title + " " + complaint.content)
                        repo.insert_complaint_embedding(complaint.id, embedding.tolist())
                    except Exception as e:
                        logger.error(f"민원 {complaint.id} 임베딩 중 에러 발생: {e}", exc_info=True)
                        complaint.status = "FAILED"
                        complaint.failure_reason = str(e)
                db.commit()
                logger.info("complaint 임베딩 완료")

                for complaint in complaints:
                    complaint_embedding = repo.get_complaint_embedding(complaint.id)
                    if complaint_embedding is None:
                        logger.warning(f"민원 {complaint.id}의 임베딩이 없습니다. 건너뜁니다.")
                        complaint.status = "FAILED"
                        complaint.failure_reason = "no_embedding_for_the_complaint"
                        continue

                    best_code = None
                    best_similarity = -1.0

                    for pct in process_code_types:
                        pct_embedding = repo.get_process_code_type_embedding(pct.code)
                        if pct_embedding is None:
                            logger.warning(f"코드 {pct.code}의 임베딩이 없습니다. 건너뜁니다.")
                            continue
                        similarity = cosine_similarity(complaint_embedding, pct_embedding)
                        if similarity > best_similarity:
                            best_similarity = similarity
                            best_code = pct.code
                    
                    if best_code is None:
                        complaint.status = "FAILED"
                        complaint.failure_reason = "no_embedding_for_any_process_code_type"
                        continue

                    complaint.code = best_code
                    complaint.status = "COMPLETED"

                db.commit()
                logger.info(f"file_id={file_id} 분류 완료")
                
                file = repo.get_file(file_id)
                if(file.status != "ERROR"):
                    file.status = "COMPLETED"
                db.commit()
                await publish_classification_response(file_id)

        except Exception as e:
            logger.error(f"분류 중 에러 발생: {e}", exc_info=True)
            try:
                with get_db() as db:
                    repo = EmbeddingRepository(db)
                    file = repo.get_file(file_id)
                    file.status = "ERROR"
                    file.error_message = str(e)
                    db.commit()
                    await publish_classification_response(file_id)
            except Exception as inner_e:
                logger.error(f"파일 실패 상태 저장 실패: {inner_e}")