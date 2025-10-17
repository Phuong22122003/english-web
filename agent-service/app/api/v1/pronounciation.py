from fastapi import APIRouter, File, UploadFile, Form
from app.schemas import *
from app.service import *
import soundfile as sf
import io

router = APIRouter()
pronoun_service = PronounciationService()

@router.post("/pronunciation")
async def check_pronunciation(file:UploadFile=File(...), text:str=Form(...)):
    # Đọc toàn bộ bytes từ UploadFile
    audio_bytes = await file.read()

    # Dùng BytesIO để đọc như file trong bộ nhớ
    audio_io = io.BytesIO(audio_bytes)

    # Đọc âm thanh trực tiếp thành numpy array
    audio_array, samplerate = sf.read(audio_io)

    # Chuyển lại con trỏ BytesIO về đầu để Whisper đọc tiếp
    audio_io.seek(0)
    text = text.strip()
    result = pronoun_service.get_ipa_confidence(text_correct=text,audio_array=audio_array,sample_rate = samplerate)

    return result