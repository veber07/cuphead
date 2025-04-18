import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(3);
        window.setResizable(false);
        window.setTitle("cup head");
        GamePanel gamePanel = new GamePanel(window);
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo((Component)null);
        window.setVisible(true);
    }
}