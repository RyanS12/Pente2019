import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class PenteGR {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int gWidth = 19*38;
		int gHeight = 19*38;
		int sbWidth = (int)(gWidth*0.50);
		
		JFrame theGame  = new JFrame("Play Pente!!");
		
		
		
		theGame.setLayout(new BorderLayout());
		theGame.setResizable(false);
		
		
		
		theGame.setSize(gWidth + sbWidth, gHeight+19);
		theGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		PenteScore sb = new PenteScore(sbWidth, gHeight);
		sb.setPreferredSize(new Dimension(sbWidth, gHeight));
		
		
		
		PenteGB gb = new PenteGB(gWidth, gHeight, sb);
		gb.setPreferredSize(new Dimension(gWidth, gHeight));
		
		sb.setGameBoard(gb);
		
		theGame.add(gb, BorderLayout.CENTER);
		theGame.add(sb, BorderLayout.EAST);
		
		theGame.setVisible(true);
		
		gb.startNewGame(true);
		
		
	}

}