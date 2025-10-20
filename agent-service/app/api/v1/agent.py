from fastapi import APIRouter, Depends
from app.schemas.topic import *
router = APIRouter(prefix='/agent')

@router.post("/plan")
def create_plan():
    