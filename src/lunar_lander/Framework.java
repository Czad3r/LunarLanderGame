package lunar_lander;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Framework extends Control {

    public static int frameWidth;

    public static int frameHeight;

    public static final long SECINNANO = 1000000000L; //How much nanoseconds have second

    public static final long MILISECINNANO = 1000000L; //How much miliseconds have second

    private final double GAME_FPS = 30; // Lower value = easy; Higher value = hard

    private final double GAME_UPDATE = SECINNANO / GAME_FPS;

    public static enum GameState {
        STARTING, DISPLAY, MENU, RUNNING, GAMEOVER, PAUSE
    }

    public static GameState gameState;

    private double gameTime; // Field to calculate every result

    private double lastTime;

    private boolean recordFlag = false; // After every game flag is true for one loop, to prevent from adding multiple same record

    private boolean healthFlag = false; // After every game flag is true for one loop, to prevent from losing multiple health

    private Main game;

    private static ArrayList<Double> recordList;

    private JTable table;

    private JPanel panel;

    private BufferedImage lunarLander;

    public Framework() {
        super();
        initializeTable();
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                Framework.frameWidth = getWidth();
                Framework.frameHeight = getHeight();
            }
        });

        gameState = GameState.DISPLAY;

        Thread gameThread = new Thread() {

            public void run() {
                gameloop();
            }
        };
        gameThread.start();

    }

    private void initializeTable() {
        initializeList();

        TableModel dataModel = new AbstractTableModel() {
            public int getColumnCount() {
                return 1;
            }

            public int getRowCount() {
                return 10;
            }

            public Object getValueAt(int row, int col) {
                return recordList.get(row);
            }

            @Override
            public String getColumnName(int column) {
                return "Records [s]";
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };


        table = new JTable(dataModel);
        table.setEnabled(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        this.setLayout(new BorderLayout());
        panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(table.getTableHeader());
        panel.add(table);
        this.add(panel, BorderLayout.EAST);
    }

    private void initializeList() {
        recordList = Utils.loadRecords();
    }

    public static void addRecord(double record) {
        if (record < recordList.get(9)) {
            recordList.set(9, record);
            recordList.sort(Double::compareTo);
            Utils.saveRecords(recordList);
        }
    }

    private void loadcontent() {
        try {

            URL lunarLanderUrl = this.getClass().getResource("/lunar_lander/resources/img/menu.png");
            lunarLander = ImageIO.read(lunarLanderUrl);

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

                    recordFlag = true;
                    healthFlag = true;
                    break;
                case GAMEOVER:
                    if (recordFlag && game.isPlayerLanded()) {
                        Framework.addRecord(gameTime / Framework.SECINNANO);
                        recordFlag = false;
                        if (Main.getLevel() == 7)
                            Main.setLevel(1); //Loop to level 1, if you want more levels, just increment it
                        else Main.setLevel(Main.getLevel() + 1);
                        initializeList();
                    }
                    if (healthFlag && game.isPlayerCrashed()) {
                        game.playerLostHealth();
                        healthFlag = false;
                    }
                    break;
                case MENU:
                    break;
                case PAUSE:
                    if (Control.keyboardKeyState(KeyEvent.VK_ENTER)) Framework.gameState = GameState.RUNNING;
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
                panel.setVisible(false);
                break;
            case GAMEOVER:
                game.drawgameover(g2d, mousePosition(), gameTime);

                break;
            case MENU:
                g2d.drawImage(lunarLander, 0, 0, frameWidth, frameHeight, null);
                panel.setVisible(true);
                break;
            case PAUSE:
                game.draw(g2d, mousePosition());
                g2d.drawString("PAUSE", frameWidth / 2, frameHeight / 2);
                g2d.drawString("Press enter to continue", frameWidth / 2, frameHeight / 2 + 10);
                panel.setVisible(true);

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