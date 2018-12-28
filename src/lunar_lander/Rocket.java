package lunar_lander;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Rocket {

	private Random random; // Random X start position

	private int x; // X coordinate (2D)

	private int y; // Y coordinate (2D)

	private boolean landed; // Check if landed

	private boolean crashed; // Check if crashed

	private int speedAccelerating; // Acceleration

	private int maxLandingSpeed; // Max speed for land

	private double speedX; // Horizontal speed

	private double speedY; // Vertical speed

	private double speedGrav; // Gravity

    private double actualFuel,maxFuel;

	private BufferedImage landerRocket; // Lunar Lander

	private BufferedImage landerLanded; // Landed Lander

	private BufferedImage landerCrashed; // Crashed Lander

	private BufferedImage landerFlyingUp; // Lander going up Lander
	
	private BufferedImage landerFlyingDown; // Accelerating Lander
	
	private BufferedImage landerFlyingRight; // Lander flying left
	
	private BufferedImage landerFlyingLeft; // Lander flying right

	public int landerRocketWidth; // Read image width

	public int landerRocketHeight; // Read image height

	public Rocket() // Gather rocket dimensions
	{
		initialize();
		loadcontent();

		x = random.nextInt(Framework.frameWidth - landerRocketWidth); // X random start
	}

	private void initialize() {
	random = new Random(); // Initialize random start

		speedAccelerating = 1;
		speedY = 1;
		speedGrav = -0.16;
		maxLandingSpeed = 5;
		maxFuel=50;
		actualFuel=50;
	}


	private void loadcontent() // Load resources
	{
		try {
			URL landerRocketURL = this.getClass().getResource("/lunar_lander/resources/img/landerRocket.png");
			landerRocket = ImageIO.read(landerRocketURL);
			landerRocketWidth = landerRocket.getWidth();
			landerRocketHeight = landerRocket.getHeight();

			URL landerLandedURL = this.getClass().getResource("/lunar_lander/resources/img/lander_landed.png");
			landerLanded = ImageIO.read(landerLandedURL);

			URL landerCrashedURL = this.getClass().getResource("/lunar_lander/resources/img/lander_crash.png");
			landerCrashed = ImageIO.read(landerCrashedURL);

			URL landerFlyingUpURL = this.getClass().getResource("/lunar_lander/resources/img/lander_fire_up.png");
			landerFlyingUp = ImageIO.read(landerFlyingUpURL);
			
			URL landerFlyingDownURL = this.getClass().getResource("/lunar_lander/resources/img/lander_fire_down.png");
			landerFlyingDown = ImageIO.read(landerFlyingDownURL);
			
			URL landerFlyingRightURL = this.getClass().getResource("/lunar_lander/resources/img/lander_fire_right.png");
			landerFlyingRight = ImageIO.read(landerFlyingRightURL);
			
			URL landerFlyingLeftURL = this.getClass().getResource("/lunar_lander/resources/img/lander_fire_left.png");
			landerFlyingLeft = ImageIO.read(landerFlyingLeftURL);
			
		} catch (IOException ex) {
			Logger.getLogger(Rocket.class.getName())
					.log(Level.SEVERE, null, ex);
		}
	}

	public void Update() // Inverted rocket controls
	{
	    if(actualFuel<=0)Framework.gameState = Framework.GameState.GAMEOVER;
		if (Control.keyboardKeyState(KeyEvent.VK_DOWN)) { // Key DOWN
            speedY -= speedAccelerating;
            actualFuel -= 0.1;
        }
		else {
			speedY -= speedGrav;
		}

		if (Control.keyboardKeyState(KeyEvent.VK_UP)){ // Key UP
			speedY += speedAccelerating;
            actualFuel-=0.1;
		}
		if (Control.keyboardKeyState(KeyEvent.VK_RIGHT)){ // Key RIGHT
			speedX -= speedAccelerating;
            actualFuel-=0.1;
		}
		if (Control.keyboardKeyState(KeyEvent.VK_LEFT)){ // Key LEFT
			speedX += speedAccelerating;
            actualFuel-=0.1;
	        }
		if (Control.keyboardKeyState(KeyEvent.VK_0)){ // Cheat
			speedY = 0;
			speedX = 0;
		}
		x += speedX;
		y += speedY;

	}
	

	public void draw(Graphics2D g2d) {
        double yScale=(double)Framework.frameHeight/720;
        double xScale=(double)Framework.frameWidth/1280;

		if (landed) // Check if landed
		{
			g2d.drawImage(landerLanded, (int)(xScale*x), (int)(yScale*y), (int)(xScale*landerRocketWidth),(int)(yScale*landerRocketHeight), null);
		} else if (crashed) // Check if crashed
		{
			g2d.drawImage(landerCrashed,(int)(xScale*x), (int)(yScale*y), (int)(xScale*landerRocketWidth),(int)(yScale*landerRocketHeight), null);
		} else {
			if (Control.keyboardKeyState(KeyEvent.VK_UP)) // Draw fly image
			g2d.drawImage(landerFlyingDown, (int)(xScale*x), (int)(yScale*y), (int)(xScale*landerRocketWidth),(int)(yScale*landerRocketHeight), null);
			g2d.drawImage(landerRocket, (int)(xScale*x), (int)(yScale*y), (int)(xScale*landerRocketWidth),(int)(yScale*landerRocketHeight), null);

			if (Control.keyboardKeyState(KeyEvent.VK_DOWN)) // Draw fly image
			g2d.drawImage(landerFlyingUp, (int)(xScale*x), (int)(yScale*y), (int)(xScale*landerRocketWidth),(int)(yScale*landerRocketHeight), null);
			g2d.drawImage(landerRocket, (int)(xScale*x), (int)(yScale*y), (int)(xScale*landerRocketWidth),(int)(yScale*landerRocketHeight), null);

			if (Control.keyboardKeyState(KeyEvent.VK_RIGHT)) // Draw fly image
			g2d.drawImage(landerFlyingLeft, (int)(xScale*x), (int)(yScale*y), (int)(xScale*landerRocketWidth),(int)(yScale*landerRocketHeight), null);
			g2d.drawImage(landerRocket, (int)(xScale*x), (int)(yScale*y), (int)(xScale*landerRocketWidth),(int)(yScale*landerRocketHeight), null);

			if (Control.keyboardKeyState(KeyEvent.VK_LEFT)) // Draw fly image
			g2d.drawImage(landerFlyingRight, (int)(xScale*x), (int)(yScale*y), (int)(xScale*landerRocketWidth),(int)(yScale*landerRocketHeight), null);
			g2d.drawImage(landerRocket, (int)(xScale*x), (int)(yScale*y), (int)(xScale*landerRocketWidth),(int)(yScale*landerRocketHeight), null);

		double fuelStatus=actualFuel/maxFuel;
		g2d.setColor(Color.BLACK);
		g2d.drawRect(Framework.frameWidth-50,0,50,15);
		g2d.fillRect(Framework.frameWidth-50,0,(int)(fuelStatus*50),15);
		}
	}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isLanded() {
        return landed;
    }

    public boolean isCrashed() {
        return crashed;
    }

    public int getSpeedAccelerating() {
        return speedAccelerating;
    }

    public int getMaxLandingSpeed() {
        return maxLandingSpeed;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public double getSpeedGrav() {
        return speedGrav;
    }

    public double getActualFuel() {
        return actualFuel;
    }

    public double getMaxFuel() {
        return maxFuel;
    }

    public void setLanded(boolean landed) {
        this.landed = landed;
    }

    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }
}