/*
 * Landing area
 */
package lunar_lander;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Landingspace {


    public int x; // X coordinate

    public int y; // Y coordinate

    private BufferedImage landingSpace;

    public int landingSpaceWidth;
    public int landingSpaceHeight;


    public Landingspace(int xPosition, int yPosition) {
        loadcontent();

        x = xPosition;
        y = yPosition;
    }

    private void loadcontent() {
        try {
            URL landingSpaceUrl = this.getClass().getResource("/lunar_lander/resources/img/landing_area.png");
            landingSpace = ImageIO.read(landingSpaceUrl);
            landingSpaceWidth = landingSpace.getWidth();
            landingSpaceHeight = landingSpace.getHeight();
        } catch (IOException ex) {
            Logger.getLogger(Landingspace.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void draw(Graphics2D g2d) {
        double yScale = (double) Framework.frameHeight / 720;
        double xScale = (double) Framework.frameWidth / 1280;
        g2d.drawImage(landingSpace, (int) (xScale * x), (int) (yScale * y), (int) (xScale * Framework.frameWidth / 10), (int) (yScale * landingSpaceHeight), null);
    }

}