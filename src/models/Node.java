package models;

public class Node {
	public char innerVal; 
	public int x;
	public int y;
	public Node left;
	public Node right;	
	
	public Node() {}
	
	public Node(char innerVal, Node left, Node right, int x, int y){
		UpdateNode(innerVal, left, right, x, y);
	}
	
	public void UpdateNode (char innerVal, Node left, Node right, int x, int y){
		this.innerVal = innerVal;
		this.left = left;
		this.right = right;
		this.x = x;
		this.y = y;
	}
}
