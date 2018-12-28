package lunar_lander;

import java.awt.*;

public class Map {
    private int[] map; //Generating irregular shapes , every element has own height

    private int speedAccelerating; // Acceleration

    private int maxLandingSpeed; // Max speed for land

    private int fuel;

    private double speedY; // Vertical speed

    private double speedGrav; // Gravity

    private int landingSpacePos;


    public Map(int level) {
        loadWorld("resources/maps/map" + level + ".txt");
    }

    private void loadWorld(String path) {
        String file = Utils.loadFileAsString(path);
        String[] tokens = file.split("\\s+");
        speedAccelerating = Utils.parseInt(tokens[0]);
        speedY = Utils.parseInt(tokens[1]);
        speedGrav = (Utils.parseInt(tokens[2])) / (-10);   //Dividing to achieve negative double
        maxLandingSpeed = Utils.parseInt(tokens[3]);
        fuel = Utils.parseInt(tokens[4]);
        landingSpacePos = Utils.parseInt(tokens[5]);

        map = new int[10];
        for (int i = 0; i < map.length; i++) {
            map[i] = Utils.parseInt(tokens[i + 6]);
        }
    }

    public void draw(Graphics2D g2d) {
        double yScale=(double)Framework.frameHeight/720;
        double xScale=(double)Framework.frameWidth/1280;
        for (int i = 0; i < map.length; i++) {
            g2d.drawRect(i * Framework.frameWidth / 10, (int)((Framework.frameHeight-map[i])*yScale), (int)(Framework.frameWidth / 12*xScale), (int)(yScale*map[i]));
        }
    }

    public boolean checkCollision(Rocket rocket) {
        return false;
    }

    public int getLandingSpacePosX() {
        return landingSpacePos*(1/12)*Framework.frameWidth;
    }

    public int getLandingSpacePosY() {
        return map[getLandingSpacePosX()];
    }

    public int[] getMap() {
        return map;
    }
}
