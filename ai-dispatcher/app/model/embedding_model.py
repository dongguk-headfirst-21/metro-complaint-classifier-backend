import logging
import torch
from transformers import AutoTokenizer
from transformers import AutoModel
from app.core.config import settings

logger = logging.getLogger(__name__)

class EmbeddingModel:
    _tokenizer = None
    _model = None

    @classmethod
    def load(cls):
        if cls._model is not None:
            logger.info("임베딩 모델이 이미 로드되어 있음")
            return

        logger.info("임베딩 모델 로딩 중: %s", settings.embedding_model_name)
        cls._tokenizer = AutoTokenizer.from_pretrained(settings.embedding_model_name)
        cls._model = AutoModel.from_pretrained(settings.embedding_model_name)
        cls._model.eval()
        logger.info("임베딩 모델 로드 완료")

    @classmethod
    def get(cls):
        if cls._model is None:
            cls.load()

        return cls._tokenizer, cls._model