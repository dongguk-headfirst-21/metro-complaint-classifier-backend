import numpy as np
import torch
from app.model.embedding_model import EmbeddingModel

def extract_embedding(text: str) -> np.ndarray:
    """
    공고 제목 텍스트를 받아서 KURE-v1으로 1024차원 임베딩 벡터를 추출해 반환

    Args:
        text: 공고 제목 (예: "백엔드 개발자 채용")

    Returns:
        np.ndarray: 1024차원 임베딩 벡터
    """

    tokenizer, model = EmbeddingModel.get()

    inputs = tokenizer(
        text,
        return_tensors="pt",
        truncation=True,
        padding=True,
        max_length=512
    )

    with torch.no_grad():
        outputs = model(**inputs)

    # KURE-v1은 mean pooling 사용
    attention_mask = inputs["attention_mask"]
    token_embeddings = outputs.last_hidden_state
    expanded_mask = attention_mask.unsqueeze(-1).expand(token_embeddings.size()).float()
    embedding = (torch.sum(token_embeddings * expanded_mask, dim=1) / expanded_mask.sum(dim=1).clamp(min=1e-9)).squeeze().numpy()

    return embedding