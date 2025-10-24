from fastapi import APIRouter, Depends
from app.schemas.topic import *
from app.service import agent_service
router = APIRouter(prefix='/agent')

@router.post("/plan")
async def create_plan(user_info: dict):
    await agent_service.invoke({"user_info": user_info})