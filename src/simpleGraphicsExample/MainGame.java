//Most of this is from http://compsci.ca/v3/viewtopic.php?t=25991

package simpleGraphicsExample;
import java.awt.*; 
import java.awt.event.KeyEvent; 
import java.awt.image.BufferedImage; 
import javax.swing.JFrame;

import simpleGraphicsExample.InputHandler;

public class MainGame extends JFrame 
{        
	private static final long serialVersionUID = 1L;
	boolean isRunning = true; 
	int fps = 30; 
	int windowWidth = 500; 
	int windowHeight = 500; 

	BufferedImage backBuffer; 
	Insets insets; 
	InputHandler input; 

	int x = 0; 
	int y = 0;

	public static void main(String[] args) 
	{ 
		MainGame game = new MainGame(); 
		game.run(); 
		System.exit(0); 
	} 

	/** 
	 * This method starts the game and runs it in a loop 
	 */ 
	 public void run() 
	{ 
		initialize(); 

		while(isRunning) 
		{ 
			long time = System.currentTimeMillis(); 

			update(); 
			draw(); 

			//  delay for each frame  -   time it took for one frame 
			time = (1000 / fps) - (System.currentTimeMillis() - time); 

			if (time > 0) 
			{ 
				try 
				{ 
					Thread.sleep(time); 
				} 
				catch(Exception e){} 
			} 
		} 

		setVisible(false); 
	} 

	 /** 
	  * This method will set up everything need for the game to run 
	  */ 
	 void initialize() 
	 { 
		 setTitle("Game Tutorial"); 
		 setSize(windowWidth, windowHeight); 
		 setResizable(false); 
		 setDefaultCloseOperation(EXIT_ON_CLOSE); 
		 setVisible(true); 

		 insets = getInsets(); 
		 setSize(insets.left + windowWidth + insets.right, 
				 insets.top + windowHeight + insets.bottom); 

		 backBuffer = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB); 
		 input = new InputHandler(this); 
	 } 

	 /** 
	  * This method will check for input, move things 
	  * around and check for win conditions, etc 
	  */ 
	 void update() 
	 { 
		 if (input.isKeyDown(KeyEvent.VK_RIGHT)) 
		 { 
			 x += 5; 
		 } 
		 if (input.isKeyDown(KeyEvent.VK_LEFT)) 
		 { 
			 x -= 5; 
		 }
		 if (input.isKeyDown(KeyEvent.VK_DOWN)) 
		 { 
			 y += 5; 
		 } 
		 if (input.isKeyDown(KeyEvent.VK_UP)) 
		 { 
			 y -= 5; 
		 } 
	 } 

	 /** 
	  * This method will draw everything 
	  */ 
	 void draw() 
	 {               
		 Graphics g = getGraphics(); 

		 Graphics bbg = backBuffer.getGraphics(); 

		 bbg.setColor(Color.cyan); 
		 bbg.fillRect(0, 0, windowWidth, windowHeight); 

		 bbg.setColor(Color.BLACK); 
		 bbg.drawRoundRect(x, y, 32, 32, 12, 12); 
		 
		 g.drawImage(backBuffer, insets.left, insets.top, this); 
	 } 
} 