from pydantic import BaseModel
class TopicSuggestionRequest(BaseModel):
    name: str
    description: str
    type: str  # e.g., "grammar", "vocab", "listening"

class TopicSuggestionResponse(BaseModel):
    ids: list[str]
    type: str  # e.g., "grammar", "vocab", "listening"
    
class TopicCreateRequest(BaseModel):
    id: str
    name: str
    description: str