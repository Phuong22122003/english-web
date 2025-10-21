from langgraph.graph import START, StateGraph, END
from app.schemas.plan import *
from langchain.chat_models import init_chat_model
from .topic_service import topic_service
import json
import os
from app.core import settings
from fastmcp import Client

if not os.environ.get("GOOGLE_API_KEY"):
  os.environ["GOOGLE_API_KEY"] = settings.GOOGLE_API_KEY

class AgentService:
    def __init__(self): 
        self.mcp_path = r"D:\Workspace\Java\Microservice\english-web\agent-service\app\mcp\plan_mcp.py"
        self.llm = init_chat_model("gemini-2.5-flash", model_provider="google_genai")
        
        # Compile application and test
        graph_builder = StateGraph(Plan)
        graph_builder.add_node(self.plan,'plan')
        graph_builder.add_node(self.user_data,'user_data')
        graph_builder.add_node(self.plan_group,'plan_group')
        graph_builder.add_node(self.plan_detail,'plan_detail')
        
        graph_builder.add_edge(START, "user_data")
        graph_builder.add_edge("user_data", "plan")
        graph_builder.add_edge("plan", "plan_group")
        graph_builder.add_edge("plan_group", "plan_detail")
        graph_builder.add_edge("plan_detail", END)
        
        self.graph = graph_builder.compile()
        
    async def invoke(self, input_data:dict):
        await self.graph.ainvoke({'user_info':'I am a beginner in English. I want to improve my English skills for daily communication.'})
    
    async def user_data(self, state: Plan):
        async with Client(self.mcp_path) as client:
            user_info = await client.call_tool("get_user_info", {"user_id": "user-123"})
        return {"user_info": user_info.content[0].text}

    def plan(self, plan: Plan):
        prompt = f"""
You are an expert English learning planner.

Your task:
Based on the user information below, create a general learning plan for the user.  
The plan should include:
- A clear and concise title describing the plan.
- A brief description (2–4 sentences) of what the plan covers and its goal.
- A start date and an end date in ISO format (YYYY-MM-DD).

User information:
{plan['user_info']}

Return ONLY a valid JSON object (no markdown, no explanation, no code block).
Use this exact format:
{{
  "title": "string",
  "description": "string",
  "startDate": "YYYY-MM-DD",
  "endDate": "YYYY-MM-DD"
}}
"""

        response = self.llm.invoke(prompt)
        plan_json = response.content
        if plan_json.startswith("```"):
            plan_json = plan_json.strip("`")       # xóa dấu `
            plan_json = plan_json.replace("json", "", 1).strip()  # xóa chữ 'json' ở đầu nếu có

        plan_response = json.loads(plan_json)
        plan.update(plan_response)
        return plan

    def plan_group(self, plan: Plan):
        prompt=f'''You are an expert English learning planner. Based on the user information and plan information, create a plan group for the user.\n
        User Information: {plan['user_info']}\n
        Plan Information: Title: {plan['title']}, description: {plan['description']}, startDate: {plan['startDate']}, endDate: {plan['endDate']}\n
        Your response should be in JSON format as below
        [{{
            "name": "string",
            "description": "string",
            "startDate": "string",
            "endDate": "string",
        }},
        {{
            "name": "string",
            "description": "string",
            "startDate": "string",
            "endDate": "string",
        }},
        ...
        ]
        '''
        response = self.llm.invoke(prompt)
        plan_group_json = response.content
        if plan_group_json.startswith("```"):
            plan_group_json = plan_group_json.strip("`")       # xóa dấu `
            plan_group_json = plan_group_json.replace("json", "", 1).strip()  # xóa chữ 'json' ở đầu nếu có
        plan_groups = json.loads(plan_group_json)
        plan['planGroups'] = plan_groups
        return plan 
    def plan_detail(self, plan:Plan):
        for group in plan["planGroups"]: 
            data = topic_service.search(group['name'] + group['description'])
            plan_detail = []
            for item in data:
                detail = {
                    'topicType': item['topic_type'],
                    'topicId': item['id']
                }
                plan_detail.append(detail)
            plan.setdefault('planDetails', []).append(plan_detail)
        return plan



# from typing_extensions import List, TypedDict

# class PlanDetail(TypedDict):
#     topicType:str
#     topicId: str

# class PlanGroup(TypedDict):
#     name: str
#     description: str
#     startDate: str
#     endDate: str
#     details: List[PlanDetail] 
    
# class Plan(TypedDict):
#     user_info: str
#     title: str
#     description: str
#     startDate: str
#     endDate:str
#     planGroups: List[PlanGroup]
    
