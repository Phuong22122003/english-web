from fastapi import APIRouter, File, UploadFile, Form
from app.schemas import *
from app.service import *
import soundfile as sf
import io
from gtts import gTTS
import eng_to_ipa
import base64
import io
from fastapi.responses import JSONResponse

router = APIRouter()
pronoun_service = PronounciationService()

@router.post("/pronunciation/{text}")
def get_pronunciation(text:str):
    text = text.strip()

    # 1️⃣ Lấy phiên âm IPA
    ipa_text = ipa.convert(text)

    # 2️⃣ Tạo file âm thanh trong bộ nhớ
    speech = gTTS(text, lang='en')
    audio_io = io.BytesIO()
    speech.write_to_fp(audio_io)   # <-- không tạo file thật
    audio_io.seek(0)

    # 3️⃣ Chuyển sang base64
    audio_base64 = base64.b64encode(audio_io.read()).decode("utf-8")

    # 4️⃣ Trả kết quả JSON
    return JSONResponse({
        "text": text,
        "ipa": ipa_text,
        "audio_base64": audio_base64
    })
    
    
import io
import wave

import numpy as np
import scipy.io.wavfile
import soundfile as sf
from scipy.io.wavfile import write

from pydub import AudioSegment
import numpy as np
import io
import soundfile as sf


@router.post("/pronunciation")
async def check_pronunciation(file:UploadFile=File(...), text:str=Form(...)):
    # Đọc toàn bộ bytes từ UploadFile
    audio_bytes = await file.read()

    # # Dùng BytesIO để đọc như file trong bộ nhớ
    audio_io = io.BytesIO(audio_bytes)
    
    audio = AudioSegment.from_file(io.BytesIO(audio_bytes))
    audio = audio.set_channels(1).set_frame_rate(16000)
    
    audio_array = np.array(audio.get_array_of_samples())
    samplerate = audio.frame_rate  # sẽ là 16000 theo set_frame_rate
    # Xuất ra file WAV
    text = text.strip()
    result = pronoun_service.get_ipa_confidence(text_correct=text,audio_array=audio_array,sample_rate = samplerate)

    return result