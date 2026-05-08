from sqlalchemy.orm import Session
from sqlalchemy import select
from app.model.models import Feature, ProcessCodeType, FeatureEmbedding, ProcessCodeTypeEmbedding

class EmbeddingRepository:

    def __init__(self, session: Session):
        self.session = session

    def get_all_features(self) -> list[Feature]:
        result = self.session.execute(select(Feature))
        return result.scalars().all()

    def get_all_process_code_types(self) -> list[ProcessCodeType]:
        result = self.session.execute(select(ProcessCodeType))
        return result.scalars().all()

    def insert_feature_embedding(self, feature_id: int, embedding: list) -> None:
        self.session.add(FeatureEmbedding(feature_id=feature_id, embedding=embedding))

    def insert_process_code_type_embedding(self, process_code_type_id: int, embedding: list) -> None:
        self.session.add(ProcessCodeTypeEmbedding(process_code_type_id=process_code_type_id, embedding=embedding))