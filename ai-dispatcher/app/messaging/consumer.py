import logging
import json
import asyncio
from datetime import date
from aiokafka import AIOKafkaConsumer
from app.db.session import get_db
from app.db.repository.embedding_repository import EmbeddingRepository
from app.service.embedding_service import extract_embedding

from app.core.config import settings

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