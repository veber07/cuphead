import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
    ArrayList<Bullet>bullets = new ArrayList<>();
    boolean shootPressed = false;
    long lastShootTime = 0;
    long shootCooldown = 500;
    Boss boss = new Boss(1000, 100);
    private final GameManag gameStateManager = new GameManag();



    Thread gameThread;
    Key keyHandler;



    public GamePanel(JFrame window,int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.FPS = 60;
        this.setPreferredSize(new Dimension(this.screenWidth, this.screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        spawnStage2Platforms();


        this.player = new Player();
        this.player.x = 100;
        this.player.y = 100;
        this.keyHandler = new Key();
        this.addKeyListener(this.keyHandler);
        this.startGameThread();


    }

    private ArrayList<Platform> platforms = new ArrayList<>();

    private void spawnStage2Platforms() {
        int ground = 300;
        platforms.clear();


        platforms.add(new Platform(400, ground - 150, 120, 20));
        platforms.add(new Platform(600, ground - 250, 120, 20));
        platforms.add(new Platform(800, ground - 180, 120, 20));
    }

    public void startGameThread() {
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    public void run() {
        double drawInterval = 1000000000.0 / FPS;

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
        gameStateManager.update(player, boss);
        if (!gameStateManager.isRunning()) return;

        boss.setY(getHeight() - 50 - boss.getHeight());
        boss.update(bullets);

        if (boss.getStage() == BossStage.STAGE_2 && platforms.isEmpty()) {
            spawnStage2Platforms();
        }

        int groundY = this.getHeight() - 50;
        player.update(keyHandler.leftPressed, keyHandler.rightPressed, keyHandler.upPressed, keyHandler.upViewPressed, groundY);

        for (Platform platform : platforms) {
            Rectangle playerBounds = new Rectangle((int)player.getX(), (int)player.getY(), player.getWidth(), player.getHeight());
            Rectangle platBounds = platform.getBounds();

            if (playerBounds.intersects(platBounds) && player.getVelocityY() >= 0) {
                player.setY(platform.getBounds().y - player.getHeight());
                player.setVelocityY(0);
                player.setOnGround(true);
            }
        }

        if (keyHandler.shootPressed) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShootTime >= shootCooldown) {
                int shootDirX = player.facingX;
                int shootDirY = keyHandler.upViewPressed ? -1 : 0;
                if (shootDirX == 0 && shootDirY == 0) {
                    shootDirX = 1;
                }
                bullets.add(new Bullet(player.x + player.width, player.y + player.height / 2, shootDirX, shootDirY, ProjectileType.PLAYER_BULLET));
                lastShootTime = currentTime;
            }
        }

        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.update();

            if (bullet.getX() > getWidth() || bullet.getX() < 0 || bullet.getY() < 0 || bullet.getY() > getHeight()) {
                bulletsToRemove.add(bullet);
                continue;
            }

            if (bullet.getType() == ProjectileType.PLAYER_BULLET &&
                    bullet.bulletHit(boss.getX(), boss.getY(), boss.getWidth(), boss.getHeight())) {
                boss.takeDamage(bullet.getType().getDamage());
                bulletsToRemove.add(bullet);
                continue;
            }

            if (bullet.getType() == ProjectileType.BOSS_BALL &&
                    bullet.bulletHit(player.x, player.y, player.width, player.height)) {
                player.takeDamage(bullet.getType().getDamage());
                bulletsToRemove.add(bullet);
            }
        }

        bullets.removeAll(bulletsToRemove);
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.GREEN);
        g2.fillRect(0, this.getHeight() - 50, this.getWidth(), 50);
        for (Bullet bullet : bullets) {
            bullet.draw(g2);
        }
        boss.draw(g2);
        player.draw(g2);
        g2.setColor(Color.WHITE);
        for (Platform platform : platforms) {
            System.out.println("Platform: x=" + platform.getBounds().x + ", y=" + platform.getBounds().y + ", width=" + platform.getBounds().width + ", height=" + platform.getBounds().height);

            platform.draw(g2);
        }
        System.out.println("Počet platforem: " + platforms.size());
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        g2.drawString("Player HP: " + player.getHealth(), 20, 40);
        g2.drawString("Boss HP: " + boss.getHealth(), 20, 70);
        g2.drawString("Boss Stage: " + boss.getStage(), 20, 100);
        if (!gameStateManager.isRunning()) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 72));
            FontMetrics fm = g2.getFontMetrics();
            String text = gameStateManager.getResultText();
            int textWidth = fm.stringWidth(text);
            g2.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 2);
        }
        g2.dispose();
    }

    public int getScreenWidth() {
        return this.screenWidth;
    }

    public int getScreenHeight() {
        return this.screenHeight;
    }
}


