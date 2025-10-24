from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    JWT:str='''eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJFbmdsaXNoV2Vic2l0ZSIsInN1YiI6Ijg2ZjVjNzYzLTY2NTAtNDgzMy1hNzk2LTI5YWM0ODA2ZWQ1ZSIsImV4cCI6MTc2MzczNzc5OCwiaWF0IjoxNzYwOTcyOTk4LCJqdGkiOiJjZDkwZTYyYS0zZjg3LTQ1YWUtYWNmYy0zN2Q2YjEyOThlMGIiLCJzY29wZSI6IlJPTEVfVVNFUiJ9.s6Sy-fbspcWxziqqVPY1bhCGIcxmjE8JJ6y72ekjylITK67r5HY2RfdzUncwsBgT8fItrnLcZjFWk-YqCTMJ6A'''
    GOOGLE_API_KEY:str='AIzaSyC25KhSrP9q6CPmGppr44vUVZASFXFsR6g'
    PLAN_SERVICE_CALLBACK_URL: str = "http://localhost:8083/learning-service/plan/callback"
    class Config:
        env_file = ".env"

