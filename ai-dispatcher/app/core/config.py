from pydantic_settings import BaseSettings

class Settings(BaseSettings):

    db_host: str
    db_port: int
    db_name: str
    db_user: str
    db_password: str
    
    kafka_bootstrap_servers: str
    kafka_topic_embedding_trigger: str
    kafka_topic_classification_request: str
    kafka_topic_classification_response: str
    kafka_consumer_group_id: str
    
    embedding_model_name: str

    llm_model: str
    llm_api_key: str
    llm_base_url: str

    class Config:
        env_file = ".env"

settings = Settings()