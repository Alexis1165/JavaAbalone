package models;

import java.util.List;

public class TripleMove {
	public Node srcNode1;
	public Node srcNode2;
	public Node srcNode3;
	public Node destNode1;
	public Node destNode2;
	public Node destNode3;
	public Node pushNode1;
	public Node pushNode2;
	public Node pushNode3;
	public Boolean elimination;

	public TripleMove CreateTripleMove(List<Node> src, List<Node> dest,
			List<Node> push, Boolean e) {

		TripleMove move = new TripleMove();

		srcNode1 = src.get(0);
		srcNode2 = src.get(1);
		srcNode3 = src.get(2);
		destNode1 = dest.get(0);
		destNode2 = dest.get(1);			
		destNode3 = dest.get(2);			
		pushNode1 = push.get(0);
		pushNode2 = push.get(1);
		pushNode3 = push.get(2);
		elimination = e;		

		return move;
	}	
}
