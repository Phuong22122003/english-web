from pydantic import BaseModel
class PronunciationResponse(BaseModel):
    message: str
    ipa: str
    score: float
    detail_scores: list