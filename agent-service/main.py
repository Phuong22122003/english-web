from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.v1 import agent
from app.api.v1 import pronounciation

app = FastAPI()

app.include_router(agent.router, prefix="/agent-service", tags=["agent"])
app.include_router(pronounciation.router, prefix="/agent-service", tags=["agent"])