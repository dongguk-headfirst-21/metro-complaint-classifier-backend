from sqlalchemy import BigInteger, String, Integer, ForeignKey, DateTime, Text, Boolean
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column
from pgvector.sqlalchemy import Vector
from datetime import datetime
from typing import Optional

class Base(DeclarativeBase):
    pass

class File(Base):
    __tablename__ = "file"

    id: Mapped[int] = mapped_column(BigInteger, primary_key=True)
    file_name: Mapped[str] = mapped_column(String(100), nullable=False)
    row_count: Mapped[Optional[int]] = mapped_column(Integer)
    capacity: Mapped[Optional[int]] = mapped_column(BigInteger)
    uploaded_at: Mapped[datetime] = mapped_column(DateTime, nullable=False)
    status: Mapped[str] = mapped_column(String(10), nullable=False)
    error_message: Mapped[Optional[str]] = mapped_column(Text)

class Complaint(Base):
    __tablename__ = "complaint"

    id: Mapped[int] = mapped_column(BigInteger, primary_key=True)
    title: Mapped[str] = mapped_column(String(60), nullable=False)
    content: Mapped[str] = mapped_column(String(2000), nullable=False)
    code: Mapped[Optional[int]] = mapped_column(Integer)
    is_checked: Mapped[bool] = mapped_column(Boolean, nullable=False, default=False)
    status: Mapped[str] = mapped_column(String(20), nullable=False, default="PENDING")
    failure_reason: Mapped[Optional[str]] = mapped_column(String(100))
    file_id: Mapped[int] = mapped_column(BigInteger, ForeignKey("file.id"), nullable=False)
    depart_id: Mapped[Optional[int]] = mapped_column(BigInteger)

class ComplaintEmbedding(Base):
    __tablename__ = "complaint_embedding"

    id: Mapped[int] = mapped_column(BigInteger, primary_key=True)
    complaint_id: Mapped[int] = mapped_column(BigInteger, ForeignKey("complaint.id"), nullable=False, unique=True)
    embedding: Mapped[list] = mapped_column(Vector(1024))
    
class Feature(Base):
    __tablename__ = "feature"
    id: Mapped[int] = mapped_column(BigInteger, primary_key=True)
    content: Mapped[str] = mapped_column(String(255), nullable=False)

class ProcessCodeType(Base):
    __tablename__ = "process_code_type"

    code: Mapped[int] = mapped_column(Integer, primary_key=True)
    text: Mapped[str] = mapped_column(String(100), nullable=False)


class FeatureEmbedding(Base):
    __tablename__ = "feature_embedding"

    id: Mapped[int] = mapped_column(BigInteger, primary_key=True)
    feature_id: Mapped[int] = mapped_column(BigInteger, ForeignKey("feature.id"), nullable=False, unique=True)
    embedding: Mapped[list] = mapped_column(Vector(1024))


class ProcessCodeTypeEmbedding(Base):
    __tablename__ = "process_code_type_embedding"

    id: Mapped[int] = mapped_column(BigInteger, primary_key=True)
    process_code_type_id: Mapped[int] = mapped_column(BigInteger, ForeignKey("process_code_type.code"), nullable=False, unique=True)
    embedding: Mapped[list] = mapped_column(Vector(1024))


class FeatureDepart(Base):
    __tablename__ = "feature_depart"

    id: Mapped[int] = mapped_column(BigInteger, primary_key=True)
    feature_id: Mapped[int] = mapped_column(BigInteger, ForeignKey("feature.id"), nullable=False)
    depart_id: Mapped[int] = mapped_column(BigInteger, ForeignKey("depart.id"), nullable=False)
