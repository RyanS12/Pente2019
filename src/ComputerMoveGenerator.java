import java.util.ArrayList;
import java.util.Comparator;

public class ComputerMoveGenerator {
	
	public static final int OFFENSE= 1;
	public static final int DEFENSE= -1;
	public static final int ONE_IN_ROW_DEF= 1;
	public static final int TWO_IN_ROW_DEF= 2;
	public static final int TWO_IN_ROW_CAP= 4;
	public static final int THREE_IN_ROW_DEF= 2;

	
	PenteGB myGame;
	
	int myStone;
	
	ArrayList<CMObject> oMoves = new ArrayList<CMObject>();
	ArrayList<CMObject> dMoves = new ArrayList<CMObject>();

	
	public ComputerMoveGenerator(PenteGB gb, int stoneColor) {
		
		myStone = stoneColor;
		myGame = gb;
		
		System.out.println("computer is playing with " + myStone);
		
	}
	
	public void sortDefPriorities() {
		
		Comparator<CMObject> compareByPriority = (CMObject o1, CMObject o2) ->
		o1.getPriority().compareTo(o2.getPriority());
		
	}
	
	public int[] getComputerMove() {
	
		int[] newMove = new int[2];
		
		newMove[0] = -1;
		newMove[1] = -1;
		dMoves.clear();
		oMoves.clear();
		
		findDefMoves();
		findOffMoves();
	
		if(dMoves.size() > 0) {
			
			int whichOne = (int)(Math.random() * dMoves.size());
			CMObject ourMove = dMoves.get(whichOne);
			
			newMove[0] = ourMove.getRow();
			newMove[1] = ourMove.getCol();
			
			
		} else {
			
			newMove = generateRandomMove();

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
					
				}
				
				
			}
		}
	}
	
	public void findOneDef(int r, int c) {
		for(int rL = -1; rL <= 1; rL++) {
			
			for(int uD = -1; uD<=1; uD++) {
				
				try {
					
					if(myGame.getBoard()[r + rL][c + uD].getState() == PenteGB.EMPTY) {
						
						setDefMove(r + rL, c + uD, ONE_IN_ROW_DEF);
					
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
	
	public void setDefMove(int r, int c, int p) {
		
		
		CMObject newMove = new CMObject();
		newMove.setRow(r);
		newMove.setCol(c);
		newMove.setPriority(p);
		newMove.setMoveType(DEFENSE);
		dMoves.add(newMove);
		
	}
	
	public void findTwoDef(int r, int c) {
		for(int rL = -1; rL <= 1; rL++) {
			
			for(int uD = -1; uD<=1; uD++) {
				
				try {
					
					if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 &&
							myGame.getBoard()[r + rL*2][c + uD*2].getState() == PenteGB.EMPTY) {
						
						if(isOnBoard(r-rL, c - uD) == false) {
							
							
							
						}
						
						setDefMove(r + rL*2, c + uD*2, TWO_IN_ROW_DEF);
						
					} else if (myGame.getBoard()[r + rL][c+uD].getState() == myStone * -1) {
						setDefMove(r+rL, c+uD, TWO_IN_ROW_DEF);
						
					}
					
				} catch(ArrayIndexOutOfBoundsException e) {
					//System.out.println("Off the board in findTwoDef at [" + r + ", " + c + "]");
					return;
				}
				
			}
		}
		
	}
	
	public void findOffMoves() {
		
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