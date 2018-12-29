package lunar_lander;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private Rocket player; // Set player model

    private Map map;

    private Landingspace landingSpace; // Area to land

    private BufferedImage failed; // Failed image

    private static int health = 5;

    private static int level = 1;

    public Main() {

        Thread threadForInitGame = new Thread() {

            public void run() {
                initialize(); // Set all variables

                loadcontent(); // Get game files

                Framework.gameState = Framework.GameState.RUNNING;
            }
        };
        threadForInitGame.start();
    }

    private void initialize() { // Start new game
        if(health<=0)
        {
            level=1;
            health=5;
        }
        map = new Map(level);
        player = map.getPlayer();
        landingSpace = new Landingspace(map.getLandingSpacePosX(), map.getLandingSpacePosY());

    }

    private void loadcontent() {
        try {
            URL failedUrl = this.getClass().getResource("/lunar_lander/resources/img/failed.png");
            failed = ImageIO.read(failedUrl);

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void UpdateGame(double gameTime, Point mousePosition) // Get position of rocket
    {
        player.Update();

        if (Control.keyboardKeyState(KeyEvent.VK_ESCAPE)) {
            Framework.gameState = Framework.GameState.PAUSE;
        }
        if (map.checkCollision()) Framework.gameState = Framework.GameState.GAMEOVER;
    }

    public void draw(Graphics2D g2d, Point mousePosition) {

        map.draw(g2d);
        landingSpace.draw(g2d);
        player.draw(g2d);
    }

    public void drawgameover(Graphics2D g2d, Point mousePosition,
                             double gameTime) // Outputs if landed or crashed
    {
        draw(g2d, mousePosition);

        if (player.isLanded()) {
            g2d.setColor(Color.green);
            g2d.drawString("Congrats!", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3);
            g2d.drawString("You landed in " + gameTime / Framework.SECINNANO + " seconds.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 20);
            g2d.drawString("Press Enter to return to the menu.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 60);
        } else {
            g2d.drawImage(failed, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
            g2d.setColor(Color.red);
            g2d.drawString("Fail!", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 70);
            g2d.drawString("Press Enter to return to the main menu.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 90);
        }
    }

    public boolean isPlayerLanded() {
        return player.isLanded();
    }

    public boolean isPlayerCrashed() {
        return player.isCrashed();
    }

    public void playerLostHealth() {
        health--;
    }

    public static int getHealth() {
        return health;
    }

    public static void setLevel(int level) {
        Main.level = level;
    }

    public static int getLevel() {
        return level;
    }
}