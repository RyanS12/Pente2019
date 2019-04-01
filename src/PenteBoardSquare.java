import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PenteBoardSquare {
	private int xLoc,yLoc;
	private int sWidth, sHeight;
	
	private int sState;
	
	private Color sColor; //square
	private Color lColor; //line
	private Color bColor; //color of boarder
	private Color innerC;
	
	
	private Color darkStoneColor = new Color(4, 9, 64);
	private Color darkStoneTop = new Color(63, 69, 158);
	private Color darkStoneHighLight = new Color(188, 199, 255);
	
	private Color shadowGrey = new Color(132, 101, 54);
	
	private Color lightStoneColor = new Color(224,222,204);
	private Color lightStoneTop = new Color(250,250,250);
	
	
	boolean isInner = false;
	
	
	
	
	
	
	
	//constructor
	public PenteBoardSquare(int x, int y, int w, int h) {
		
		
		xLoc = x;
		yLoc = y;
		sWidth = w;
		sHeight = h;
		
		sColor = new Color(255, 223, 145);
		lColor = new Color(0,0,0);
		bColor = Color.YELLOW;
		innerC = new Color(255, 238, 150);
		
		
		
		
		sState = PenteGB.EMPTY;
		
		
		
	}
	
	public void setInner() {
		
		isInner = true;
		
	}
	
	public void drawMe(Graphics g) {
		
		//step 1 draw board square
		if(isInner == true) {
			g.setColor(innerC);
		} else {
			g.setColor(sColor);
		}
		
		g.fillRect(xLoc, yLoc, sWidth, sHeight);
		
		
		g.setColor(bColor);
		g.drawRect(xLoc, yLoc, sWidth, sHeight);
		
		
		if(sState != PenteGB.EMPTY) {
			g.setColor(shadowGrey);
			g.fillOval(xLoc, yLoc + 6, sWidth - 8, sHeight - 8);
		}
		
		
		g.setColor(lColor);
		
		//Horizontal lines
		
		g.drawLine(xLoc, yLoc+sHeight/2, xLoc + sWidth, yLoc + sHeight/2);
		
		//Vertical lines 
		
		g.drawLine(xLoc + sWidth/2, yLoc, xLoc + sWidth/2, yLoc + sHeight);
		
		
		
		
		
		
		
		
		//step 3 stones
		
		if(sState == PenteGB.BLACKSTONE) {
			g.setColor(darkStoneColor);
			g.fillOval(xLoc + 4, yLoc + 4, sWidth - 8, sHeight - 8);
			
			g.setColor(darkStoneTop);
			g.fillOval(xLoc + 8, yLoc + 6, sWidth - 12,  sHeight - 10);
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(3));
			
			g2.setColor(darkStoneHighLight);
			
			g2.setStroke(new BasicStroke(1));
			
			
			
			
			
			
			g2.drawArc(xLoc + (int)(sWidth*0.45),
					yLoc + 10, 
					(int)(sWidth*0.30),
					(int)(sHeight * 0.35),
					0,
					90);
		}
		
		if(sState == PenteGB.WHITESTONE) {
			g.setColor(lightStoneColor);
			g.fillOval(xLoc + 4, yLoc + 4, sWidth - 8, sHeight - 8);
			
			g.setColor(lightStoneTop);
			g.fillOval(xLoc + 8, yLoc + 6, sWidth - 12, sHeight - 10);
		}
		
	}
	
	public void setState(int newState) {
		
		if(newState <  - 1 || newState > 1) {
			System.out.println("wrong domain on newstate");
		} else {
			sState = newState;
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
