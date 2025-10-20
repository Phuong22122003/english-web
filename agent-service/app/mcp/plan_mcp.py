from mcp.server import FastMCP
mcp = FastMCP(
    name='Plan server'
)

@mcp.tool()
def get_user_info(user_id:str):
    pass
if __name__=='__main__':
    mcp.run()