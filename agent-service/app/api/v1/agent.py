from fastapi import APIRouter, Depends
from app.schemas.topic import *
from app.service import agent_service
from fastapi.responses import JSONResponse
router = APIRouter(prefix='/agent')

@router.post("/plan")
async def create_plan(user_info: dict):
    print("Creating plan...")
    await agent_service.invoke({"user_info": user_info})
    return JSONResponse({"message": "Plan created successfully"})