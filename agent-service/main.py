from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.v1 import agent
from app.api.v1 import pronunciation
app = FastAPI()


app.include_router(agent.router, prefix="/agent-service", tags=["agent"])
app.include_router(pronunciation.router, prefix="/agent-service", tags=["pronounciation"])