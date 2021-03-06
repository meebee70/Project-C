package characters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.sun.glass.events.KeyEvent;

import NonPlayerObjects.Coins;

import levels.Level;
import mainGameEngine.InputHandler;
import mainGameEngine.StateManager;
import physics.Physics;
import terrain.LevelEnder;
import terrain.Terrain;

public class PlayerOne {
	final int FPS;
	
	private Level level;
  
	private final double BASE_X_SPEED, JUMPSPEED,GRAVITY; // pixels/frame
	private final int JUMPSMAX,ANIMATION_SPEED;

	private int keyLeft, keyRight, keyUp, jumps, xDirection, frame;
	private double x, y, xVelocity, yVelocity, playerOnePoints;
	private boolean keyReleasedUp, inAir,touchesWall;

	private Image playerOneStationairy, playerOneSprite;
	private ArrayList<Image> playerOneRight, playerOneLeft;

	private StateManager sm;

	private InputHandler input;

	Physics physics = new Physics();

	final private int HEIGHT;
	final private int WIDTH;

	public PlayerOne(int x, int y, StateManager sm, Level levelThatWeAreIn){
		this.x = x;
		this.y = y;
		this.xVelocity = 0;
		this.yVelocity = 0;
		this.keyLeft = 0;
		this.keyRight = 0;
		this.keyUp = 0;
		this.jumps = 0;
    this.playerOnePoints = 0;
		this.frame = 0;
		
		this.FPS = levelThatWeAreIn.getFPS();
		
		BASE_X_SPEED = 60 / FPS; // pixels/frame
		JUMPSPEED = 2.1;
		GRAVITY = physics.getGravity();
		JUMPSMAX = 1;
		ANIMATION_SPEED = 4;
		touchesWall = false;

		this.HEIGHT = 32;	//Of sprite or Hitbox
		this.WIDTH = 22; //Update later
		this.sm = sm;
		this.level = levelThatWeAreIn;
		input = sm.input;


		try {
			this.playerOneStationairy = ImageIO.read(new File("res/PlayerSprites/Player 1.gif"));
			//this.playerOneRight = ImageIO.read(new File("res/PlayerSprites/Player 1 walk right.gif"));
			//this.playerOneLeft = ImageIO.read(new File("res/PlayerSprites/Player 1 walk left.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		playerOneRight = new ArrayList<Image>();
		playerOneLeft = new ArrayList<Image>();
		loadSprite(playerOneRight, "res/PlayerSprites/Player 1 walk right.gif");
		loadSprite(playerOneLeft, "res/PlayerSprites/Player 1 walk left.gif");
		

	}

	//ACCESSORS
	public int getCurrentX() {
		return (int) x;
	}

	public int getCurrentY() {
		return (int) y;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public int getWidth() {
		return WIDTH;
	}

	public Image getSprite(){
		return playerOneSprite;
	}

	

	public void setSprite(Image sprite){
		this.playerOneSprite = sprite;
	}
	public void setSprite(ArrayList<Image> sprite){
		this.playerOneSprite = sprite.get((int)((this.frame / ANIMATION_SPEED) % sprite.size()));
		
	}
	public void setSprite(ArrayList<Image> sprite, int i){
		this.playerOneSprite = sprite.get(i);
	}
	
	private void loadSprite(ArrayList<Image> gifList, String fileName){
		try {
			ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
			File input = new File(fileName);
			ImageInputStream stream = ImageIO.createImageInputStream(input);
			reader.setInput(stream);

			int count = reader.getNumImages(true);
			for (int index = 0; index < count; index++) {
				BufferedImage frame = reader.read(index);
				// Here you go
				gifList.add(frame);
			}
		} catch (IOException ex) {
		}
	}
	//MUTATORS
	//@Param = new x and/or y coord
	public void setCurrentX(double newX) {
		this.x = newX;
	}
	public void setCurrentY(double newY) {
		this.y = newY;
	}
	public void setXandY(double newX, double newY){
		this.x = newX;
		this.y = newY;
	}

	//Adds @Param to x and/or y coord
	public void moveX(double xVelocity) {
		this.x += xVelocity;
	}
	public void moveY(double yVelocity) {
		this.y += yVelocity;
	}
	public void moveXandY(double xVelocity, double yVelocity){
		this.x += xVelocity;
		this.y += yVelocity;
	}

	public boolean inAir(Terrain[] platforms){
		inAir = true;
		for (Terrain form: platforms){

			int aX = this.getCurrentX();
			int aY2 = this.getCurrentY();
			int aX2 = aX + this.getWidth();
			int aY = aY2 + this.getHeight();
			inAir = !(Physics.collides(aX, aY+1, aX2, aY2+1, form) || !inAir);
		}
		return inAir;
	}

	/**
	 * Updates Player Object while getting input and calculating new x & y
	 */
	public void updatePlayer(Terrain[] platforms,PlayerTwo player2){
		this.inAir(platforms);
		getInputs();
		frame++;
		
		xDirection = this.keyLeft + this.keyRight;

		xVelocity = (this.keyLeft + this.keyRight) * this.BASE_X_SPEED;
		//yVelocity = (this.keyUp + this.keyDown) * this.BASE_Y_SPEED;

		//Collision (Should we have this in a separate method?)
		if (this.x + xVelocity < 0){
			this.setCurrentX(0);
			this.xVelocity = 0;
		}
		if (this.x + xVelocity >= sm.UNIVERSE_WIDTH - WIDTH){
			this.setCurrentX(sm.UNIVERSE_WIDTH - WIDTH -1);
			this.xVelocity = 0;
		}

		//Uses collision in physics class to calculate physics for all the objects

		if (yVelocity < 10){
			yVelocity += GRAVITY;
		}
		
		this.collisionCalculate(platforms,player2);

		if (touchesWall && input.isKeyDown(KeyEvent.VK_W)){
			yVelocity = -this.BASE_X_SPEED;
		}else if (this.keyUp == 1 && jumps > 0){
			jumps--;
			yVelocity = -JUMPSPEED;
		}

		coinCollision();
		this.moveXandY(xVelocity, yVelocity);
		this.updateSprites();

		xVelocity = 0;
		//yVelocity = 0;
		keyLeft = 0;
		keyRight = 0;
		keyUp = 0;

	}
	
	private int sign(double velocity){
		if (velocity > 0){
			return 1;
		} else if (velocity < 0){
			return -1;
		} else {
			return 0;
		}
	}
	
	private void getInputs(){
		if (!keyReleasedUp && input.isKeyDown(KeyEvent.VK_W)){
			this.keyUp = 1;
		}
		keyReleasedUp = input.isKeyDown(KeyEvent.VK_W);

		//Set xSpeed
		if (input.isKeyDown(KeyEvent.VK_A)){
			this.keyLeft = -1;
		}
		if (input.isKeyDown(KeyEvent.VK_D)){
			this.keyRight = 1;
		}
	}
	
	private void collisionCalculate(Terrain[] platforms, PlayerTwo player2){
		boolean[] touches = new boolean[platforms.length];
		for (Terrain form: platforms){
			
			int i = 0;
			
			//player 1 coords
			int aX = this.getCurrentX();
			int aY2 = this.getCurrentY();
			int aX2 = aX + this.getWidth();
			int aY = aY2 + this.getHeight();

			//player 2 coords
			int bX = player2.getCurrentX() - 1;
			int bY2 = player2.getCurrentY() - 1;
			int bX2 = bX + player2.getWidth();
			int bY =  bY2 + player2.getHeight();
			
			//X Collision
			if (Physics.collides(aX + xVelocity, aY, aX2 + xVelocity, aY2, form)){
				while(!Physics.collides(aX + sign(xVelocity), aY, aX2 + sign(xVelocity), aY2, form)){
					this.moveX(sign(xVelocity));

					aX = this.getCurrentX();
					aX2 = aX + this.getHeight();
					
					
				}
				
				if (form.getClass() == LevelEnder.class){
					((LevelEnder) form).goToNextLevel();
				}
				
				xVelocity = 0;
				touches[i] = true;
			}
			
			if (Physics.collides(aX + xVelocity,aY,aX2 + xVelocity,aY2,bX,bY,bX2,bY2)){
				xVelocity = 0;
			}
			//Y Collision
			if (Physics.collides(aX, aY+1, aX2, aY2+1, form)){
				jumps = JUMPSMAX;
			}

			if (Physics.collides(aX, aY + yVelocity, aX2, aY2 + yVelocity, form)){
				while(!Physics.collides(aX, aY + sign(yVelocity), aX2, aY2 + sign(yVelocity), form))
				{
					this.moveY(sign(yVelocity));

					aY = this.getCurrentY();
					aY2 = aY + this.getHeight();
				}
				if (form.getClass() == LevelEnder.class){
					((LevelEnder) form).goToNextLevel();
				}
				
				yVelocity = 0;
			}
			
			if (Physics.collides(aX,aY + yVelocity,aX2,aY2 + yVelocity,bX,bY,bX2,bY2)){
				yVelocity = 0;
			}

			if (getCurrentY() + yVelocity < 0){
				yVelocity = 0;
			}

			i++;
		}
		
		touchesWall = false;
		
		for (int i = 0; i < platforms.length; i++){
			touchesWall = touches[i] || touchesWall;
		}

	}
	
	private void coinCollision(){
		ArrayList<Coins> listOfCoins = this.level.getCoinsList();
		
		for (Coins coin : listOfCoins){
			final double aX = this.getCurrentX();
			final double aY2 = this.getCurrentY();
			final double aX2 = aX + this.getWidth();
			final double aY = aY2 + this.getHeight();
			final double bX = coin.getCurrentX();
			final double bY2 = coin.getCurrentY();
			final double bX2 = bX + coin.getWidth();
			final double bY = bY2 + coin.getHeight();
			//Coin Collision
			if (Physics.collides(aX, aY, aX2, aY2, bX, bY, bX2, bY2) && coin.getPoints()){
				coin.setSprite(null);
				playerOnePoints++;
				coin.givePoints();
				System.out.println(playerOnePoints);
				}
			}
		}


	//Changes Sprites Based on Movement/Actions
	public void updateSprites(){
		if (xDirection == 1){
			this.setSprite(playerOneRight);
		} else if (xDirection == -1){
			this.setSprite(playerOneLeft);
		} else {
			this.setSprite(playerOneStationairy);
		}
	}

}
