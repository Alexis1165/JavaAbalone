package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import helpers.Constants;
import models.DoubleMove;
import models.Graph;
import models.Node;
import models.TripleMove;

public class Game {	
	@SuppressWarnings("resource")
	public void Start(int player) {		
		Graph g = new Graph();				
		int white = 0;
		int black = 0;
		
		while(black < 6 && white < 6) {
			System.out.println("Player " + player + ":");
			System.out.println("===================================");											

			g.PrintNodes();

			Scanner input = new Scanner(System.in);  
			System.out.println("Enter the number of marbles you want to use:");
			int marbles = input.nextInt();  
			int x1 = 0, x2 = 0, x3 = 0;
			int y1 = 0, y2 = 0, y3 = 0;
			int move = 0;

			if (marbles < 1 || marbles > 3) {
				while(marbles < 1 || marbles > 3) {
					System.out.println("Please enter a number between 1 and 3:");
					marbles = input.nextInt();
				}
			}

			if (marbles == 1) {
				Node src = new Node();
				List<Node> m = new ArrayList<>();
				System.out.println("Enter x value of marble: ");
				x1 = input.nextInt();
				System.out.println("Enter y value of marble: ");
				y1 = input.nextInt();

				if (x1 < 0 || x1 > 8 || y1 < 0 || y1 > 8 || 
						g.nodes.get(x1).get(y1).innerVal != Constants.values[player]) {
					while(x1 < 0 || x1 > 8 || y1 < 0 || y1 > 8 ||
							g.nodes.get(x1).get(y1).innerVal != Constants.values[player]) {
						System.out.println("Invalid coordinates");
						System.out.println("Enter x value of marble: ");
						x1 = input.nextInt();
						System.out.println("Enter y value of marble: ");
						y1 = input.nextInt();				
					}
				}

				for (int i = 0 ; i < g.nodes.get(x1).size() ; ++i) {
					if (g.nodes.get(x1).get(i).x == x1 && 
							g.nodes.get(x1).get(i).y == y1) 
						src = g.nodes.get(x1).get(i);
				}
				m = g.GetSingleMove(Arrays.asList(src));
				g.PrintSinglePossibilities(m, src);
				System.out.println("");
				System.out.println("Enter the move# you want to execute: ");
				move = input.nextInt();

				g.ChangePosition(src.x, src.y, m.get(move - 1).x, m.get(move - 1).y);
			}

			else if (marbles == 2) {
				List<Node> src = new ArrayList<>();
				List<DoubleMove> m = new ArrayList<>();
				System.out.println("Enter x value of marble1: ");
				x1 = input.nextInt();
				System.out.println("Enter y value of marble1: ");
				y1 = input.nextInt();	
				System.out.println("Enter x value of marble2: ");
				x2 = input.nextInt();
				System.out.println("Enter y value of marble2: ");
				y2 = input.nextInt();	

				if (x1 < 0 || x1 > 8 || y1 < 0 || y1 > 8 || 
						x2 < 0 || x2 > 8 || y2 < 0 || y2 > 8 ||						
						g.nodes.get(x1).get(y1).innerVal != Constants.values[player] ||
						g.nodes.get(x2).get(y2).innerVal != Constants.values[player]) {				
					while (x1 < 0 || x1 > 8 || y1 < 0 || y1 > 8 || 
							x2 < 0 || x2 > 8 || y2 < 0 || y2 > 8 ||						
							g.nodes.get(x1).get(y1).innerVal != Constants.values[player]||
							g.nodes.get(x2).get(y2).innerVal != Constants.values[player]) {				

						System.out.println("Invalid coordinates");					
						System.out.println("Enter x value of marble1: ");
						x1 = input.nextInt();
						System.out.println("Enter y value of marble1: ");
						y1 = input.nextInt();	
						System.out.println("Enter x value of marble2: ");
						x2 = input.nextInt();
						System.out.println("Enter y value of marble2: ");
						y2 = input.nextInt();						
					}
				}

				else if(!isAdjacentDouble(x1, y1, x2, y2)) {
					System.out.println("Marbles are not adjacent");					
					System.out.println("Enter x value of marble1: ");
					x1 = input.nextInt();
					System.out.println("Enter y value of marble1: ");
					y1 = input.nextInt();	
					System.out.println("Enter x value of marble2: ");
					x2 = input.nextInt();
					System.out.println("Enter y value of marble2: ");
					y2 = input.nextInt();											
				}

				for (int i = 0 ; i < g.nodes.get(x1).size() ; ++i) {
					if (g.nodes.get(x1).get(i).x == x1 && 
							g.nodes.get(x1).get(i).y == y1) 
						src.add(g.nodes.get(x1).get(i));
				}

				for (int i = 0 ; i < g.nodes.get(x2).size() ; ++i) {
					if (g.nodes.get(x2).get(i).x == x2 && 
							g.nodes.get(x2).get(i).y == y2) 
						src.add(g.nodes.get(x2).get(i));
				}

				if (x1 == x2)
					m = g.GetDoubleMoveH(src, player == 1 ? 2 : 1);

				else m = g.GetDoubleMoveV(src, player == 1 ? 2 : 1);

				g.PrintDoublePossibilities(m);
				System.out.println("");
				System.out.println("Enter the move# you want to execute: ");
				move = input.nextInt();	

				if (m.get(move - 1).pushNode1 != null) {					
					g.ChangePosition(m.get(move - 1).pushNode1.x, 
							m.get(move - 1).pushNode1.y, 
							m.get(move - 1).pushNode2.x, 
							m.get(move - 1).pushNode2.y);					

					if (m.get(move - 1).elimination) {
						if (player == Constants.BLACK) ++black;
						if (player == Constants.WHITE) ++white;
						System.out.println("Marble eliminated!!");
					}
				}

				g.ChangePosition(m.get(move - 1).srcNode2.x, 
						m.get(move - 1).srcNode2.y, 
						m.get(move - 1).destNode2.x, 
						m.get(move - 1).destNode2.y);

				g.ChangePosition(m.get(move - 1).srcNode1.x, 
						m.get(move - 1).srcNode1.y, 
						m.get(move - 1).destNode1.x, 
						m.get(move - 1).destNode1.y);				
			}

			else if (marbles == 3) {
				List<Node> src = new ArrayList<>();
				List<TripleMove> m = new ArrayList<>();
				System.out.println("Enter x value of marble1: ");
				x1 = input.nextInt();
				System.out.println("Enter y value of marble1: ");
				y1 = input.nextInt();	
				System.out.println("Enter x value of marble2: ");
				x2 = input.nextInt();
				System.out.println("Enter y value of marble2: ");
				y2 = input.nextInt();				
				System.out.println("Enter x value of marble3: ");
				x3 = input.nextInt();
				System.out.println("Enter y value of marble3: ");
				y3 = input.nextInt();	

				if (x1 < 0 || x1 > 8 || y1 < 0 || y1 > 8 || 
						x2 < 0 || x2 > 8 || y2 < 0 || y2 > 8 ||						
						x3 < 0 || x3 > 8 || y3 < 0 || y3 > 8 ||						
						g.nodes.get(x1).get(y1).innerVal != Constants.values[player] ||
						g.nodes.get(x2).get(y2).innerVal != Constants.values[player] ||				
						g.nodes.get(x3).get(y3).innerVal != Constants.values[player]) {				
					while (x1 < 0 || x1 > 8 || y1 < 0 || y1 > 8 || 
							x2 < 0 || x2 > 8 || y2 < 0 || y2 > 8 ||						
							x3 < 0 || x3 > 8 || y3 < 0 || y3 > 8 ||						
							g.nodes.get(x1).get(y1).innerVal != Constants.values[player] ||
							g.nodes.get(x2).get(y2).innerVal != Constants.values[player] ||				
							g.nodes.get(x3).get(y3).innerVal != Constants.values[player]) {				

						System.out.println("Invalid coordinates");					
						System.out.println("Enter x value of marble1: ");
						x1 = input.nextInt();
						System.out.println("Enter y value of marble1: ");
						y1 = input.nextInt();	
						System.out.println("Enter x value of marble2: ");
						x2 = input.nextInt();
						System.out.println("Enter y value of marble2: ");
						y2 = input.nextInt();						
						System.out.println("Enter x value of marble3: ");
						x3 = input.nextInt();
						System.out.println("Enter y value of marble3: ");
						y3 = input.nextInt();	
					}
				}
				
				for (int i = 0 ; i < g.nodes.get(x1).size() ; ++i) {
					if (g.nodes.get(x1).get(i).x == x1 && 
							g.nodes.get(x1).get(i).y == y1) 
						src.add(g.nodes.get(x1).get(i));
				}

				for (int i = 0 ; i < g.nodes.get(x2).size() ; ++i) {
					if (g.nodes.get(x2).get(i).x == x2 && 
							g.nodes.get(x2).get(i).y == y2) 
						src.add(g.nodes.get(x2).get(i));
				}				
				
				for (int i = 0 ; i < g.nodes.get(x3).size() ; ++i) {
					if (g.nodes.get(x3).get(i).x == x3 && 
							g.nodes.get(x3).get(i).y == y3) 
						src.add(g.nodes.get(x3).get(i));
				}				
				
				if (x1 == x2 && x2 == x3)
					m = g.GetTripleMoveH(src, player == 1 ? 2 : 1);

				else m = g.GetTripleMoveV(src, player == 1 ? 2 : 1);
				
				g.PrintTriplePossibilities(m);
				System.out.println("");
				System.out.println("Enter the move# you want to execute: ");
				move = input.nextInt();	
				
				if (m.get(move - 1).pushNode2 != null) {
					g.ChangePosition(m.get(move - 1).pushNode2.x, 
							m.get(move - 1).pushNode2.y, 
							m.get(move - 1).pushNode3.x, 
							m.get(move - 1).pushNode3.y);					

					if (m.get(move - 1).elimination) {
						if (player == Constants.BLACK) ++black;
						if (player == Constants.WHITE) ++white;
						System.out.println("Marble eliminated!!");
					}
				}				
				
				else if (m.get(move - 1).pushNode1 != null) {
					g.ChangePosition(m.get(move - 1).pushNode1.x, 
							m.get(move - 1).pushNode1.y, 
							m.get(move - 1).pushNode2.x, 
							m.get(move - 1).pushNode2.y);					

					if (m.get(move - 1).elimination) {
						if (player == Constants.BLACK) ++black;
						if (player == Constants.WHITE) ++white;
						System.out.println("Marble eliminated!!");
					}
				}
				
				if (m.get(move - 1).destNode1.x < m.get(move - 1).srcNode1.x &&
						m.get(move - 1).destNode1.x < m.get(move - 1).srcNode2.x &&
						m.get(move - 1).destNode1.x < m.get(move - 1).srcNode3.x) {
					g.ChangePosition(m.get(move - 1).srcNode1.x, 
							m.get(move - 1).srcNode1.y, 
							m.get(move - 1).destNode1.x, 
							m.get(move - 1).destNode1.y);
					
					g.ChangePosition(m.get(move - 1).srcNode2.x, 
							m.get(move - 1).srcNode2.y, 
							m.get(move - 1).destNode2.x, 
							m.get(move - 1).destNode2.y);

					g.ChangePosition(m.get(move - 1).srcNode3.x, 
							m.get(move - 1).srcNode3.y, 
							m.get(move - 1).destNode3.x, 
							m.get(move - 1).destNode3.y);									
				}
				
				else {
					g.ChangePosition(m.get(move - 1).srcNode3.x, 
							m.get(move - 1).srcNode3.y, 
							m.get(move - 1).destNode3.x, 
							m.get(move - 1).destNode3.y);
					
					g.ChangePosition(m.get(move - 1).srcNode2.x, 
							m.get(move - 1).srcNode2.y, 
							m.get(move - 1).destNode2.x, 
							m.get(move - 1).destNode2.y);

					g.ChangePosition(m.get(move - 1).srcNode1.x, 
							m.get(move - 1).srcNode1.y, 
							m.get(move - 1).destNode1.x, 
							m.get(move - 1).destNode1.y);									
				}
				
			}

			if (player == Constants.BLACK) player = Constants.WHITE;
			else if (player == Constants.WHITE) player = Constants.BLACK;			
			
			System.out.println("===================================");											
			System.out.println("Score: Player 1 = " + black + " : Player 2 = " + white); 			
		}
		
		if (black > white)
			System.out.println("Player 1 is the winner");
		else if (black < white)
			System.out.println("Player 2 is the winner");
	}	

	public Boolean isAdjacentDouble(int x1, int y1, int x2, int y2) {
		Boolean adjacent = false;

		if (x1 == x2 && ((y1 - 1 == y2) || (y1 + 1 == y2))) {
			adjacent = true;
		} 

		else if(x1 == x2 && ((y2 - 1 == y1) || (y2 + 1 == y1))) {
			adjacent = true;			
		}

		else if (x1 + 1 == x2 && ((y1 - 1 == y2) || (y1 == y2))) {
			adjacent = true;
		} 

		else if (x2 + 1 == x1 && ((y2 - 1 == y1) || (y1 == y2))) {
			adjacent = true;
		} 


		else if (x1 - 1 == x2 && ((y1 - 1 == y2) || (y1 == y2))) {
			adjacent = true;
		}

		else if (x2 - 1 == x1 && ((y2 - 1 == y1) || (y1 == y2))) {
			adjacent = true;
		}

		return adjacent;
	}
}
