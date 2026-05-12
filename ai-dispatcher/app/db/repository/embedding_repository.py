import numpy as np
from sqlalchemy.orm import Session
from sqlalchemy import select
from app.model.models import File, Complaint, ComplaintEmbedding, ProcessCodeType, Feature, FeatureEmbedding, ProcessCodeTypeEmbedding

class EmbeddingRepository:

    def __init__(self, session: Session):
        self.session = session
        
    def get_file(self, file_id: int):
        stmt = select(File).where(File.id == file_id)
        return self.session.scalars(stmt).first()
        
    def get_all_complaints_in_the_file(self, file_id: int) -> list[Complaint]:
        stmt = select(Complaint).where(Complaint.file_id == file_id)
        return list(self.session.scalars(stmt).all())
    
    def get_complaint_embedding(self, complaint_id) -> np.ndarray:
        stmt = select(ComplaintEmbedding).where(ComplaintEmbedding.complaint_id == complaint_id)
        result = self.session.scalars(stmt).first()
        return np.array(result.embedding) if result else None
    
    def get_all_features(self) -> list[Feature]:
        result = self.session.execute(select(Feature))
        return result.scalars().all()

    def get_all_process_code_types(self) -> list[ProcessCodeType]:
        result = self.session.execute(select(ProcessCodeType))
        return result.scalars().all()
    
    def get_process_code_type_embedding(self, process_code_type_id: int) -> np.ndarray | None:
        stmt = select(ProcessCodeTypeEmbedding).where(
            ProcessCodeTypeEmbedding.process_code_type_id == process_code_type_id
        )
        result = self.session.scalars(stmt).first()
        return np.array(result.embedding) if result else None

    def insert_complaint_embedding(self, complaint_id: int, embedding: list) -> None:
        self.session.add(ComplaintEmbedding(complaint_id=complaint_id, embedding=embedding))
    
    def insert_feature_embedding(self, feature_id: int, embedding: list) -> None:
        self.session.add(FeatureEmbedding(feature_id=feature_id, embedding=embedding))

    def insert_process_code_type_embedding(self, process_code_type_id: int, embedding: list) -> None:
        self.session.add(ProcessCodeTypeEmbedding(process_code_type_id=process_code_type_id, embedding=embedding))