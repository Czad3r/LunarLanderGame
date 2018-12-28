package lunar_lander;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Map {
    private int[] map; //Generating irregular shapes , every element has own height

    private int speedAccelerating; // Acceleration

    private int maxLandingSpeed; // Max speed for land

    private int fuel;

    private double speedY; // Vertical speed

    private double speedGrav; // Gravity

    private int landingSpacePos;

    private Rocket player;


    public Map(int level) {
        loadWorld("resources/maps/map" + level + ".txt");
        player = new Rocket();
        initializeRocket();
    }

    private void initializeRocket() {
        player.setSpeedAccelerating(speedAccelerating);
        player.setSpeedY(speedY);
        player.setSpeedGrav(speedGrav);
        player.setMaxLandingSpeed(maxLandingSpeed);
        player.setMaxFuel(fuel);
        player.setActualFuel(fuel);
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
        for (int i = 0; i < map.length; i++) {
            g2d.setColor(Color.CYAN);
            g2d.fillRect((int) (xScale * i * ((double) Framework.frameWidth / 10)), (int) ((Framework.frameHeight - map[i]) * yScale), (int) (Framework.frameWidth / 10 * xScale), (int) (yScale * map[i]));
        }
    }

    public boolean checkCollision() {

        Rectangle rocketRectangle = new Rectangle(player.getX(),player.getY(),player.landerRocketWidth,player.landerRocketHeight);
        int xSection=player.getX()/(Framework.frameWidth / 10);
        if(xSection==landingSpacePos) {
            if (player.getY() + player.landerRocketHeight - 10 > getLandingSpacePosY()) {
                if ((player.getX() > getLandingSpacePosX())
                        && (player.getX() < getLandingSpacePosX() + Framework.frameWidth / 10 - (player.landerRocketWidth)/2)) {
                    if (player.getSpeedY() <= player.getMaxLandingSpeed())
                        player.setLanded(true);
                    else
                        player.setCrashed(true);
                } else
                    player.setCrashed(true);

                Framework.gameState = Framework.GameState.GAMEOVER;
            }
        }
        if(xSection>-1 && xSection<10){
        Rectangle ground=new Rectangle((int) ( xSection * ((double) Framework.frameWidth / 10)),  ((Framework.frameHeight - map[xSection]) ),  (Framework.frameWidth / 10 ),  ( map[xSection]));
        return rocketRectangle.intersects(ground);}
        else return true;
    }

    public int getLandingSpacePosX() {
        return (int) (landingSpacePos * ((double) Framework.frameWidth / 10));
    }

    public int getLandingSpacePosY() {
        return Framework.frameHeight - map[landingSpacePos]-15;
    }

    public int[] getMap() {
        return map;
    }

    public Rocket getPlayer() {
        return player;
    }
}
