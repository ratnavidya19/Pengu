public class BFSNode {
	
	private int col=0;
	private int row=0;

	BFSNode prev;
	
	boolean westNodeVisited = false;
	boolean northNodeVisited = false;
	boolean eastNodeVisited = false;
	boolean southNodeVisited = false;
	boolean southeastNodeVisited = false;
	boolean southwestVisited = false;
	boolean northeastNodeVisited = false;
	boolean northwestNodeVisited = false;


	
	// Default constructor
	public BFSNode()
	{
		this.col = 0;
		this.row = 0;
	}
	
	// Constructor with row, col and prev node
	public BFSNode( int row, int col,BFSNode prev) 
	{
		this.setCol(col);
		this.setRow(row);
		this.prev = prev;
	}

	
	//Constructor with all fields
	public BFSNode(int row, int col, boolean westNodeVisited,
			boolean northNodeVisited, boolean eastNodeVisited,
			boolean southNodeVisited, boolean southeastNodeVisited,
			boolean southwestVisited, boolean northeastNodeVisited, boolean northwestNodeVisited) 
	{
		this.setCol(col);
		this.setRow(row);
		this.westNodeVisited = westNodeVisited;
		this.northNodeVisited = northNodeVisited;
		this.eastNodeVisited = eastNodeVisited;
		this.southNodeVisited = southNodeVisited;
		this.southeastNodeVisited = southeastNodeVisited;
		this.southwestVisited = southwestVisited;
		this.northeastNodeVisited = northeastNodeVisited;
		this.northwestNodeVisited = northwestNodeVisited;
		//this.currentNode = currentNode;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
	public boolean iswestNodeVisited() {
		return westNodeVisited;
	}

	public void setwestNodeVisited(boolean westNodeVisited) {
		this.westNodeVisited = westNodeVisited;
	}

	
	public boolean isnorthNodeVisited() {
		return northNodeVisited;
	}

	public void setnorthNodeVisited(boolean northNodeVisited) {
		this.northNodeVisited = northNodeVisited;
	}

	
	public boolean isnortheastNodeVisited() {
		return northeastNodeVisited;
	}

	public void setnortheastNodeVisited(boolean northeastNodeVisited) {
		this.northeastNodeVisited = northeastNodeVisited;
	}

	
	
	public boolean issouthNodeVisited() {
		return southNodeVisited;
	}
	

	public void setsouthNodeVisited(boolean southNodeVisited) {
		this.southNodeVisited = southNodeVisited;
	}
	
	
	public boolean issoutheastNodeVisited() {
		return southeastNodeVisited;
	}

	public void setsoutheastNodeVisited(boolean southeastNodeVisited) {
		this.southeastNodeVisited = southeastNodeVisited;
	}

	
	public boolean issouthwestVisited() {
		return southwestVisited;
	}

	public void setsouthwestVisited(boolean southwestVisited) {
		this.southwestVisited = southwestVisited;
	}

	
	public boolean iseastNodeVisited() {
		return eastNodeVisited;
	}

	public void seteastNodeVisited(boolean eastNodeVisited) {
		this.eastNodeVisited = eastNodeVisited;
	}

	
	
	public boolean isnorthwestNodeVisited() {
		return northwestNodeVisited;
	}
	

	public void setsnorthwestNodeVisited(boolean northwestNodeVisited) {
		this.northwestNodeVisited = northwestNodeVisited;
	}
}