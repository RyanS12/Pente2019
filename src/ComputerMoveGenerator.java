import java.util.ArrayList;

public class ComputerMoveGenerator {
	
	public static final int OFFENSE= 1;
	public static final int DEFENSE= -1;

	
	PenteGB myGame;
	
	int myStone;
	
	ArrayList<CMObject> oMoves = new ArrayList<CMObject>();

	ArrayList<CMObject> dMoves = new ArrayList<CMObject>();

	
	public ComputerMoveGenerator(PenteGB gb, int stoneColor) {
		
		myStone = stoneColor;
		myGame = gb;
		
		System.out.println("computer is playing with " + myStone);
		
	}
	
	public int[] getComputerMove() {
	
		int[] newMove;
		
		findDefMoves();
		findOffMoves();
	
		
		
		
		newMove = generateRandomMove();
		
		
		
		try {
			sleepForAMove();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return newMove;
		
	}
	
	
	public void findOffMoves() {
		findOneDef();
		findTwoDef();
		findThreeDef();
		findFourDef();
	}
	
	public void findOneDef() {
		
	}
	
	public void findDefMoves() {
		
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