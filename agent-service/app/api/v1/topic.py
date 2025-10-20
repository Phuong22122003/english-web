from fastapi import APIRouter, Depends
from app.schemas.topic import *
from app.service import topic_service
router = APIRouter(prefix='/topics')

@router.post("")
def add_topic(topic:TopicCreateRequest):
    topic_service.add_topic(topic)
    return "Add topic successfully"

@router.delete("/{id}")
def delete_topic(id: str):
    pass