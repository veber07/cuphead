import javax.swing.*;
import java.awt.*;
/**
 * Staeting whole code and game
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameLauncher());
    }

}