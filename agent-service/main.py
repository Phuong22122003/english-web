from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.v1 import agent

app = FastAPI()

app.include_router(agent.router, prefix="/api/agent-service", tags=["agent"])