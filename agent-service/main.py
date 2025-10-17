from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.v1 import agent
from app.api.v1 import pronounciation
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Cho phép tất cả (hoặc ghi cụ thể: ["http://localhost:4200"])
    allow_credentials=True,
    allow_methods=["*"],  # Cho phép mọi method: GET, POST, PUT, DELETE, ...
    allow_headers=["*"],  # Cho phép mọi header
)


app.include_router(agent.router, prefix="/agent-service", tags=["agent"])
app.include_router(pronounciation.router, prefix="/agent-service", tags=["pronounciation"])