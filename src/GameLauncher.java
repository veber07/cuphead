import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
/**
 * The GameLauncher class is responsible for initializing and managing the main game window,
 * switching between different game panels (main menu, game, tutorial), and handling full-screen mode.
 */
public class GameLauncher {
    private JFrame window;
    private int screenWidth;
    private int screenHeight;
    /**
     * Construction
     */
    public GameLauncher() {
        setupWindow();
        showMainMenu();
    }
    /**
     * Configures the main JFrame for the game, including full-screen modeand default close operation, and window listeners for cleanup.

     */
    private void setupWindow() {
        window = new JFrame("My Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setUndecorated(true);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode dm = gd.getDisplayMode();
        screenWidth = dm.getWidth();
        screenHeight = dm.getHeight();
        System.out.println(dm.getHeight() + " " + dm.getWidth());

        window.setSize(screenWidth, screenHeight);
        window.setLocationRelativeTo(null);

        gd.setFullScreenWindow(window);
        window.setVisible(true);

        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                StageStorage.deleteStageFile();
                gd.setFullScreenWindow(null);
            }
        });
    }

    /**
     * Displays the main menu panel in the game window.
     */
    public void showMainMenu() {
        MenuPanel menuPanel = new MenuPanel(screenWidth, screenHeight, this);
        window.setContentPane(menuPanel);
        window.revalidate();
        window.repaint();
    }
    /**
     * Starts the game by switching the window's content to the GamePanel.
     */
    public void startGame() {
        GamePanel gamePanel = new GamePanel(this, screenWidth, screenHeight);
        window.setContentPane(gamePanel);
        window.revalidate();
        gamePanel.requestFocusInWindow();
    }

    /**
     * Displays the tutorial panel with game controls and instructions.
     */
    public void showTutorial() {
        JPanel tutorialPanel = new JPanel();
        tutorialPanel.setBackground(Color.DARK_GRAY);
        tutorialPanel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea(
                "OVLÁDÁNÍ:\n\n" +
                        "A/D - Pohyb vlevo/vpravo\n" +
                        "W - Míření nahoru\n" +
                        "S - Přískok / Přikrčení\n" +
                        "SPACE - Skok\n" +
                        "P - Střelba\n\n" +
                        "Stiskni ESC pro návrat do menu"
        );
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 24));
        textArea.setEditable(false);
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(Color.DARK_GRAY);

        tutorialPanel.add(textArea, BorderLayout.CENTER);

        window.setContentPane(tutorialPanel);
        window.revalidate();
        tutorialPanel.requestFocusInWindow();

        tutorialPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "backToMenu");
        tutorialPanel.getActionMap().put("backToMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });

    }

}