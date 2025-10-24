from fastmcp import Client
import asyncio
import atexit

class MCPClientHolder:
    _client = None

    @classmethod
    async def get_client(cls):
        if cls._client is None:
            cls._client = await Client(r"D:\PH\english-web\agent-service\app\mcp\plan_mcp.py").__aenter__()
        return cls._client

    @classmethod
    async def close(cls):
        if cls._client:
            await cls._client.__aexit__(None, None, None)
            cls._client = None

# --- ví dụ sử dụng ---
async def main():
    plan = {
        "user_info": {
            "name": "Linh",
            "age": 25,
            "english_level": "intermediate",
            "learning_goal": "improve speaking and listening for business communication",
            "available_time": "1 hour per day",
            "preferred_topics": ["business meetings", "presentations", "small talk"]
        },
        "title": "Business English Speaking Booster",
        "description": "A 6-week plan designed to improve spoken fluency and listening comprehension for professional settings.",
        "startDate": "2025-10-27",
        "endDate": "2025-12-08"
    }
    
    client = await MCPClientHolder.get_client()
    # print('======================')
    prompt = await client.get_prompt("get_plan_group_prompt", arguments={"plan": plan})
    prompt = prompt.messages[0].content
    print(prompt)
    await MCPClientHolder.close()

if __name__ == "__main__":
    asyncio.run(main())
