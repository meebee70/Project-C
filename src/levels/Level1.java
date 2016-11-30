package levels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.glass.events.KeyEvent;

import mainGameEngine.InputHandler;
import mainGameEngine.StateManager;

public class Level1 extends Level {
	private Graphics g;
	private int left = sm.insets.left;
	private int top = sm.insets.top;
	private Image background;
	private InputHandler input = sm.input;
	private static int universeWidth = 3000;
	private static int universeHeight = 1200;
	
	private int x1 = left;
	private int y1 = top;
	
	private int x2 = left + 100;
	private int y2 = top - 30;
	
	private int xUniverse = ((x1 + x2)/2);
	private int yUniverse = ((y1 +y2)/2);
	

	public Level1(StateManager sm) {
		super(sm);
		g = sm.getGraphics();
		
	}

	@Override
	public void initialize() {
		g.clearRect(left, top, sm.WINDOW_WIDTH,sm.WINDOW_HEIGHT);
		
		try {
			background = ImageIO.read(new File("Laughing Stock.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.drawImage(background,(int)x1 , (int)y1, sm);

		
		
	}

	@Override
	public void update() {
		//player 1
		if (input.isKeyDown(KeyEvent.VK_W)){
			y1--;
		}else if (input.isKeyDown(KeyEvent.VK_S)){
			y1++;
		}if (input.isKeyDown(KeyEvent.VK_A)){
			x1--;
		}else if (input.isKeyDown(KeyEvent.VK_D)){
			x1++;
		}
		
		//player 2
		if (input.isKeyDown(KeyEvent.VK_UP)){
			y2--;
		}else if (input.isKeyDown(KeyEvent.VK_DOWN)){
			y2++;
		}if (input.isKeyDown(KeyEvent.VK_LEFT)){
			x2--;
		}else if (input.isKeyDown(KeyEvent.VK_RIGHT)){
			x2++;
		}
		
		if(input.isKeyDown(KeyEvent.VK_ESCAPE)){
			sm.levels.pop();
		}
		
		xUniverse =-((x1 + x2)/2) + (sm.WINDOW_WIDTH/2);
		yUniverse =-((y1 +y2)/2) + (sm.WINDOW_HEIGHT /2);
		
		System.out.println(x1 + " " + y1 + "  " + x2 + " " + y2 + "  " + xUniverse + " " + yUniverse);
	}

	@Override
	public void draw() {
		Graphics universe = sm.backBuffer.getGraphics();
		universe.fillRect(left,top,universeWidth,universeHeight);
		universe.drawImage(background,x1,y1,sm);
		universe.drawImage(background, x2,y2, sm);
		
		universe.setColor(Color.BLUE);
		universe.fillRect(0, 0, universeWidth, 5);
		universe.fillRect(0, 0, 5, universeHeight);
		universe.fillRect(universeWidth, 0, 5,universeHeight);
		universe.fillRect(0, universeHeight, universeWidth, 5);
		
		g.drawImage(sm.backBuffer,xUniverse,yUniverse,sm);
	}

}
