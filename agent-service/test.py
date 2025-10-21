from app.service.agent_service import AgentService
import asyncio
if __name__=='__main__':
    agent = AgentService()
    result = asyncio.run( agent.invoke({}))
    print(result)