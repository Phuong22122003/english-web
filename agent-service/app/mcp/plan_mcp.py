from mcp.server import FastMCP
mcp = FastMCP(
    name='Plan server'
)

@mcp.tool()
def get_user_info(user_id:str):
    '''Get user information by user ID'''
    return  'I am a beginner in English. I want to improve my English skills for grammar and vocabulary.'

if __name__=='__main__':
    mcp.run()