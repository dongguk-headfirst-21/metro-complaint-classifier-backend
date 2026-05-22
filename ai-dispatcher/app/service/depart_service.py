import logging
import numpy as np
from app.model.models import Complaint
from app.db.repository.embedding_repository import EmbeddingRepository
from app.service.llm_service import find_best_feature_id

logger = logging.getLogger(__name__)


def _cosine_similarity(a: np.ndarray, b: np.ndarray) -> float:
    return float(np.dot(a, b) / (np.linalg.norm(a) * np.linalg.norm(b)))


async def classify_depart(
    complaint: Complaint,
    complaint_embedding: np.ndarray,
    repo: EmbeddingRepository,
    top_n: int = 100,
) -> tuple[int | None, str | None]:
    feature_pairs = repo.get_all_feature_embeddings_with_content()
    if not feature_pairs:
        logger.warning("민원 %s: feature 임베딩 없음, 부서 분류 건너뜀", complaint.id)
        return None, "no_feature_embeddings"

    scored = sorted(
        feature_pairs,
        key=lambda pair: _cosine_similarity(complaint_embedding, pair[1]),
        reverse=True,
    )[:top_n]

    top_features = [f for f, _ in scored]

    feature_id = await find_best_feature_id(complaint.title, complaint.content, top_features)
    if feature_id is None:
        logger.warning("민원 %s: LLM이 유효한 feature_id 미반환", complaint.id)
        return None, "llm_no_feature_id"

    depart_id = repo.get_depart_id_by_feature_id(feature_id)
    if depart_id is None:
        logger.warning("feature_id=%s에 연결된 부서 없음", feature_id)
        return None, "no_depart_for_feature_id"
    return depart_id, None
