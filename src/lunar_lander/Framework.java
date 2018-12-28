package lunar_lander;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;


@SuppressWarnings("serial")
public class Framework extends Control {

    public static int frameWidth;

    public static int frameHeight;

    public static final long SECINNANO = 1000000000L;

    public static final long MILISECINNANO = 1000000L;

    private final double GAME_FPS = 30; // Lower value = easy; Higher value = hard

    private final double GAME_UPDATE = SECINNANO / GAME_FPS;

    public static enum GameState {
        STARTING, DISPLAY, MENU, RUNNING, GAMEOVER, PAUSE
    }

    public static GameState gameState;

    private double gameTime;

    private double lastTime;


    private Main game;


    private BufferedImage lunarLander;

    private BufferedImage lunarLanderPause;

    public Framework() {
        super();

        gameState = GameState.DISPLAY;

        Thread gameThread = new Thread() {

            public void run() {
                gameloop();
            }
        };
        gameThread.start();


    }

    private void loadcontent() {
        try {

            URL lunarLanderUrl = this.getClass().getResource("/lunar_lander/resources/img/menu.png");
            lunarLander = ImageIO.read(lunarLanderUrl);

            URL lunarLanderUrl2 = this.getClass().getResource("/lunar_lander/resources/img/pause.png");
            lunarLanderPause = ImageIO.read(lunarLanderUrl2);
        } catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null,
                    ex);
        }

    }

    private void gameloop() {
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();

        long beginTime,
                timeTaken;

        double timeLeft;

        while (true) {
            beginTime = System.nanoTime();

            switch (gameState) {
                case RUNNING:
                    gameTime += System.nanoTime() - lastTime;

                    game.UpdateGame(gameTime, mousePosition());

                    lastTime = System.nanoTime();

                    break;
                case GAMEOVER:
                    break;
                case MENU:
                    break;
                case PAUSE:
                    if(Control.keyboardKeyState(KeyEvent.VK_ENTER))Framework.gameState= GameState.RUNNING;
                    break;
                case STARTING:

                    loadcontent();

                    gameState = GameState.MENU;
                    break;
                case DISPLAY:
                    if (this.getWidth() > 1 && visualizingTime > SECINNANO) {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        gameState = GameState.STARTING;
                    } else {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                    break;
            }

            repaint();

            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE - timeTaken) / MILISECINNANO;

            if (timeLeft < 10)
                timeLeft = 10;
            try {
                Thread.sleep((long) timeLeft);
            } catch (InterruptedException ex) {
            }
        }
    }

    public void draw(Graphics2D g2d) {
        switch (gameState) {
            case RUNNING:
                game.draw(g2d, mousePosition());
                break;
            case GAMEOVER:
                game.drawgameover(g2d, mousePosition(), gameTime);
                break;
            case MENU:
                g2d.drawImage(lunarLander, 0, 0, frameWidth, frameHeight, null);
                break;
            case PAUSE:
                g2d.drawImage(lunarLanderPause, 0, 0, frameWidth, frameHeight, null);

        }
    }

    private void newGame() {
        gameTime = 0;
        lastTime = System.nanoTime();

        game = new Main();


    }

    private void restartGame() {
        gameState = GameState.MENU;

    }

    private Point mousePosition() {
        try {
            Point mp = this.getMousePosition();

            if (mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        } catch (Exception e) {
            return new Point(0, 0);
        }
    }

    public void keyReleasedFramework(KeyEvent e) {
        switch (gameState) {
            case MENU:
                newGame();
                break;
            case GAMEOVER:
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    restartGame();
                break;
        }
    }

    public void mouseClicked(MouseEvent e) {

    }
}