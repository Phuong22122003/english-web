from app.service.agent_service import Agent
if __name__=='__main__':
    agent = Agent()
    result = agent.invoke({})
    print(result)