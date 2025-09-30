from fastapi import APIRouter, Depends
from app.schemas.topic import *
router = APIRouter()

@router.post("/suggestion")
# return grammar, vocab, listening topic id
def suggest_content(topic:TopicSuggestionRequest) -> TopicSuggestionResponse:
    pass
    
@router.post("/topic")
def add_topic(topic:TopicCreateRequest):
    pass

@router.post("/chat")
def chat_with_agent(message:str) -> str:
    pass