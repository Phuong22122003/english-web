from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    GOOGLE_API_KEY:str='AIzaSyC25KhSrP9q6CPmGppr44vUVZASFXFsR6g'

    class Config:
        env_file = ".env"

