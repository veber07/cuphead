import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    final int originalTitleSize = 16;
    final int scale = 3;
    public int finaTitlesize = 48;
    final int maxScreenUp = 16;
    final int maxScreenLeft = 12;
    final int screenWidth;
    final int screenHeight;
    final int FPS;

    Player player;
    boolean leftPressed = false;
    boolean rightPressed = false;
    boolean jumpPressed = false;

    Thread gameThread;


    public GamePanel(JFrame window) {
        this.screenWidth = this.finaTitlesize * 16;
        this.screenHeight = this.finaTitlesize * 12;
        this.FPS = 60;
        this.setPreferredSize(new Dimension(this.screenWidth, this.screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);

        this.startGameThread();
        this.player = new Player();
        this.player.x = 100;
        this.player.y = 100;

    }

    public void startGameThread() {
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    public void run() {
        double drawInterval = 1.6666666E7;

        for(double nextDrawTime = (double)System.nanoTime() + drawInterval; this.gameThread != null; nextDrawTime += drawInterval) {
            this.update();
            this.repaint();

            try {
                double remainingTime = nextDrawTime - (double)System.nanoTime();
                remainingTime /= 1000000.0;
                if (remainingTime < 0.0) {
                    remainingTime = 0.0;
                }

                Thread.sleep((long)remainingTime);
            } catch (InterruptedException var7) {
                Thread.currentThread().interrupt();
            }
        }

    }

    public void update() {


    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.GREEN);
        g2.fillRect(0, this.getHeight() - 50, this.getWidth(), 50);

        player.draw(g2);
        g2.dispose();

    }

    public int getScreenWidth() {
        return this.screenWidth;
    }

    public int getScreenHeight() {
        return this.screenHeight;
    }
}


