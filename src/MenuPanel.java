import javax.swing.*;
import javax.swing.*;
import java.awt.*;
/**
 * The MenuPanel class represents the main menu screen of the game.
 */
public class MenuPanel extends JPanel {
    private GameLauncher launcher;
    /**
     * Construction
     *
     * @param screenWidth The width of the screen.
     * @param screenHeight The height of the screen.
     * @param launcher The GameLauncher instance that manages game panels.
     */
    public MenuPanel(int screenWidth, int screenHeight, GameLauncher launcher) {
        this.launcher = launcher;
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        JButton playButton = new JButton("Play");
        JButton tutorialButton = new JButton("Tutorial");

        playButton.addActionListener(e -> launcher.startGame());
        tutorialButton.addActionListener(e -> launcher.showTutorial());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 0;
        add(playButton, gbc);

        gbc.gridy = 1;
        add(tutorialButton, gbc);
    }
}

