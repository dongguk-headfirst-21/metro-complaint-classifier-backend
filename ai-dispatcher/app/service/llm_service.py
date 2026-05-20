import re
import logging
from openai import AsyncOpenAI
from app.core.config import settings
from app.model.models import Feature

logger = logging.getLogger(__name__)

_SYSTEM_PROMPT = (
    "당신은 지하철 민원을 담당 부서 업무에 매핑하는 분류기입니다. "
    "제공된 업무 목록 중 민원과 가장 관련 있는 업무 하나의 ID를 숫자만 출력하세요. "
    "다른 텍스트는 절대 출력하지 마세요."
)


def _build_user_message(title: str, content: str, features: list[Feature]) -> str:
    feature_list = "\n".join(f"[{f.id}] {f.content}" for f in features)
    return f"민원 제목: {title}\n민원 내용: {content}\n\n업무 목록:\n{feature_list}"


async def _call_llm(user_message: str) -> str:
    client = AsyncOpenAI(base_url=settings.llm_base_url, api_key=settings.llm_api_key)
    response = await client.chat.completions.create(
        model=settings.llm_model,
        messages=[
            {"role": "system", "content": _SYSTEM_PROMPT},
            {"role": "user", "content": user_message},
        ],
        max_tokens=16,
    )
    return response.choices[0].message.content


async def find_best_feature_id(title: str, content: str, features: list[Feature]) -> int | None:
    raw = await _call_llm(_build_user_message(title, content, features))
    valid_ids = {f.id for f in features}
    match = re.search(r"\d+", raw.strip())
    if match and (feature_id := int(match.group())) in valid_ids:
        return feature_id
    logger.warning("LLM 응답에서 유효한 feature_id 파싱 실패: %s", raw)
    return None
