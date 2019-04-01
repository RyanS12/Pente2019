import javax.swing.JFrame;

public class PenteGR {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int gWidth = 19*40;
		int gHeight = 19*40;
		
		JFrame theGame  = new JFrame("Play Pente!!");
		theGame.setSize(gWidth, gHeight + 20);
		theGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		PenteGB gb = new PenteGB(gWidth, gHeight);
		
		theGame.add(gb);
		
		theGame.setVisible(true);
		
		gb.startNewGame();
		
		
	}

}
