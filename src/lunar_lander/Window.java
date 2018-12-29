package lunar_lander;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import javax.swing.*;
import javax.swing.plaf.nimbus.State;


public class Window extends JFrame {

    private int windowHeight = 720;
    private int windowWidth = 1280;


    private Window() {

        URL iconURL = getClass().getResource("/lunar_lander/resources/img/icon.png");
        ImageIcon icon = new ImageIcon(iconURL);
        this.setIconImage(icon.getImage());
        this.setTitle("Lunar Lander");

        this.setSize(windowWidth, windowHeight); // 1280x720px
        this.setLocationRelativeTo(null); // Centered
        this.setResizable(false); // Resizable


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Stop thread when user exists frame

        this.setContentPane(new Framework());

        this.setVisible(true);

    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() { // Run thread

            public void run() {
                new Window();
            }
        });
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public int getWindowWidth() {
        return windowWidth;
    }
}