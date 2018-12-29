package lunar_lander;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Map {
    private int[] map; //Generating irregular shapes , every element has own height

    private int speedAccelerating; // Acceleration

    private int maxLandingSpeed; // Max speed for land

    private int fuel; //Declared in map configuration

    private double speedY; // Vertical speed

    private double speedGrav; // Gravity

    private int landingSpacePos; //Declared in map configuration

    private Rocket player;

    private Ellipse2D deadCircle;


    public Map(int level) {
        loadWorld("resources/maps/map" + level + ".txt"); //Reading map configuration from specific file
        player = new Rocket();
        initializeRocket();
        initializeCircle();
    }

    private void initializeRocket() {
        player.setSpeedAccelerating(speedAccelerating);
        player.setSpeedY(speedY);
        player.setSpeedGrav(speedGrav);
        player.setMaxLandingSpeed(maxLandingSpeed);
        player.setMaxFuel(fuel);
        player.setActualFuel(fuel);
    }

    private void initializeCircle() {

        int level = Main.getLevel();
        Random random = new Random();
        int x = random.nextInt(Framework.frameWidth / 2);
        int y = random.nextInt(Framework.frameHeight / 2);
        deadCircle = new Ellipse2D.Double(x, y, 20 * level, 20 * level);

    }

    private void loadWorld(String path) {
        String file = Utils.loadFileAsString(path);
        String[] tokens = file.split("\\s+");
        speedAccelerating = Utils.parseInt(tokens[0]);
        speedY = Utils.parseInt(tokens[1]);
        speedGrav = (double) (Utils.parseInt(tokens[2])) / (-10);   //Dividing to achieve negative double
        maxLandingSpeed = Utils.parseInt(tokens[3]);
        fuel = Utils.parseInt(tokens[4]);
        landingSpacePos = Utils.parseInt(tokens[5]);

        map = new int[10];
        for (int i = 0; i < map.length; i++) {
            map[i] = Utils.parseInt(tokens[i + 6]);
        }
    }

    public void draw(Graphics2D g2d) {
        double yScale = (double) Framework.frameHeight / 720;
        double xScale = (double) Framework.frameWidth / 1280;
        //Section drawing circle
        g2d.fillRoundRect((int) deadCircle.getX() - 10, (int) deadCircle.getY() - 10,
                (int) deadCircle.getWidth(), (int) deadCircle.getHeight(),
                (int) deadCircle.getWidth(), (int) deadCircle.getHeight());

        //Updating circle if game not paused
        if (Framework.gameState == Framework.GameState.RUNNING)
            checkCircle();

        //Section drawing irregular ground
        for (int i = 0; i < map.length; i++) {
            g2d.setColor(Color.CYAN);
            g2d.fillRect((int) (xScale * i * ((double) Framework.frameWidth / 10)), (int) ((Framework.frameHeight - map[i]) * yScale), (int) (Framework.frameWidth / 10 * xScale), (int) (yScale * map[i]));
        }
    }

    public boolean checkCollision() {

        Rectangle rocketRectangle = new Rectangle(player.getX(), player.getY(), player.landerRocketWidth, player.landerRocketHeight);

        if (checkCollisionWithCircle(rocketRectangle)) {
            player.setCrashed(true);
            return true;
        }

        int xSection = player.getX() / (Framework.frameWidth / 10);
        if (xSection == landingSpacePos) {
            if (player.getY() + player.landerRocketHeight - 10 > getLandingSpacePosY()) {
                if ((player.getX() > getLandingSpacePosX())
                        && (player.getX() < getLandingSpacePosX() + Framework.frameWidth / 10 - (player.landerRocketWidth) / 2)) {
                    if (player.getSpeedY() <= player.getMaxLandingSpeed())
                        player.setLanded(true);
                    else
                        player.setCrashed(true);
                } else
                    player.setCrashed(true);

                Framework.gameState = Framework.GameState.GAMEOVER;
            }
        }
        if (xSection > -1 && xSection < 10) {
            Rectangle ground = new Rectangle((int) (xSection * ((double) Framework.frameWidth / 10)), ((Framework.frameHeight - map[xSection])), (Framework.frameWidth / 10), (map[xSection]));
            if (rocketRectangle.intersects(ground)) {
                player.setCrashed(true);
                return true;
            } else return false;
        } else {
            player.setCrashed(true);
            return true;
        }
    }

    private void checkCircle() {
        if (deadCircle.getCenterX() < Framework.frameWidth + deadCircle.getWidth() && deadCircle.getCenterX() > -deadCircle.getWidth()
                && deadCircle.getCenterY() < Framework.frameHeight + deadCircle.getHeight())
            updateCircle();
    }

    private void updateCircle() {
        if (!player.isCrashed()) {
            int move = Main.getLevel();
            deadCircle.setFrame(deadCircle.getX() + move, deadCircle.getY() + move, deadCircle.getWidth(), deadCircle.getHeight());
        }
    }

    private boolean checkCollisionWithCircle(Rectangle rect) {
        return rect.intersects(deadCircle.getBounds());
    }

    //Getters and setters
    public int getLandingSpacePosX() {
        return (int) (landingSpacePos * ((double) Framework.frameWidth / 10));
    }

    public int getLandingSpacePosY() {
        return Framework.frameHeight - map[landingSpacePos] - 15;
    }

    public Rocket getPlayer() {
        return player;
    }
}
