import os
from app.core import settings
from langchain_google_genai import GoogleGenerativeAIEmbeddings
from langchain_qdrant import QdrantVectorStore
from qdrant_client import QdrantClient
from qdrant_client.models import Distance, VectorParams
from app.schemas import TopicCreateRequest
from langchain_core.documents import Document

if not os.environ.get("GOOGLE_API_KEY"):
  os.environ["GOOGLE_API_KEY"] = settings.GOOGLE_API_KEY

class TopicService:
    def __init__(self):
        embeddings = GoogleGenerativeAIEmbeddings(model="models/gemini-embedding-001")
        client = QdrantClient(host="localhost", port=6333)
        collection_name = "topics"
        vector_size = len(embeddings.embed_query("sample text"))
        if not client.collection_exists(collection_name):
            client.create_collection(
                collection_name=collection_name,
                vectors_config=VectorParams(size=vector_size, distance=Distance.COSINE)
            )

        self.vector_store = QdrantVectorStore(
            client=client,
            collection_name=collection_name,
            embedding=embeddings,
        )
    def add_topic(self,topic: TopicCreateRequest):
        doc = Document(page_content= f'{topic.name} {topic.description}',metadata={'id':topic.id,'name':topic.name,'description':topic.description,'topic_type':topic.topic_type })
        self.vector_store.add_documents([doc])
    
    def search(self, query:str) ->list:
        results = self.vector_store.similarity_search(query=query,k=5)
        topics = []
        for result in results:
            metadata = result.metadata
            topic = {
                'id':metadata['id'],
                'name':metadata['name'],
                'description':metadata['description'],
                'topic_type':metadata['topic_type']
            }
            topics.append(topic)
        return topics
            
topic_service = TopicService()