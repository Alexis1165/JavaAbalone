package models;

import java.util.List;

public class DoubleMove {
	public Node srcNode1;
	public Node srcNode2;
	public Node destNode1;
	public Node destNode2;
	public Node pushNode1;
	public Node pushNode2;	
	public Boolean elimination;

	public DoubleMove CreateDoubleMove(List<Node> src, List<Node> dest,
			Node pushNode1, Node pushNode2, Boolean e) {
		
		DoubleMove move = new DoubleMove();

		srcNode1 = src.get(0);
		srcNode2 = src.get(1);
		destNode1 = dest.get(0);
		destNode2 = dest.get(1);	
		this.pushNode1 = pushNode1;
		this.pushNode2 = pushNode2;
		elimination = e;
		
		return move;
	}
}
