from sqlalchemy import BigInteger, String, Integer, ForeignKey
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column
from pgvector.sqlalchemy import Vector

class Base(DeclarativeBase):
    pass

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
    embedding: Mapped[list] = mapped_column(Vector(768))


class ProcessCodeTypeEmbedding(Base):
    __tablename__ = "process_code_type_embedding"

    id: Mapped[int] = mapped_column(BigInteger, primary_key=True)
    process_code_type_id: Mapped[int] = mapped_column(BigInteger, ForeignKey("process_code_type.code"), nullable=False, unique=True)
    embedding: Mapped[list] = mapped_column(Vector(768))
