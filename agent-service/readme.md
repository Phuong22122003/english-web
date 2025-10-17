- run: uvicorn main:app --reload --host 0.0.0.0 --port 5000

- create env: python -m venv .venv
- activate: .venv\Scripts\activate
- deactivate
- pip install requirements.txt