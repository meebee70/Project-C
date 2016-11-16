//Most of this is from http://compsci.ca/v3/viewtopic.php?t=25991

package mainGameEngine;
import java.awt.*; 
import java.awt.event.KeyEvent; 
import java.awt.image.BufferedImage; 
import javax.swing.JFrame;

import mainGameEngine.InputHandler;

public class MainMenu extends JFrame 
{        
	private static final long serialVersionUID = 1L;
	boolean isRunning = true; 
	int fps = 30;
	final int windowWidth = 500;
	final int windowHeight = 500;

	BufferedImage backBuffer; 
	Insets insets; 
	InputHandler input; 

	private int currentlySelected;
	private String[] menuItems = {"Start","Help","QUIT"};
	
	private Font normalFont= new Font("ARIEL",Font.PLAIN,40);
	private Font selectedFont= new Font("ARIEL",Font.BOLD,60);
	
	

	public static void main(String[] args) 
	{ 
		MainMenu game = new MainMenu(); 
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
		//moves the selector up and down
		if (input.isKeyDown(KeyEvent.VK_UP)){
			 currentlySelected--;
		}else if (input.isKeyDown(KeyEvent.VK_DOWN)){
			 currentlySelected++;
		}
		//loops the selector around the list
		if (currentlySelected < 0){
			 currentlySelected = menuItems.length -1;
		}else if (currentlySelected > menuItems.length -1){
			 currentlySelected = 0;
		}
		
		if (input.isKeyDown(KeyEvent.VK_ENTER)){
			if (currentlySelected == 0){
				
			}else if (currentlySelected == 1){
				
			}else if (currentlySelected == 2){
				isRunning = false;
			}
		}
		 
	 } 

	 /** 
	  * This method will draw everything 
	  */ 
	 void draw() 
	 {               
		 Graphics g = getGraphics(); 

		 Graphics bbg = backBuffer.getGraphics(); 

		 bbg.setColor(Color.BLACK); 
		 bbg.fillRect(0, 0, windowWidth, windowHeight); 
		 
		 
		 for (int i = 0;i < menuItems.length;i++){
			 if (currentlySelected == i){
				 bbg.setFont(selectedFont);
				 bbg.setColor(Color.GREEN);
				 
			 }else{
				 bbg.setFont(normalFont);
				 bbg.setColor(Color.BLUE);
			 }
			 bbg.drawString(menuItems[i], (windowWidth / 2) - 50, 50 + (i * 100));

		 }
		 
		 
		 g.drawImage(backBuffer, insets.left, insets.top, this); 
	 } 
} 