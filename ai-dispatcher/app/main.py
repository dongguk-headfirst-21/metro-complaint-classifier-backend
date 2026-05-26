import logging.config
from fastapi import FastAPI
from app.core.lifespan import lifespan

LOGGING_CONFIG = {
    "version": 1,
    "disable_existing_loggers": False,
    "formatters": {
        "default": {
            "format": "%(asctime)s | %(levelname)-8s | %(name)s | %(message)s",
            "datefmt": "%Y-%m-%d %H:%M:%S",
        }
    },
    "handlers": {
        "console": {
            "class": "logging.StreamHandler",
            "formatter": "default",
        }
    },
    "root": {
        "level": "INFO",
        "handlers": ["console"],
    }
}

logging.config.dictConfig(LOGGING_CONFIG)

app = FastAPI(
    title="민원 처리 코드 생성 및 분류 서비스",
    description="KURE-v1 + pgvector 기반 유사도 검색으로 민원 처리 코드 생성 및 자체 모델 기반으로 공고를 각 부서에 배부",
    version="1.0.0",
    lifespan=lifespan
)

@app.get("/health", tags=["health"])
def health_check():
    """
    로드밸런서/쿠버네티스 헬스체크용
    """
    return {"status": "ok"}