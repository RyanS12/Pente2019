import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JOptionPane;

public class ComputerMoveGenerator {
	
	public static final int OFFENSE= 1;
	public static final int DEFENSE= -1;
	public static final int ONE_IN_ROW_DEF = 2;
	public static final int TWO_IN_ROW_DEF = 5;
	
	public static final int ONE_IN_ROW_OFF = 1;
	public static final int TWO_IN_ROW_OFF = 4;
	
	public static final int TWO_IN_ROW_CAP = 10;
	public static final int TWO_IN_ROW_DEF_CAP = 0;

	
	public static final int THREE_IN_ROW_DEF = 15;
	public static final int FOUR_IN_ROW_DEF = 100;
	
	
	public static final int THREE_IN_ROW_OFF = 13;
	public static final int FOUR_IN_ROW_OFF = 70;
	

	
	PenteGB myGame;
	
	int myStone;
	
	
	ArrayList<CMObject> Moves = new ArrayList<CMObject>();

	
	public ComputerMoveGenerator(PenteGB gb, int stoneColor) {
		
		myStone = stoneColor;
		myGame = gb;
		
		System.out.println("computer is playing with " + myStone);
		
	}
	
	
	public void printPriorities() {
		for(CMObject m: Moves) {
			System.out.println(m);
		}
	}
	
	public void sortPriorities() {
		
		//Comparator<CMObject> compareByPriority = (CMObject o1, CMObject o2) ->
		//o1.getPriorityInt().compareTo(o2.getPriorityInt());
		
		Collections.sort(Moves);
		printPriorities();
		
	}
	
	
	public int[] getComputerMove() {
	
		int[] newMove = new int[2];
		
		newMove[0] = -1;
		newMove[1] = -1;
		Moves.clear();
		
		findDefMoves();
		findOffMoves();
		sortPriorities();
		
		
	
		if(Moves.size() > 0) {
			
			//System.out.println("First Def Move: " + Moves.get(0));
			//System.out.println("Last Def Move: " + Moves.get(Moves.size() - 1));
			
			//int whichOne = (int)(Math.random() * dMoves.size());
			CMObject ourMove = Moves.get(0);
			
			if(Moves.get(0).getPriority() <= this.ONE_IN_ROW_DEF) {
				ourMove= Moves.get((int)(Math.random()*Moves.size()));
			} else {
				ourMove = Moves.get(0);
			}
			
			newMove[0] = ourMove.getRow();
			newMove[1] = ourMove.getCol();
			
			if(myGame.darkSquareProblem(newMove[0], newMove[1]) == true) {
				System.out.println("??");
			}
			
			
		} else {
			
			if(myStone == PenteGB.BLACKSTONE && myGame.getDarkStoneMove2Taken() == false) {
				int newBStoneProbRow = -1;
				int newBStoneProbCol = -1;
				int innerSafeSquareSideLen = PenteGB.INNER_END - PenteGB.INNER_START + 1;
				
				while(myGame.getDarkStoneMove2Taken() == false) {
					newBStoneProbRow = (int)(Math.random() * (innerSafeSquareSideLen + 2))
							+ (innerSafeSquareSideLen + 1);
					newBStoneProbCol = (int)(Math.random() * (innerSafeSquareSideLen + 2))
							+ (innerSafeSquareSideLen + 1);
					
					myGame.darkSquareProblem(newBStoneProbRow, newBStoneProbCol);
				}
				
				newMove[0] = newBStoneProbRow;
				newMove[1] = newBStoneProbCol;
				
				
			} else {
				
				
				
			}

		}
		
		
		//newMove = generateRandomMove();
		
		
		
		try {
			sleepForAMove();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("In GetComputerMove I have a move at [" +newMove[0]+ "," + newMove[1] + "]");
		
		return newMove;
		
	}
	
	
	
	
	public void findDefMoves() {
		for(int row = 0; row < PenteGB.NUM_SQUARES_SIDE; row++) {
			
			for(int col = 0; col < PenteGB.NUM_SQUARES_SIDE; col++) {
				
				if(myGame.getBoard()[row][col].getState() == myStone * -1) {
					
					findOneDef(row, col);
					findTwoDef(row, col);
					findThreeDef(row, col);
					findFourDef(row,col);
					findExceptionDef(row,col);
				}
				
				
			}
		}
	}
	
	
	public void findOffMoves() {
		
		for(int row = 0; row < PenteGB.NUM_SQUARES_SIDE; row++) {
			
			for(int col = 0; col < PenteGB.NUM_SQUARES_SIDE; col++) {
				
				if(myGame.getBoard()[row][col].getState() == myStone) {
					
					findOneOff(row, col);
					findTwoOff(row, col);
					findThreeOff(row, col);
					findFourOff(row,col);
				}
				
				
			}
		}
		
		
		
	}
	
	
	public void findOneOff(int r, int c) {
		
		for(int rL = -1; rL <= 1; rL++) {
			
			for(int uD = -1; uD<=1; uD++) {
				
				try {
					
					if(myGame.getBoard()[r + rL][c + uD].getState() == PenteGB.EMPTY) {
						
						if(myGame.getBoard()[r - rL][c - uD].getState() == myStone * -1) {
							
							setMove(r + rL, c + uD, TWO_IN_ROW_DEF_CAP, OFFENSE);
							
						} else {
						
							setMove(r + rL, c + uD, ONE_IN_ROW_OFF, OFFENSE);
						}
					
					}
					
				} catch(ArrayIndexOutOfBoundsException e) {
					//System.out.println("Off the board in findOneDef at [" + r + ", " + c + "]");
					return;
				}
				
			}
		}
		
		
		
		
		
	}
	
	
	public void findOneDef(int r, int c) {
		for(int rL = -1; rL <= 1; rL++) {
			
			for(int uD = -1; uD<=1; uD++) {
				
				try {
					
					if(myGame.getBoard()[r + rL][c + uD].getState() == PenteGB.EMPTY) {
						
						
						setMove(r + rL, c + uD, ONE_IN_ROW_DEF, DEFENSE);
					
					}
					
					
					
				} catch(ArrayIndexOutOfBoundsException e) {
					//System.out.println("Off the board in findOneDef at [" + r + ", " + c + "]");
					return;
				}
				
			}
		}
		
	}
	
	
	
	public boolean isOnBoard(int r, int c) {
		boolean isOn = false;
		if(r >= 0 && r < PenteGB.NUM_SQUARES_SIDE) {
			if(c >= 0 && c < PenteGB.NUM_SQUARES_SIDE) {
				isOn = true;
			}
		}
		return isOn;
	}
	
	public void setMove(int r, int c, int p, int type) {
		
		if(myStone == PenteGB.BLACKSTONE && myGame.getDarkStoneMove2Taken() == false) {
			
			if(myGame.darkSquareProblemComputerMoveList(r, c) == false) {
				
				CMObject newMove = new CMObject();
				newMove.setRow(r);
				newMove.setCol(c);
				newMove.setPriority(p);
				newMove.setMoveType(type);
				Moves.add(newMove);
				
			}
			
		} else {
			
			CMObject newMove = new CMObject();
			newMove.setRow(r);
			newMove.setCol(c);
			newMove.setPriority(p);
			newMove.setMoveType(type);
			Moves.add(newMove);
			
			
		}
			
		
		
		
	}
	
	
	
	public void findTwoDef(int r, int c) {
		
		for(int rL = -1; rL <= 1; rL++) {
			
			for(int uD = -1; uD<=1; uD++) {
				
				try {
					
					if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 &&
							myGame.getBoard()[r + rL*2][c + uD*2].getState() == PenteGB.EMPTY) {
						
						if(isOnBoard(r-rL, c - uD) == false) {
							
							setMove(r + rL * 2, c + uD*2, TWO_IN_ROW_DEF - 1, DEFENSE);
							
						} else if (myGame.getBoard()[r - rL][c - uD].getState() == myStone * -1) {
							
							setMove(r+rL * 2, c+uD * 2, TWO_IN_ROW_DEF, DEFENSE);
							
						} else if (myGame.getBoard()[r-rL][c-uD].getState() == myStone) {
							
							setMove(r+rL * 2, c+uD * 2, TWO_IN_ROW_CAP, DEFENSE);
							
						}
					
						
					}
					
				} catch(ArrayIndexOutOfBoundsException e) {
					//System.out.println("Off the board in findTwoDef at [" + r + ", " + c + "]");
					//return;
				}
				
			}
		}
		
	}
	
	public void findTwoOff(int r, int c) {
		
		for(int rL = -1; rL <= 1; rL++) {
			
			for(int uD = -1; uD<=1; uD++) {
				
				try {
					
					if(myGame.getBoard()[r + rL][c + uD].getState() == myStone &&
							myGame.getBoard()[r + rL*2][c + uD*2].getState() == PenteGB.EMPTY) {
						
						if(isOnBoard(r-rL, c - uD) == false) {
							
							setMove(r + rL * 2, c + uD*2, TWO_IN_ROW_OFF-1, OFFENSE);
							
						} else if (myGame.getBoard()[r - rL][c - uD].getState() == myStone * -1) {
							
							setMove(r+rL * 2, c+uD * 2, TWO_IN_ROW_OFF - 1, OFFENSE);
							
						} else if (myGame.getBoard()[r-rL][c-uD].getState() == PenteGB.EMPTY) {
							
							setMove(r+rL * 2, c+uD * 2, TWO_IN_ROW_OFF, OFFENSE);
							
						}
					
						
					}
					
				} catch(ArrayIndexOutOfBoundsException e) {
					//System.out.println("Off the board in findTwoDef at [" + r + ", " + c + "]");
					//return;
				}
				
			}
		}
		
	}
	
	public void findThreeDef(int r, int c) {
	
		
		for(int rL = -1; rL <= 1; rL++) {
			
			for(int uD = -1; uD<=1; uD++) {
				
				try {
					
					if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 &&
							myGame.getBoard()[r + rL*2][c + uD*2].getState() == myStone * -1 &&
							myGame.getBoard()[r + rL*3][c + uD*3].getState() == PenteGB.EMPTY) {
						
						if(isOnBoard(r-rL, c - uD) == false) {
							
							setMove(r + rL*3, c + uD*3, THREE_IN_ROW_DEF, DEFENSE);
							
						} else if (myGame.getBoard()[r - rL][c - uD].getState() == myStone * -1) {
							
							setMove(r + rL * 3, c + uD * 3, THREE_IN_ROW_DEF, DEFENSE);
							
						} else if (myGame.getBoard()[r - rL][c - uD].getState() == PenteGB.EMPTY) {
							
							setMove(r + rL * 3, c + uD * 3, FOUR_IN_ROW_DEF - 1, DEFENSE);
							
						}
					
						
					}
					
				} catch(ArrayIndexOutOfBoundsException e) {
					
					//System.out.println("Off the board in findTwoDef at [" + r + ", " + c + "]");
					//return;
					
				}
				
				
			}
			
		}
		
	}
	
	public void findExceptionDef(int r, int c) {
		
		
		for(int rL = -1; rL <= 1; rL++) {
			
			for(int uD = -1; uD<=1; uD++) {
				
				try {
					
					if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 &&
							myGame.getBoard()[r + rL*2][c + uD*2].getState() == PenteGB.EMPTY &&
							myGame.getBoard()[r + rL*3][c + uD*3].getState() == myStone * -1) {
						
						if(isOnBoard(r-rL, c - uD) == false) {
							
							setMove(r + rL*2, c + uD*2, THREE_IN_ROW_DEF, DEFENSE);
							
						} else if (myGame.getBoard()[r - rL][c - uD].getState() == myStone) {
							
							setMove(r + rL * 2, c + uD * 2, THREE_IN_ROW_DEF, DEFENSE);
							
						} else if (myGame.getBoard()[r - rL][c - uD].getState() == PenteGB.EMPTY) {
							
							setMove(r + rL * 2, c + uD * 2, FOUR_IN_ROW_DEF - 1, DEFENSE);
							
						}
					
						
					}
					
					
					if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 &&
							myGame.getBoard()[r + rL*2][c + uD*2].getState() == myStone * -1 &&
							myGame.getBoard()[r + rL*3][c + uD*3].getState() == PenteGB.EMPTY &&
									myGame.getBoard()[r + rL*4][c + uD*4].getState() == myStone * -1) {
						
						setMove(r + rL * 3, c + uD * 3, FOUR_IN_ROW_DEF, DEFENSE);
						
						
						
					}
					
					if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 &&
							myGame.getBoard()[r + rL*2][c + uD*2].getState() == PenteGB.EMPTY &&
							myGame.getBoard()[r + rL*3][c + uD*3].getState() == myStone * -1 &&
									myGame.getBoard()[r + rL*4][c + uD*4].getState() == myStone * -1) {
						
						setMove(r + rL * 2, c + uD * 2, FOUR_IN_ROW_DEF, DEFENSE);
						
						
						
					}
					
				} catch(ArrayIndexOutOfBoundsException e) {
					
					//System.out.println("Off the board in findTwoDef at [" + r + ", " + c + "]");
					//return;
					
				}
				
				
			}
			
		}
		
		
	}
	
	public void findThreeOff(int r, int c) {
	
		
		for(int rL = -1; rL <= 1; rL++) {
			
			for(int uD = -1; uD<=1; uD++) {
				
				try {
					
					if(myGame.getBoard()[r + rL][c + uD].getState() == myStone &&
							myGame.getBoard()[r + rL*2][c + uD*2].getState() == myStone  &&
							myGame.getBoard()[r + rL*3][c + uD*3].getState() == PenteGB.EMPTY) {
						
						if(isOnBoard(r-rL, c - uD) == false) {
							
							setMove(r + rL*3, c + uD*3, THREE_IN_ROW_OFF, OFFENSE);
							
						} else if (myGame.getBoard()[r - rL][c - uD].getState() == myStone) {
							
							setMove(r + rL * 3, c + uD * 3, THREE_IN_ROW_OFF, OFFENSE);
							
						} else if (myGame.getBoard()[r - rL][c - uD].getState() == PenteGB.EMPTY) {
							
							setMove(r + rL * 3, c + uD * 3, THREE_IN_ROW_OFF + 5, OFFENSE);
							
						}
					
						
					}
					
				} catch(ArrayIndexOutOfBoundsException e) {
					
					//System.out.println("Off the board in findTwoDef at [" + r + ", " + c + "]");
					//return;
					
				}
				
				
			}
			
		}
		
	}
	
	
	public void findFourDef(int r, int c) {
		
		for(int rL = -1; rL <= 1; rL++) {
			
			for(int uD = -1; uD<=1; uD++) {
				
				try {
					
					if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 &&
							myGame.getBoard()[r + rL*2][c + uD*2].getState() == myStone * -1 &&
							myGame.getBoard()[r + rL*3][c + uD*3].getState() == myStone * -1 &&
							myGame.getBoard()[r + rL*4][c + uD*4].getState() == PenteGB.EMPTY) {
						
						
						if(isOnBoard(r-rL, c - uD) == false) {
							
							setMove(r + rL*4, c + uD*4, FOUR_IN_ROW_DEF, DEFENSE);
							
						} else if (myGame.getBoard()[r - rL][c - uD].getState() == myStone) {
							
							setMove(r + rL * 4, c + uD * 4, FOUR_IN_ROW_DEF, DEFENSE);
							
						}
							
						
					
						
					}
					
				} catch(ArrayIndexOutOfBoundsException e) {
					
					//System.out.println("Off the board in findTwoDef at [" + r + ", " + c + "]");
					//return;
					
				}
				
				
			}
			
		}
		
	}
	
	public void findFourOff(int r, int c) {
		
		for(int rL = -1; rL <= 1; rL++) {
			
			for(int uD = -1; uD<=1; uD++) {
				
				try {
					
					if(myGame.getBoard()[r + rL][c + uD].getState() == myStone &&
							myGame.getBoard()[r + rL*2][c + uD*2].getState() == myStone  &&
							myGame.getBoard()[r + rL*3][c + uD*3].getState() == myStone &&
							myGame.getBoard()[r + rL*4][c + uD*4].getState() == PenteGB.EMPTY) {
						
						
						if(isOnBoard(r-rL, c - uD) == false) {
							
							setMove(r + rL*4, c + uD*4, FOUR_IN_ROW_OFF, OFFENSE);
							
						} else if (myGame.getBoard()[r - rL][c - uD].getState() == myStone) {
							
							setMove(r + rL * 4, c + uD * 4, FOUR_IN_ROW_OFF, OFFENSE);
							
						} else if (myGame.getBoard()[r - rL][c - uD].getState() == PenteGB.EMPTY) {
							
							setMove(r + rL * 4, c + uD * 4, FOUR_IN_ROW_OFF, OFFENSE);
							
						}
					
						
					}
					
				} catch(ArrayIndexOutOfBoundsException e) {
					
					//System.out.println("Off the board in findTwoDef at [" + r + ", " + c + "]");
					//return;
					
				}
				
				
			}
			
		}
		
	}
	
	
	public int[] generateRandomMove() {
		
		int[] move = new int[2];
		
		boolean done = false;
		
		int newR, newC;
		
		do {
			newR = (int)(Math.random() * PenteGB.NUM_SQUARES_SIDE);
			newC = (int)(Math.random() * PenteGB.NUM_SQUARES_SIDE);
			
			
			if(myGame.getBoard()[newR][newC].getState() == PenteGB.EMPTY) {
				done = true;
				move[0] = newR;
				move[1] = newC;
			}
			
			
			
		} while(!done); 
		
		return move;
			
		
		
		
		
	}
	
	public void sleepForAMove() throws InterruptedException {
		
		Thread currThread = Thread.currentThread();
		Thread.sleep(PenteGB.SLEEP_TIME);
		
	}

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}