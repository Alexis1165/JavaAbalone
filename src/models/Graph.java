package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import helpers.Constants;

public class Graph {
	public List<List<Node>> nodes;
	int min = 5;
	int max = 8;	

	public Graph(){
		nodes = new ArrayList<>();
		List<Node> nodeArr = new ArrayList<>();
		for (int i = 0 ; i < min ; ++i) 
			nodeArr.add(AddNode(Constants.BLACK, 0, i));

		nodes.add(nodeArr);

		for (int i = 0 ; i < min - 1 ; ++i) {
			nodeArr = new ArrayList<>();
			for (int j = 0 ; j < nodes.get(i).size() ; ++j) {
				int innerVal = Constants.EMPTY;

				if (i < 1) innerVal = Constants.BLACK;
				else if (i == 1 && j > 0 && j < 4) innerVal = Constants.BLACK;

				if (j == 0) {
					nodes.get(i).get(j).left = AddNode(innerVal, i + 1, j);
					nodeArr.add(nodes.get(i).get(j).left);
				}

				else nodes.get(i).get(j).left = nodes.get(i).get(j - 1).right;

				nodes.get(i).get(j).right = AddNode(innerVal, i + 1, j + 1);
				nodeArr.add(nodes.get(i).get(j).right);
			}
			nodes.add(nodeArr);
		}	


		for (int i = min - 1 ; i < max ; ++i) {
			nodeArr = new ArrayList<>();
			for (int j = 0 ; j < nodes.get(i).size() ; ++j) {
				int innerVal = Constants.EMPTY;

				if (i > 5) innerVal = Constants.WHITE;
				else if (i == min && j > 1 && j < 5) innerVal = Constants.WHITE;

				if (j != 0)
					nodes.get(i).get(j).left = nodes.get(i).get(j - 1).right;

				if (j < nodes.get(i).size() - 1) {
					nodes.get(i).get(j).right = AddNode(innerVal, i + 1, j);
					nodeArr.add(nodes.get(i).get(j).right);
				}
			}
			nodes.add(nodeArr);
		}	
	}

	public Node AddNode(int innerVal, int x, int y) {
		return new Node(Constants.values[innerVal], null, null, x, y);		
	}

	public void AddChildren(Node parent, Node left, Node right) {			
		parent.left = left;
		parent.right = right;
	}

	public void PrintNodes() {

		int spaces = 4;
		for (int i = 0; i < 5; ++i) {
			System.out.print(i + "|       ");
			PrintSpaces(spaces);
			for (int j = 0; j < nodes.get(i).size(); ++j) {
				System.out.print(nodes.get(i).get(j).innerVal + " ");
			}
			--spaces;
			System.out.println("");
		}

		++spaces;

		for (int i = 5; i < 9; ++i) {
			++spaces;
			System.out.print(i + "|       ");
			PrintSpaces(spaces);
			for (int j = 0; j < nodes.get(i).size(); ++j) {
				System.out.print(nodes.get(i).get(j).innerVal + " ");
			}
			System.out.println("");
		}
		System.out.println("===================================");
		System.out.println("8        0 1 2 3 4 5 6 7 8");
		System.out.println("");  		
	}

	public static void PrintSpaces(int spaces) {
		for (int i = 0; i < spaces; ++i) {
			System.out.print(" ");
		}
	}  	

	public List<Node> GetSingleMove(List<Node> src){
		int x = src.get(0).x;
		int y = src.get(0).y;

		List<Node> move = new ArrayList<>();
		List<Node> parents = GetParents(src.get(0), x); 

		if (y < 8 && nodes.get(x).get(y + 1).innerVal == 
				Constants.values[Constants.EMPTY])
			move.add(nodes.get(x).get(y + 1));

		if (y > 0 && nodes.get(x).get(y - 1).innerVal == 
				Constants.values[Constants.EMPTY])
			move.add(nodes.get(x).get(y - 1));

		if (parents.size() > 0)  
			for (int i = 0 ; i < parents.size() ; ++i)
				if(parents.get(i).innerVal == 
				Constants.values[Constants.EMPTY]) 
					move.add(parents.get(i));

		if (src.get(0).left.innerVal == 
				Constants.values[Constants.EMPTY])
			move.add(src.get(0).left);

		if (src.get(0).right.innerVal == 
				Constants.values[Constants.EMPTY])
			move.add(src.get(0).right);

		return move;
	}

	public List<DoubleMove> GetDoubleMoveH(List<Node> src, int enemy) {
		List<DoubleMove> dm = new ArrayList<>();
		Node greater = new Node();
		Node smaller = new Node();


		if (src.get(0).y > src.get(1).y) { 
			greater = src.get(0);
			smaller = src.get(1);
		}

		else {
			greater = src.get(1);
			smaller = src.get(0);
		}

		int x1 = smaller.x; int y1 = smaller.y;
		int x2 = greater.x; int y2 = greater.y;

		if (y2 < 8 && nodes.get(x2).get(y2 + 1).innerVal == 
				Constants.values[Constants.EMPTY]) {
			DoubleMove m = new DoubleMove();
			m.CreateDoubleMove(Arrays.asList(smaller, greater), 
					Arrays.asList(greater, nodes.get(x2).get(y2 + 1)), 
					null, null, false);			
			dm.add(m);
		}

		else if (y2 < 8 && nodes.get(x2).get(y2 + 1).innerVal == 
				Constants.values[enemy]) {
			if (y2 + 2 < 8) {
				if (nodes.get(x2).get(y2 + 2).innerVal == 
						Constants.values[Constants.EMPTY]) {
					DoubleMove m = new DoubleMove();
					m.CreateDoubleMove(Arrays.asList(smaller, greater), 
							Arrays.asList(greater, nodes.get(x2).get(y2 + 1)), 
							nodes.get(x2).get(y2 + 1), nodes.get(x2).get(y2 + 2), 
							false);					
					dm.add(m);					
				}
			}

			else {
				DoubleMove m = new DoubleMove();
				m.CreateDoubleMove(Arrays.asList(smaller, greater), 
						Arrays.asList(greater, nodes.get(x2).get(y2 + 1)), 
						nodes.get(x2).get(y2 + 1), nodes.get(x2).get(y2 + 2), 
						true);					
				dm.add(m);						
			}
		}

		if (y1 > 0 && nodes.get(x1).get(y1 - 1).innerVal == 
				Constants.values[Constants.EMPTY]) {
			DoubleMove m = new DoubleMove();
			m.CreateDoubleMove(Arrays.asList(smaller, greater), 
					Arrays.asList(smaller, nodes.get(x1).get(y1 - 1)), 
					null, null, false);			
			dm.add(m);
		}	

		else if (y1 > 0 && nodes.get(x1).get(y1 - 1).innerVal == 
				Constants.values[enemy]) {
			if (y1 - 2 > -1) {
				if (nodes.get(x1).get(y1 - 2).innerVal == 
						Constants.values[Constants.EMPTY]) {
					DoubleMove m = new DoubleMove();
					m.CreateDoubleMove(Arrays.asList(smaller, greater), 
							Arrays.asList(smaller, nodes.get(x1).get(y1 - 1)), 
							nodes.get(x1).get(y1 - 1), nodes.get(x1).get(y1 - 2), 
							false);					
					dm.add(m);					
				}
			}

			else {
				DoubleMove m = new DoubleMove();
				m.CreateDoubleMove(Arrays.asList(smaller, greater), 
						Arrays.asList(smaller, nodes.get(x1).get(y1 - 1)), 
						nodes.get(x1).get(y1 - 1), null, true);				
				dm.add(m);					
			}
		}			

		return dm;
	}	

	public List<DoubleMove> GetDoubleMoveV(List<Node> src, int enemy) {
		List<DoubleMove> dm = new ArrayList<>();

		Node greater = new Node();
		Node smaller = new Node();
		int direction = 0;


		if (src.get(0).left == src.get(1)) { 
			greater = src.get(1);
			smaller = src.get(0);
			direction = 1;
		}

		else if (src.get(0).right == src.get(1)) { 
			greater = src.get(1);
			smaller = src.get(0);
			direction = 2;
		}

		else if (src.get(1).left == src.get(0)) { 
			greater = src.get(0);
			smaller = src.get(1);
			direction = 1;
		}

		else if (src.get(1).right == src.get(0)) { 
			greater = src.get(0);
			smaller = src.get(1);
			direction = 2;
		}

		int x1 = smaller.x; int y1 = smaller.y;
		int x2 = greater.x; int y2 = greater.y;
		Node nextNode = new Node();
		Node prevNode = new Node();
		Node nextNextNode = new Node();
		Node prevPrevNode = new Node();

		if (direction == 1) { 
			nextNode = nodes.get(x2).get(y2).left;
			nextNextNode = nextNode.left;

			if (GetParents(nodes.get(x1).get(y1), x1).size() > 0)
				prevNode = GetParents(nodes.get(x1).get(y1), x1).get(1);			

			if(GetParents(prevNode, prevNode.x).size() > 0)
				prevPrevNode = GetParents(prevNode, prevNode.x).get(1);
		}

		else {
			nextNode = nodes.get(x2).get(y2).right;
			nextNextNode = nextNode.right;

			if(GetParents(nodes.get(x1).get(y1), x1).size() > 0)
				prevNode = GetParents(nodes.get(x1).get(y1), x1).get(0);

			if(GetParents(prevNode, prevNode.x).size() > 0)
				prevPrevNode = GetParents(prevNode, prevNode.x).get(0);
		}



		if (x2 < 8 && nextNode.innerVal == 
				Constants.values[Constants.EMPTY]) {
			DoubleMove m = new DoubleMove();
			m.CreateDoubleMove(Arrays.asList(smaller, greater), 
					Arrays.asList(greater, nextNode), 
					null, null, false);			
			dm.add(m);
		}	

		else if (x2 < 8 && nextNode.innerVal == 
				Constants.values[enemy]) {
			if (x2 + 2 < 8) {
				if (nextNextNode.innerVal == 
						Constants.values[Constants.EMPTY]) {
					DoubleMove m = new DoubleMove();
					m.CreateDoubleMove(Arrays.asList(smaller, greater), 
							Arrays.asList(greater, nextNode), 
							nextNode, nextNextNode, false);					
					dm.add(m);					
				}
			}

			else {
				DoubleMove m = new DoubleMove();
				m.CreateDoubleMove(Arrays.asList(smaller, greater), 
						Arrays.asList(greater, nextNode), 
						nextNode, null, true);				
				dm.add(m);					
			}
		}	

		if (x1 > 0 && prevNode.innerVal == 
				Constants.values[Constants.EMPTY]) {
			DoubleMove m = new DoubleMove();
			m.CreateDoubleMove(Arrays.asList(smaller, greater), 
					Arrays.asList(smaller, prevNode), 
					null, null, false);			
			dm.add(m);
		}	

		else if (x1 > 0 && prevNode.innerVal == 
				Constants.values[enemy]) {
			if (x1 - 2 > 0) {
				if (prevPrevNode.innerVal == 
						Constants.values[Constants.EMPTY]) {
					DoubleMove m = new DoubleMove();
					m.CreateDoubleMove(Arrays.asList(smaller, greater), 
							Arrays.asList(smaller, prevNode), 
							prevNode, prevPrevNode, false);					
					dm.add(m);					
				}
			}

			else {
				DoubleMove m = new DoubleMove();
				m.CreateDoubleMove(Arrays.asList(smaller, greater), 
						Arrays.asList(smaller, prevNode), 
						prevNode, null, true);					
				dm.add(m);					
			}
		}			

		return dm;		
	}

	public List<TripleMove> GetTripleMoveH(List<Node> src, int enemy) {
		List<TripleMove> tm = new ArrayList<>();
		int[] x = new int[] {src.get(0).x, src.get(1).x, src.get(2).x};
		int[] y = new int[] {src.get(0).y, src.get(1).y, src.get(2).y};
		Node greater = new Node();
		Node middle = new Node();
		Node smaller = new Node();

		Arrays.sort(x);
		Arrays.sort(y);
		greater = nodes.get(x[2]).get(y[2]);
		middle = nodes.get(x[1]).get(y[1]);
		smaller = nodes.get(x[0]).get(y[0]);


		if (y[2] < 8 && nodes.get(x[2]).get(y[2] + 1).innerVal == 
				Constants.values[Constants.EMPTY]) {
			TripleMove m = new TripleMove();
			m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
					Arrays.asList(middle, greater, nodes.get(x[2]).get(y[2] + 1)), 
					Arrays.asList(null, null, null),
					false);			
			tm.add(m);			
		}		

		else if (y[2] < 8 && nodes.get(x[2]).get(y[2] + 1).innerVal == 
				Constants.values[enemy]) {
			if (y[2] + 2 < 8) {
				if (nodes.get(x[2]).get(y[2] + 2).innerVal == 
						Constants.values[Constants.EMPTY]) {
					TripleMove m = new TripleMove();
					m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
							Arrays.asList(middle, greater, nodes.get(x[2]).get(y[2] + 1)), 
							Arrays.asList(nodes.get(x[2]).get(y[2] + 1), 
									nodes.get(x[2]).get(y[2] + 2), null),
							false);			
					tm.add(m);					
				}

				else if (nodes.get(x[2]).get(y[2] + 2).innerVal == 
						Constants.values[enemy]) {
					if (y[2] + 3 < 8) {
						if (nodes.get(x[2]).get(y[2] + 3).innerVal == 
								Constants.values[Constants.EMPTY]) {
							TripleMove m = new TripleMove();
							m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
									Arrays.asList(middle, greater, nodes.get(x[2]).get(y[2] + 1)), 
									Arrays.asList(nodes.get(x[2]).get(y[2] + 1), 
											nodes.get(x[2]).get(y[2] + 2), 
											nodes.get(x[2]).get(y[2] + 3)),									
									false);			
							tm.add(m);					
						}						
					}

					else {
						TripleMove m = new TripleMove();
						m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
								Arrays.asList(middle, greater, nodes.get(x[2]).get(y[2] + 1)), 
								Arrays.asList(nodes.get(x[2]).get(y[2] + 1), 
										nodes.get(x[2]).get(y[2] + 2), 
										nodes.get(x[2]).get(y[2] + 3)),
								true);			
						tm.add(m);							
					}
				}
			}

			else {
				TripleMove m = new TripleMove();
				m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
						Arrays.asList(middle, greater, nodes.get(x[2]).get(y[2] + 1)), 
						Arrays.asList(nodes.get(x[2]).get(y[2] + 1),
								nodes.get(x[2]).get(y[2] + 2), null),
						true);			
				tm.add(m);				
			}
		}

		if (y[0] > 0 && nodes.get(x[0]).get(y[0] - 1).innerVal == 
				Constants.values[Constants.EMPTY]) {
			TripleMove m = new TripleMove();
			m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
					Arrays.asList(smaller, middle, nodes.get(x[0]).get(y[0] - 1)), 
					Arrays.asList(null, null, null),
					false);			
			tm.add(m);			
		}		

		else if (y[0] > 0 && nodes.get(x[0]).get(y[0] - 1).innerVal == 
				Constants.values[enemy]) {
			if (y[0] - 2 > 0) {
				if (nodes.get(x[0]).get(y[0] - 2).innerVal == 
						Constants.values[Constants.EMPTY]) {
					TripleMove m = new TripleMove();
					m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
							Arrays.asList(smaller, middle, nodes.get(x[0]).get(y[0] - 1)), 
							Arrays.asList(nodes.get(x[0]).get(y[0] - 1), 
									nodes.get(x[0]).get(y[0] - 2), null),
							false);			
					tm.add(m);					
				}

				else if (nodes.get(x[0]).get(y[0] - 2).innerVal == 
						Constants.values[enemy]) {
					if (y[0] - 3 > 0) {
						if (nodes.get(x[0]).get(y[0] - 3).innerVal == 
								Constants.values[Constants.EMPTY]) {
							TripleMove m = new TripleMove();
							m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
									Arrays.asList(smaller, middle, nodes.get(x[0]).get(y[0] - 1)), 
									Arrays.asList(nodes.get(x[0]).get(y[0] - 1), 
											nodes.get(x[0]).get(y[0] - 2), 
											nodes.get(x[0]).get(y[0] - 3)),
									false);			
							tm.add(m);					
						}						
					}

					else {
						TripleMove m = new TripleMove();
						m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
								Arrays.asList(middle, greater, nodes.get(x[0]).get(y[0] - 1)), 
								Arrays.asList(nodes.get(x[0]).get(y[0] - 1), 
										nodes.get(x[0]).get(y[0] - 2),
										nodes.get(x[0]).get(y[0] - 3)),
								true);			
						tm.add(m);							
					}
				}
			}

			else {
				TripleMove m = new TripleMove();
				m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
						Arrays.asList(middle, greater, nodes.get(x[0]).get(y[0] - 1)), 
						Arrays.asList(nodes.get(x[0]).get(y[0] - 1), 
								nodes.get(x[0]).get(y[0] - 2), null),
						true);			
				tm.add(m);				
			}
		}		
		return tm;
	}

	public List<TripleMove> GetTripleMoveV(List<Node> src, int enemy) {
		List<TripleMove> tm = new ArrayList<>();		
		Node smaller = new Node();
		Node middle = new Node();
		Node greater = new Node();
		

		if(src.get(0).x < src.get(1).x && src.get(0).x < src.get(2).x) {
			smaller = nodes.get(src.get(0).x).get(src.get(0).y);
			
			if (src.get(1).x < src.get(2).x) {
				middle = nodes.get(src.get(1).x).get(src.get(1).y);
				greater = nodes.get(src.get(2).x).get(src.get(2).y);
			}
			
			else {
				middle = nodes.get(src.get(2).x).get(src.get(2).y);
				greater = nodes.get(src.get(1).x).get(src.get(1).y);
			}
		}

		else if(src.get(1).x < src.get(0).x && src.get(1).x < src.get(2).x) {
			smaller = nodes.get(src.get(1).x).get(src.get(1).y);
			
			if (src.get(0).x < src.get(2).x) {
				middle = nodes.get(src.get(0).x).get(src.get(0).y);
				greater = nodes.get(src.get(2).x).get(src.get(2).y);
			}
			
			else {
				middle = nodes.get(src.get(2).x).get(src.get(2).y);
				greater = nodes.get(src.get(0).x).get(src.get(0).y);
			}
		}

		else if(src.get(2).x < src.get(0).x && src.get(2).x < src.get(1).x) {
			smaller = nodes.get(src.get(2).x).get(src.get(2).y);
			
			if (src.get(0).x < src.get(1).x) {
				middle = nodes.get(src.get(0).x).get(src.get(0).y);
				greater = nodes.get(src.get(1).x).get(src.get(1).y);
			}
			
			else {
				middle = nodes.get(src.get(1).x).get(src.get(1).y);
				greater = nodes.get(src.get(0).x).get(src.get(0).y);
			}
		}		
		
		int[] x = new int[] {smaller.x, middle.x, greater.x};
		int[] y = new int[] {smaller.y, middle.y, greater.y};
		Node nextNode = new Node();
		Node prevNode = new Node();
		Node nextNextNode = new Node();
		Node prevPrevNode = new Node();		
		Node nextNNode = new Node();
		Node prevPNode = new Node();	
			
		if (nodes.get(x[0]).get(y[0]).left == nodes.get(x[1]).get(y[1]) &&
				nodes.get(x[1]).get(y[1]).left == nodes.get(x[2]).get(y[2])) {
			
			if (nodes.get(x[2]).get(y[2]).left != null)
			nextNode = nodes.get(x[2]).get(y[2]).left;
			
			if (nextNode.left != null)
			nextNextNode = nextNode.left;
			
			if (nextNextNode.left != null)
			nextNNode = nextNextNode.left;

			if (GetParents(nodes.get(x[0]).get(y[0]), x[0]).size() > 0)
				prevNode = GetParents(nodes.get(x[0]).get(y[0]), x[0]).get(1);

			if (GetParents(prevNode, prevNode.x).size() > 0)
				prevPrevNode = GetParents(prevNode, prevNode.x).get(1);			

			if (GetParents(prevPrevNode, prevPrevNode.x).size() > 0)			
				prevPNode = GetParents(prevPrevNode, prevPrevNode.x).get(1);			
		}

		else if (nodes.get(x[0]).get(y[0]).right == nodes.get(x[1]).get(y[1]) &&
				nodes.get(x[1]).get(y[1]).right == nodes.get(x[2]).get(y[2])) {
			
			if (nodes.get(x[2]).get(y[2]).right != null)
			nextNode = nodes.get(x[2]).get(y[2]).right;
			
			if (nextNode.right != null)
			nextNextNode = nextNode.right;
			
			if (nextNextNode.right != null)
			nextNNode = nextNextNode.right;

			if (GetParents(nodes.get(x[0]).get(y[0]), x[0]).size() > 0)
				prevNode = GetParents(nodes.get(x[0]).get(y[0]), x[0]).get(0);

			if (GetParents(prevNode, prevNode.x).size() > 0)
				prevPrevNode = GetParents(prevNode, prevNode.x).get(0);			

			if (GetParents(prevPrevNode, prevPrevNode.x).size() > 0)			
				prevPNode = GetParents(prevPrevNode, prevPrevNode.x).get(0);		
		}

		if (x[2] < 8 && nextNode.innerVal == 
				Constants.values[Constants.EMPTY]) {
			TripleMove m = new TripleMove();
			m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
					Arrays.asList(middle, greater, nextNode), 
					Arrays.asList(null, null, null),
					false);			
			tm.add(m);			
		}		

		else if (x[2] < 8 && nextNode.innerVal == 
				Constants.values[enemy]) {
			if (x[2] + 2 < 8) {
				if (nextNextNode.innerVal == 
						Constants.values[Constants.EMPTY]) {
					TripleMove m = new TripleMove();
					m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
							Arrays.asList(middle, greater, nextNode), 
							Arrays.asList(nextNode, nextNextNode, null),
							false);			
					tm.add(m);					
				}

				else if (nextNextNode.innerVal == 
						Constants.values[enemy]) {
					if (x[2] + 3 < 8) {
						if (nextNNode.innerVal == 
								Constants.values[Constants.EMPTY]) {
							TripleMove m = new TripleMove();
							m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
									Arrays.asList(middle, greater, nextNode), 
									Arrays.asList(nextNode, nextNextNode, nextNNode),
									false);			
							tm.add(m);					
						}						
					}

					else {
						TripleMove m = new TripleMove();
						m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
								Arrays.asList(middle, greater, nextNode), 
								Arrays.asList(nextNode, nextNextNode, nextNNode),
								true);			
						tm.add(m);							
					}
				}
			}

			else {
				TripleMove m = new TripleMove();
				m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
						Arrays.asList(middle, greater, nextNode), 
						Arrays.asList(nextNode, nextNextNode, null),
						true);			
				tm.add(m);				
			}
		}		

		if (x[0] > 0 && prevNode.innerVal == 
				Constants.values[Constants.EMPTY]) {
			TripleMove m = new TripleMove();
			m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
					Arrays.asList(prevNode, smaller, middle), 
					Arrays.asList(null, null, null),
					false);			
			tm.add(m);			
		}			

		else if (x[0] > 0 && prevNode.innerVal == 
				Constants.values[enemy]) {
			if (x[0] - 2 > 0) {
				if (prevPrevNode.innerVal == 
						Constants.values[Constants.EMPTY]) {
					TripleMove m = new TripleMove();
					m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
							Arrays.asList(prevNode, smaller, middle), 
							Arrays.asList(prevNode, prevPrevNode, null),
							false);			
					tm.add(m);					
				}

				else if (prevPrevNode.innerVal == 
						Constants.values[enemy]) {
					if (x[0] - 3 > 0) {
						if (prevPNode.innerVal == 
								Constants.values[Constants.EMPTY]) {
							TripleMove m = new TripleMove();
							m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
									Arrays.asList(prevNode, smaller, middle), 
									Arrays.asList(prevNode, prevPrevNode, prevPNode),
									false);			
							tm.add(m);					
						}						
					}

					else {
						TripleMove m = new TripleMove();
						m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
								Arrays.asList(prevNode, smaller, middle), 
								Arrays.asList(prevNode, prevPNode, prevPNode),
								true);			
						tm.add(m);							
					}
				}
			}

			else {
				TripleMove m = new TripleMove();
				m.CreateTripleMove(Arrays.asList(smaller, middle, greater), 
						Arrays.asList(prevNode, smaller, middle), 
						Arrays.asList(prevNode, prevPrevNode, null),
						true);			
				tm.add(m);				
			}
		}			
		return tm;
	}

	public List<Node> GetParents(Node node, int level){
		List<Node> parents = new ArrayList<>();

		try {
			for (int i = 0 ; i < nodes.get(level - 1).size() ; ++i) {
				if (nodes.get(level - 1).get(i).left == node) 
					parents.add(nodes.get(level - 1).get(i));
				else if (nodes.get(level - 1).get(i).right == node) 
					parents.add(nodes.get(level - 1).get(i));
			}
		}

		catch(Exception ex) {

		}

		return parents;
	}	

	public void ChangePosition(int x1, int y1, int x2, int y2) {
		Node src = nodes.get(x1).get(y1); 
		Node dest = nodes.get(x2).get(y2); 
		char srcVal = src.innerVal;
//		char destVal = dest.innerVal;

		nodes.get(x1).get(y1).UpdateNode('0', src.left, 
				src.right, src.x, src.y);
		nodes.get(x2).get(y2).UpdateNode(srcVal, dest.left, 
				dest.right, dest.x, dest.y);		
	}

	public void PrintSinglePossibilities(List<Node> m, Node src) {
		System.out.println("Move Posibilities: ");
		System.out.println("===================================");													
		System.out.print("Moves from");
		System.out.println("(" + src.x + ", " + src.y + ") =>>");

		for (int i = 0 ; i < m.size() ; ++i) {
			System.out.println("");
			System.out.println("Move #" + (i + 1));
			System.out.print("(" + m.get(i).x + ", " + m.get(i).y + ") ");
		}		

		System.out.println("");		
		System.out.println("===================================");											
	}

	public void PrintDoublePossibilities(List<DoubleMove> m) {
		System.out.println("Move Posibilities: ");
		System.out.println("===================================");											
		for (int i = 0 ; i < m.size() ; ++i) {
			System.out.println("Move #" + (i + 1) + " from ");	
			System.out.print("(" + m.get(i).srcNode1.x + ", " + m.get(i).srcNode1.y + "), ");
			System.out.println("(" + m.get(i).srcNode2.x + ", " + m.get(i).srcNode2.y + "), =>> ");
			System.out.println("");
			if (m.get(i).destNode1.x < m.get(i).destNode2.x) {
				System.out.print("(" + m.get(i).destNode1.x + ", " + m.get(i).destNode1.y + "), ");
				System.out.print("(" + m.get(i).destNode2.x + ", " + m.get(i).destNode2.y + ") ");
			}

			else {
				System.out.print("(" + m.get(i).destNode2.x + ", " + m.get(i).destNode2.y + ") ");
				System.out.print("(" + m.get(i).destNode1.x + ", " + m.get(i).destNode1.y + "), ");				
			}

			System.out.println("");
			if(m.get(i).pushNode1 != null) {
				System.out.print("Push possible from (" + m.get(i).pushNode1.x + ", " + m.get(i).pushNode1.y + ") to ");				
				System.out.println("(" + m.get(i).pushNode2.x + ", " + m.get(i).pushNode2.y + ")");				
			}

			if (m.get(i).elimination) {
				System.out.print("Elimination Possible!!");								
			}
			System.out.println("===================================");											
		}
	}

	public void PrintTriplePossibilities(List<TripleMove> m) {
		System.out.println("Move Posibilities: ");
		System.out.println("===================================");											
		for (int i = 0 ; i < m.size() ; ++i) {
			int[] X = new int[] {m.get(i).destNode1.x, m.get(i).destNode2.x, m.get(i).destNode3.x};
			int[] Y = new int[] {m.get(i).destNode1.y, m.get(i).destNode2.y, m.get(i).destNode3.y};
			Arrays.sort(X);
			Arrays.sort(Y);

			System.out.println("Move #" + (i + 1) + " from ");			
			System.out.print("(" + m.get(i).srcNode1.x + ", " + m.get(i).srcNode1.y + "), ");
			System.out.print("(" + m.get(i).srcNode2.x + ", " + m.get(i).srcNode2.y + "), ");
			System.out.println("(" + m.get(i).srcNode3.x + ", " + m.get(i).srcNode3.y + ") =>>");
			System.out.println("");

			if (X[0] == X[1] && X[1] == X[2]) {
				System.out.print("(" + X[0] + ", " + Y[0] + "), ");				
				System.out.print("(" + X[1] + ", " + Y[1] + ") ");
				System.out.print("(" + X[2] + ", " + Y[2] + ") ");
				
			}
			
			else if(X[0] < X[1] && X[0] < X[2]) {
				System.out.print("(" + m.get(i).destNode1.x + ", " + m.get(i).destNode1.y + "), ");				
				
				if (X[1] < X[2]) {
					System.out.print("(" + m.get(i).destNode2.x + ", " + m.get(i).destNode2.y + "), ");				
					System.out.print("(" + m.get(i).destNode3.x + ", " + m.get(i).destNode3.y + "), ");									
				}
				
				else {
					System.out.print("(" + m.get(i).destNode3.x + ", " + m.get(i).destNode3.y + "), ");									
					System.out.print("(" + m.get(i).destNode2.x + ", " + m.get(i).destNode2.y + "), ");									
				}
			}

			else if(X[1] < X[0] && X[1] < X[2]) {
				System.out.print("(" + m.get(i).destNode2.x + ", " + m.get(i).destNode2.y + "), ");				
				
				if (X[0] < X[2]) {
					System.out.print("(" + m.get(i).destNode1.x + ", " + m.get(i).destNode1.y + "), ");				
					System.out.print("(" + m.get(i).destNode3.x + ", " + m.get(i).destNode3.y + "), ");									
				}
				
				else {
					System.out.print("(" + m.get(i).destNode3.x + ", " + m.get(i).destNode3.y + "), ");									
					System.out.print("(" + m.get(i).destNode1.x + ", " + m.get(i).destNode1.y + "), ");									
				}
			}

			else if(X[2] < X[0] && X[2] < X[1]) {
				System.out.print("(" + m.get(i).destNode3.x + ", " + m.get(i).destNode3.y + "), ");				
				
				if (X[0] < X[1]) {
					System.out.print("(" + m.get(i).destNode1.x + ", " + m.get(i).destNode1.y + "), ");				
					System.out.print("(" + m.get(i).destNode2.x + ", " + m.get(i).destNode2.y + "), ");									
				}
				
				else {
					System.out.print("(" + m.get(i).destNode2.x + ", " + m.get(i).destNode2.y + "), ");									
					System.out.print("(" + m.get(i).destNode1.x + ", " + m.get(i).destNode1.y + "), ");									
				}
			}
			
			System.out.println("");
			if(m.get(i).pushNode1 != null) {
				System.out.print("Push possible from (" + m.get(i).pushNode1.x + ", " + m.get(i).pushNode1.y + "), ");				
				System.out.print("(" + m.get(i).pushNode2.x + ", " + m.get(i).pushNode2.y + ") to ");				
				System.out.print("(" + m.get(i).pushNode2.x + ", " + m.get(i).pushNode2.y + "), ");				
				System.out.println("(" + m.get(i).pushNode3.x + ", " + m.get(i).pushNode3.y + ")");				
			}

			if (m.get(i).elimination) {
				System.out.print("Elimination Possible!!");								
			}
			System.out.println("===================================");											
		}		
	}	

	public void PushDouble(DoubleMove m) {
		ChangePosition(m.pushNode1.x, m.pushNode1.y, m.pushNode2.x, m.pushNode2.y);
		ChangePosition(m.srcNode1.x, m.srcNode1.y, m.destNode1.x, m.destNode1.y);
		ChangePosition(m.srcNode2.x, m.srcNode2.y, m.destNode2.x, m.destNode2.y);
	}	

	public void PushTriple(TripleMove m) {
		ChangePosition(m.pushNode2.x, m.pushNode2.y, m.pushNode3.x, m.pushNode3.y);		
		ChangePosition(m.pushNode1.x, m.pushNode1.y, m.pushNode2.x, m.pushNode2.y);		
		ChangePosition(m.srcNode1.x, m.srcNode1.y, m.destNode1.x, m.destNode1.y);
		ChangePosition(m.srcNode2.x, m.srcNode2.y, m.destNode2.x, m.destNode2.y);
	}	
}
