import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PenteGB extends JPanel implements MouseListener {
	
	public static final int EMPTY = 0;
	public static final int BLACKSTONE = -1;
	public static final int WHITESTONE = 1;
	public static final int NUM_SQUARES_SIDE = 19;
	public static final int INNER_START = 7;
	public static final int INNER_END = 11;
	public static final int PLAYER1_TURN = -1;
	public static final int PLAYER2_TURN = 1;
	public static final int MAX_CAPTURES = 5;
	public static final int SLEEP_TIME = 500;
	
	private int bWidth, bHeight;
	
	private PenteBoardSquare testSquare;
	private int squareW, squareH;
	
	//variables for playing games
	
	//P1 is dark stone
	
	private int playerTurn;
	
	private boolean player1IsComputer = false;
	private boolean player2IsComputer = false;
	
	private String p1Name, p2Name;
	
	private boolean darkStoneMove2Taken = false;
	
	private boolean gameOver = false;
	
	//Variables for Computer Game Player
	private ComputerMoveGenerator p1ComputerPlayer = null;
	private ComputerMoveGenerator p2ComputerPlayer = null;

	private PenteScore myScoreBoard;
	private PenteBoardSquare[][] gameBoard;
	private int p1Captures, p2Captures;
	
	public PenteGB(int w, int h, PenteScore sb) {
		
		bWidth = w;
		bHeight = h;
		myScoreBoard = sb;
		
		p1Captures = 0;
		p2Captures = 0;
		
		this.setSize(w,h);
		this.setBackground(Color.CYAN);
		
		squareW = bWidth/this.NUM_SQUARES_SIDE;
		squareH = bHeight/this.NUM_SQUARES_SIDE;
		
		//testSquare = new PenteBoardSquare(0,0,squareW, squareH);
		
		
		gameBoard = new PenteBoardSquare[NUM_SQUARES_SIDE][NUM_SQUARES_SIDE];
		
		for(int row = 0; row < NUM_SQUARES_SIDE; row++) {
			
			for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
				
				gameBoard[row][col] = new PenteBoardSquare(col * squareW, row * squareH ,squareW ,  squareH);
				
				if(col >= INNER_START && col <= INNER_END && row >= INNER_START && row <= INNER_END) {
					
					gameBoard[row][col].setInner();
					
				}
				
			}
			
		}
		
		initialDisplay();
		
		repaint();
		
		addMouseListener(this);
		this.setFocusable(true);
		
		
		
		
		
	}
	
	
	//method to do drawing...
	
	public void paintComponent(Graphics g) {
			
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, bWidth, bHeight);
		
		for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
			
			for(int row = 0; row < NUM_SQUARES_SIDE; row++) {
				
				gameBoard[row][col].drawMe(g);
				
				
				
			}
			
		}
		
		
			
	}
	
	
	
	public void resetBoard() {
		
		for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
			
			for(int row = 0; row < NUM_SQUARES_SIDE; row++) {
				

				gameBoard[row][col].setState(EMPTY);
				gameBoard[row][col].setWinningSquare(false);
				
				
				//this.paintImmediately(0, 0, bWidth, bHeight);
				
				
			}
			//this.paintImmediately(0, 0, bWidth, bHeight);
			
		}
		repaint();
		//this.paintImmediately(0,0,bWidth,bHeight); // ok
		
		
	}
	
	
	public void startNewGame(boolean firstGame) {
		
		p1Captures = 0;
		p2Captures = 0;
		
		gameOver = false;
		
		//resetBoard();
		
		if(firstGame) {
			p1Name = JOptionPane.showInputDialog("Name p1 or type 'c' for computer");
		

			if(p1Name.toLowerCase().equals("c") || p1Name.toLowerCase().equals("computer") || p1Name.toLowerCase().equals("comp")) {
		
				player1IsComputer = true;
				p1ComputerPlayer = new ComputerMoveGenerator(this, BLACKSTONE);
				
			}
			
		}
		
		this.myScoreBoard.setName(p1Name, BLACKSTONE);
		this.myScoreBoard.setCaptures(p1Captures, BLACKSTONE);
		
		if(firstGame) {
			p2Name = JOptionPane.showInputDialog("Name p2 or type 'c' for computer");
		
			if(p2Name.toLowerCase().equals("c") || p2Name.toLowerCase().equals("computer") || p2Name.toLowerCase().equals("comp")) {
			
				player2IsComputer = true;
				p2ComputerPlayer = new ComputerMoveGenerator(this, WHITESTONE);

			}
		}
		
		
		this.myScoreBoard.setName(p2Name, WHITESTONE);
		this.myScoreBoard.setCaptures(p2Captures, WHITESTONE);


		
		resetBoard();
		
		
		
		playerTurn = this.PLAYER1_TURN;
		
		
		this.gameBoard[this.NUM_SQUARES_SIDE/2][NUM_SQUARES_SIDE/2].setState(BLACKSTONE);
		
		darkStoneMove2Taken = false;
		
		changePlayerTurn();
		checkForComputerMove(playerTurn);
		
		this.repaint();
		
		
		
		
	}
	
	
	
	public void changePlayerTurn() {
		
		playerTurn *= -1;
		
		System.out.println(playerTurn);
		
		myScoreBoard.setPlayerTurn(playerTurn);
		
	}
	
	
	
	public boolean fiveInARow(int whichPlayer) {
		
		boolean isFive = false;
		
		
		//every square on the board
		
		for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
			
			for(int row = 0; row < NUM_SQUARES_SIDE; row++) {
				
				//all 8 directions
				
				for(int rL = -1; rL <= 1; rL++) {
					
					for(int uD = -1; uD<=1; uD++) {
						
						if(fiveCheck(row, col, whichPlayer, uD, rL)) {
							isFive = true;
						}
								
					}
				}
				
				
				
			}
			
		}
		
		return isFive;
		
		
	}
	
	
	public boolean fiveCheck(int r, int c, int pt, int upDown, int rightLeft) {
		
		try {
			
			boolean five = false;
			
			if(upDown != 0 || rightLeft != 0) {
				
				if(gameBoard[r + upDown][c+rightLeft].getState() == pt && 
						gameBoard[r  + upDown * 2][c+ (rightLeft*2)].getState() == pt && 
						gameBoard[r + upDown * 3][c + 3 * rightLeft].getState() == pt &&
						gameBoard[r + upDown * 4][c + 4 * rightLeft].getState() == pt &&
						gameBoard[r + upDown * 5][c + 5 * rightLeft].getState() == pt ) {
							
					five = true;
					
					gameBoard[r + upDown][c+rightLeft].setWinningSquare(true);
					gameBoard[r  + upDown * 2][c+ (rightLeft*2)].setWinningSquare(true);
					gameBoard[r + upDown * 3][c + 3 * rightLeft].setWinningSquare(true);
					gameBoard[r + upDown * 4][c + 4 * rightLeft].setWinningSquare(true);
					gameBoard[r + upDown * 5][c + 5 * rightLeft].setWinningSquare(true);
			
				}
				
			}
			
			
			
			return five;
			
		} catch(ArrayIndexOutOfBoundsException e) {
			
			
			System.out.println(e.toString());			
			
			return false;
			
			
		}
		
	}
	
	
	
	public void checkForWin(int whichPlayer) {
		
		
		if(whichPlayer == PLAYER1_TURN) {
			
			if(p1Captures >= MAX_CAPTURES) { // 5 = MAX_CAPTURES
				//win
				
				JOptionPane.showMessageDialog(null, "GG!!!! " + MAX_CAPTURES + " captures accomplished by " + p1Name + "!!");
				gameOver = true;
				
				
			} else if(fiveInARow(PLAYER1_TURN)) {
				
				JOptionPane.showMessageDialog(null, "GG!!!! Congrats " + p1Name + ".");
				
				gameOver = true;
				
			}
				
		} else {
			
			if(p2Captures >= 5) {
				//win
				
				JOptionPane.showMessageDialog(null, "GG!!!! " + MAX_CAPTURES + " captures accomplished by " + p2Name + "!!");
				gameOver = true;
				
				
			} else if(fiveInARow(PLAYER2_TURN)) {
				
				JOptionPane.showMessageDialog(null, "GG!!!! Congrats " + p2Name + ".");
				
				gameOver = true;
				
			}
			
			
		}
		
		
		
	}



	public void checkClick(int clickX, int clickY) {
		
		if(!gameOver) {
		
			for(int row = 0; row < NUM_SQUARES_SIDE; row++) {
				
				for(int col = 0; col < NUM_SQUARES_SIDE; col++) { 
					
					
					boolean squareClicked = gameBoard[row][col].isClicked(clickX, clickY);
					
					if(squareClicked) {
						
						System.out.println("You clicked the square at[" + row + ", " + col + "]");
						
						if(gameBoard[row][col].getState()==EMPTY) {
							
							if(!darkSquareProblem(row, col)) {
							
								gameBoard[row][col].setState(playerTurn);
								
								checkForAllCaptures(row, col, playerTurn);
								
								this.repaint();
								
								this.paintImmediately(0, 0, bWidth, bHeight);
								
								checkForWin(playerTurn);
								
								this.changePlayerTurn();
								
								checkForComputerMove(playerTurn);
								
								
								
								
								
								/*
								if(playerTurn == this.PLAYER1_TURN && this.player1IsComputer) {
									
								}*/
								
							} else {
								JOptionPane.showMessageDialog(null, "Second dark stone move has to be outside of the light box");
	
							}
							
							
						} else {
							
							JOptionPane.showMessageDialog(null, "This square is taken, click on another square");
						
						}	
					}
				}
				
			}
		}
		
	}
	
	
	public void checkForComputerMove(int whichPlayer) {
		
		
		
		if(whichPlayer == this.PLAYER1_TURN && this.player1IsComputer) {
			
			int[] nextMove = this.p1ComputerPlayer.getComputerMove();
			
			int newR = nextMove[0];
			int newC = nextMove[1];
			
			
			
			gameBoard[newR][newC].setState(playerTurn);
			
			//this.paintImmediately(0, 0, bWidth, bHeight);
			
			this.repaint();
			
			checkForAllCaptures(newR, newC, playerTurn);
			
			this.repaint();
			
			checkForWin(playerTurn);
			
			if(!gameOver) {
				
				this.changePlayerTurn();
				
				checkForComputerMove(playerTurn);
			}
			
			
			
		} else if(whichPlayer == this.PLAYER2_TURN && this.player2IsComputer) {
			
			int[] nextMove = this.p2ComputerPlayer.getComputerMove();
			
			int newR = nextMove[0];
			int newC = nextMove[1];
			
			gameBoard[newR][newC].setState(playerTurn);
			
			this.paintImmediately(0, 0, bWidth, bHeight);
			
			checkForAllCaptures(newR, newC, playerTurn);
			
			this.repaint();
			
			checkForWin(playerTurn);
			
			if(!gameOver) {
				this.changePlayerTurn();
				
				checkForComputerMove(playerTurn);
			}
			
		}
		
		this.repaint();
		
		
	}
	
	public boolean darkSquareProblem(int r, int c) {
		
		boolean dsp = false;
		
		if(darkStoneMove2Taken == false &&
				playerTurn == BLACKSTONE) {
			
		
			if(r >= INNER_START && r <= INNER_END &&
				c >= INNER_START && c <= INNER_END) {
			
				dsp = true;

			} else {
				darkStoneMove2Taken = true;
			}
			
		}
		
		
		
		
		return dsp;
		
	}

	public void checkForAllCaptures(int r, int c, int pt) {
		
		//System.out.println("capt");
		
		for(int i = -1; i <= 1; i++) {
			
			for(int j = -1; j<=1; j++) {
				
				checkForCaptures(r,c,pt, i, j);
						
			}
		}
			
	}
	
	public void checkForCaptures(int r, int c, int pt, int upDown, int rightLeft) {
		
		try {
		
			boolean cap = false;
			
			if(gameBoard[r + upDown][c+rightLeft].getState() == pt * -1 && 
					gameBoard[r  + upDown * 2][c+ (rightLeft*2)].getState() == pt * -1 && 
					gameBoard[r + upDown * 3][c + 3 * rightLeft].getState() == pt) {
						
				cap = true;
						//System.out.println("rc");
				gameBoard[r + upDown][c+rightLeft].setState(EMPTY);
				gameBoard[r + upDown * 2][c+rightLeft * 2].setState(EMPTY);
						
				if(pt == PLAYER1_TURN) {
							
					p1Captures ++;
					myScoreBoard.setCaptures(p1Captures, playerTurn);
							
							
				} else {
							
					p2Captures++;
					myScoreBoard.setCaptures(p2Captures, playerTurn);
				}	
		
			}
			
			return;
			
		} catch(ArrayIndexOutOfBoundsException e) {
			
			
			System.out.println(e.toString());			
			
			return ;
			
			
		}
		
	}

	
	
	
	

	




	
	
	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		//System.out.println("Mouse clicked");
		//System.out.println("Clicked at " + e.getX() + ", " + e.getY());
		
		
		checkClick(e.getX(), e.getY());
		
		
		
	}










	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	public void initialDisplay() {
		
		
		this.gameBoard[3][4].setState(BLACKSTONE);
		this.gameBoard[3][5].setState(BLACKSTONE);
		this.gameBoard[4][5].setState(BLACKSTONE);
		this.gameBoard[5][5].setState(BLACKSTONE);
		this.gameBoard[5][4].setState(BLACKSTONE);
		this.gameBoard[5][3].setState(BLACKSTONE);
		this.gameBoard[4][3].setState(BLACKSTONE);
		this.gameBoard[3][3].setState(BLACKSTONE);
		
		this.gameBoard[5][13].setState(WHITESTONE);
		this.gameBoard[5][14].setState(WHITESTONE);
		this.gameBoard[5][15].setState(WHITESTONE);
		this.gameBoard[4][15].setState(WHITESTONE);
		this.gameBoard[3][15].setState(WHITESTONE);
		this.gameBoard[3][14].setState(WHITESTONE);
		this.gameBoard[3][13].setState(WHITESTONE);
		this.gameBoard[4][13].setState(WHITESTONE);
		
		this.gameBoard[7][8].setState(BLACKSTONE);
		this.gameBoard[8][8].setState(WHITESTONE);
		this.gameBoard[9][8].setState(BLACKSTONE);
		this.gameBoard[10][8].setState(WHITESTONE);
		this.gameBoard[11][8].setState(BLACKSTONE);
		this.gameBoard[7][9].setState(WHITESTONE);
		this.gameBoard[7][10].setState(BLACKSTONE);
		this.gameBoard[8][10].setState(WHITESTONE);
		this.gameBoard[9][10].setState(BLACKSTONE);
		
		this.gameBoard[12][5].setState(WHITESTONE);
		this.gameBoard[13][6].setState(BLACKSTONE);
		this.gameBoard[14][7].setState(WHITESTONE);
		this.gameBoard[14][8].setState(BLACKSTONE);
		this.gameBoard[14][9].setState(WHITESTONE);
		this.gameBoard[14][10].setState(BLACKSTONE);
		this.gameBoard[13][12].setState(WHITESTONE);
		this.gameBoard[12][13].setState(BLACKSTONE);
		this.gameBoard[this.NUM_SQUARES_SIDE/2][NUM_SQUARES_SIDE/2].setState(WHITESTONE);
			
			
			
		
	}
	
	
	public PenteBoardSquare[][] getBoard() {
		return gameBoard;
	}
	
	
	
	
	
}
