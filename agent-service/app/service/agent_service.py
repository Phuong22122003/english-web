from langgraph.graph import START, StateGraph, END
from app.schemas.plan import *
from langchain.chat_models import init_chat_model
from .topic_service import topic_service
import json
import os
from app.core import settings
from fastmcp import Client
import threading, requests
if not os.environ.get("GOOGLE_API_KEY"):
  os.environ["GOOGLE_API_KEY"] = settings.GOOGLE_API_KEY


class MCPClientHolder:
    _client = None

    @classmethod
    async def get_client(cls):
        if cls._client is None:
            cls._client = await Client(r"D:\Documents\DATN\backend\agent-service\app\mcp\plan_mcp.py").__aenter__()
        return cls._client

    @classmethod
    async def close(cls):
        if cls._client:
            await cls._client.__aexit__(None, None, None)
            cls._client = None

class AgentService:
    def __init__(self): 
        self.llm = init_chat_model("gemini-2.5-flash-lite", model_provider="google_genai")
        
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
        result = await self.graph.ainvoke({'user_info': input_data})
        return result
    
    async def user_data(self, state: Plan):
        print("Fetching user data...")
        client = await MCPClientHolder.get_client()
        user_info = await client.call_tool("get_user_info", {"user_id": "user-123"})
        return {"user_info": user_info.content[0].text}

    async def plan(self, plan: Plan):
        print("Creating plan...")
        client = await MCPClientHolder.get_client()
        prompt = await client.get_prompt("get_plan_prompt", arguments={"user_info": plan["user_info"]})
        prompt = prompt.messages[0].content.text

        response = self.llm.invoke(prompt)
        plan_json = response.content
        if plan_json.startswith("```"):
            plan_json = plan_json.strip("`")       # xóa dấu `
            plan_json = plan_json.replace("json", "", 1).strip()  # xóa chữ 'json' ở đầu nếu có

        plan_response = json.loads(plan_json)
        plan.update(plan_response)
        return plan

    async def plan_group(self, plan: Plan):
        print("Creating plan groups...")
        client = await MCPClientHolder.get_client()
        prompt = await client.get_prompt("get_plan_group_prompt", arguments={"plan": plan})
        prompt = prompt.messages[0].content.text
        
        response = self.llm.invoke(prompt)
        plan_group_json = response.content
        if plan_group_json.startswith("```"):
            plan_group_json = plan_group_json.strip("`")       # xóa dấu `
            plan_group_json = plan_group_json.replace("json", "", 1).strip()  # xóa chữ 'json' ở đầu nếu có
        plan_groups = json.loads(plan_group_json)
        plan['planGroups'] = plan_groups
        return plan 
        
    async def plan_detail(self, plan:Plan):
        print("Creating plan details...")
        client = await MCPClientHolder.get_client()
        for group in plan["planGroups"]: 
            data = topic_service.search(group['name'] + group['description'])

            prompt = await client.get_prompt("get_plan_detail_prompt", arguments={"group": group, "plan": plan, "topics": data})
            prompt = prompt.messages[0].content.text

            response = self.llm.invoke(prompt)
            result_json = response.content.strip()
            if result_json.startswith("```"):
                result_json = result_json.strip("`").replace("json", "").strip()
            evaluation = json.loads(result_json)

            plan_detail = []
            for item in evaluation:
                if item.get("approved"):
                    topic_id = item["topicId"]
                    topic = next((t for t in data if t["id"] == topic_id), None)
                    if topic:
                        plan_detail.append({"topicType": topic["topic_type"], "topicId": topic["id"]})
            if plan_detail:
                group.setdefault("planDetails", []).extend(plan_detail)
        fire_and_forget(settings.PLAN_SERVICE_CALLBACK_URL, plan)
        return plan

def fire_and_forget(url, data):
    print("Sending callback...")
    try:
        headers = {
                "Authorization": f"Bearer {settings.JWT}",
                "Content-Type": "application/json"
        }
        requests.post(
            url,
            json=data,
            headers=headers
        )
    except requests.exceptions.ReadTimeout:
        print("Request sent, not waiting for response.")

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
    
