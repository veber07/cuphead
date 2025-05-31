import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
/**
 * The GamePanel class represents the main game panel where all game elements are rendered and updated
 * It extends JPanel and implements Runnable to manage the game loop.
 */
public class GamePanel extends JPanel implements Runnable {
    final int originalTitleSize = 16;
    final int scale = 3;
    public int finaTitlesize = 48;
    final int maxScreenUp = 16;
    final int maxScreenLeft = 12;
    final int screenWidth;
    final int screenHeight;
    final int FPS;
    private GameLauncher launcher;

    Player player;
    boolean leftPressed = false;
    boolean rightPressed = false;
    boolean jumpPressed = false;
    private boolean paused = false;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    boolean shootPressed = false;
    long lastShootTime = 0;
    long shootCooldown = 500;
    Boss boss;

    private final GameManag gameStateManager = new GameManag();
    private boolean escapeHandledOnce = false;
    private JButton playAgainButton;
    private JButton quitButton;


    Thread gameThread;
    Key keyHandler;

    /**
     * Constructs a new GamePanel.
     *
     * @param launcher The GameLauncher instance that created this panel.
     * @param screenWidth The width of the game screen.
     * @param screenHeight The height of the game screen.
     */
    public GamePanel(GameLauncher launcher, int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.FPS = 60;
        this.launcher = launcher;
        this.setPreferredSize(new Dimension(this.screenWidth, this.screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.player = new Player(screenWidth, screenHeight);
        this.player.x = 100;
        this.player.y = 100;
        this.boss = new Boss(0, 0, screenWidth, screenHeight,player);

        InputMap im = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = this.getActionMap();
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "none");
        am.put("none", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                }
        );



        this.keyHandler = new Key();
        this.addKeyListener(this.keyHandler);
        this.startGameThread();

        BossStage loadedStage = StageStorage.loadStage();
        if (loadedStage != null) {
            boss.initializeStage(loadedStage);
        } else {
            boss.initializeStage(BossStage.STAGE_1);
        }









}



    /**
     * Starts the main game loop thread.
     */
    public void startGameThread() {
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }
    /**
     * The main game loop. This method is called when the gameThread starts.
     * It handles game updates and rendering at a fixed FPS.
     */
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();
            double frameDelta = (currentTime - lastTime) / drawInterval;

            if (frameDelta <= 0 || frameDelta > 2) frameDelta = 1;
            delta += frameDelta;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * Updates the game state, including player, boss, bullets, and collisions.
     * This method is called repeatedly within the game loop.
     */
    public void update() {
        if (paused) return;
        gameStateManager.update(player, boss);

        if (keyHandler.escape) {
            keyHandler.escape = false;
            if (!gameStateManager.isRunning()) return;

            paused = true;

            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            int result = JOptionPane.showOptionDialog(
                    topFrame,
                    "Co chceš udělat?",
                    "Pauza",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Pokračovat", "Menu", "Odejít"},
                    "Pokračovat"
            );

            switch (result) {
                case 0:
                    paused = false;
                    break;
                case JOptionPane.CLOSED_OPTION:
                    paused = false;
                    break;
                case 1: // Menu
                    StageStorage.saveStage(boss.getStage());
                    MenuPanel menuPanel = new MenuPanel(this.screenWidth, this.screenHeight,launcher);
                    topFrame.setContentPane(menuPanel);
                    topFrame.revalidate();
                    topFrame.repaint();
                    menuPanel.requestFocusInWindow();
                    break;
                case 2: //
                    System.exit(0);
                    break;
            }
        }
        if (!gameStateManager.isRunning()) {
            if (!gameStateManager.isGameOverHandled()) {
                gameStateManager.setGameOverHandled(true);
                paused = true;

                this.removeKeyListener(keyHandler);

                createAndShowGameOverButtons();
            }
            return;
        }



        if (!gameStateManager.isRunning()) return;

       // boss.setY(getScreenHeight() - 50 - boss.getHeight());
        boss.update(bullets, player, getScreenWidth(), getScreenHeight());




        int groundY = getScreenHeight() - 50;
        player.update(keyHandler.leftPressed, keyHandler.rightPressed,keyHandler.upPressed,keyHandler.downPressed, keyHandler.upViewPressed, groundY);

        for (Platform platform : boss.getPlatforms()) {
            Rectangle playerBounds = new Rectangle((int)player.getX(), (int)player.getY(), player.getWidth(), player.getHeight());
            Rectangle platBounds = platform.getBounds();

            if (playerBounds.intersects(platBounds) && player.getVelocityY() >= 0) {
                player.setY(platform.getBounds().y - player.getHeight());
                player.setVelocityY(0);
                player.setOnGround(true);
            }
        }
        for (Bullet bullet : bullets) {
            bullet.update();
        }


       /* ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : new ArrayList<>(bullets))
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

            if (bullet.getTypeMain() == ProjectileTypeMain.BOSS_BULLET &&
                    bullet.bulletHit(player.x, player.y, player.width, player.getHeight())) {
                player.takeDamage(bullet.getType().getDamage());
                bulletsToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsToRemove);*/



        for (Rectangle r : boss.getGroundObstacles()) {
            if (r.intersects(new Rectangle((int)player.getX(), (int)player.getY(), player.getWidth(), player.getHeight()))) {
                player.takeDamage(15);
            }
        }
        if (keyHandler.shootPressed) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShootTime >= shootCooldown) {


                int shootDirX = player.getAimX();
                int shootDirY = player.getAimY();



                if (shootDirX == 0 && shootDirY == 0) {
                    shootDirX = player.getFacingX() != 0 ? player.getFacingX() : 1; // Střílejte ve směru, kam je hráč otočen, nebo doprava jako default
                }


                bullets.add(new Bullet(
                        player.getX() + player.getWidth() / 2,
                        player.getY() + player.getHeight() / 2,
                        shootDirX * 10, // Rychlost střely
                        shootDirY * 10, // Rychlost střely
                        ProjectileType.PLAYER_BULLET,
                        ProjectileTypeMain.PLAYER_BULLET,
                        10,
                        10));

                lastShootTime = currentTime;
            }
        }



        for (Bullet bullet : bullets) {
            bullet.update();
        }


        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : new ArrayList<>(bullets)) {
            if (bulletsToRemove.contains(bullet)) {
                continue;
            }

            if (bullet.getTypeMain() == ProjectileTypeMain.PLAYER_BULLET &&
                    bullet.bulletHit(boss.getX(), boss.getY(), boss.getWidth(), boss.getHeight())) {

                System.out.println("COLISION DEBUG: Kulka HRÁČE zasáhla BOSSE! Boss HP před: " + boss.getHealth());
                boss.takeDamage(bullet.getType().getDamage());
                System.out.println("COLISION DEBUG: Boss HP po: " + boss.getHealth() + ". Kulka odstraněna.");
                bulletsToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsToRemove);


        bulletsToRemove.clear();
        for (Bullet bullet : new ArrayList<>(bullets)) {

            if (bulletsToRemove.contains(bullet)) {
                continue;
            }

            if (bullet.getTypeMain() == ProjectileTypeMain.BOSS_BULLET &&
                    bullet.bulletHit(player.getX(), player.getY(), player.getWidth(), player.getHeight())) {

                System.out.println("COLISION DEBUG: Kulka BOSSE zasáhla HRÁČE! Player HP před: " + player.getHealth());
                player.takeDamage(bullet.getType().getDamage());
                System.out.println("COLISION DEBUG: Player HP po: " + player.getHealth() + ". Kulka odstraněna.");
                bulletsToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsToRemove);


//
        Rectangle playerBounds = new Rectangle((int)player.getX(), (int)player.getY(), player.getWidth(), player.getHeight());
        Rectangle bossBounds = new Rectangle((int)boss.getX(), (int)boss.getY(), boss.getWidth(), boss.getHeight());

        if (playerBounds.intersects(bossBounds)) {
            System.out.println("COLISION DEBUG: HRÁČ narazil do těla BOSSE! Player HP před: " + player.getHealth());
            player.takeDamage(5);
            System.out.println("COLISION DEBUG: Player HP po: " + player.getHealth());


            if (player.getX() < boss.getX()) {
                player.setX(boss.getX() - player.getWidth());
            } else {
                player.setX(boss.getX() + boss.getWidth());
            }
        }


        for (Rectangle r : boss.getGroundObstacles()) {
            if (playerBounds.intersects(r)) {
                System.out.println("COLISION DEBUG: HRÁČ zasáhl pozemní překážku!");
                player.takeDamage(15);

            }
        }





        /*if (keyHandler.shootPressed) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShootTime >= shootCooldown) {
                int shootDirX = player.getFacingX();
                int shootDirY = keyHandler.upViewPressed ? -1 : 0;
                if (shootDirX == 0 && shootDirY == 0) {
                    shootDirX = 1;
                }
                bullets.add(new Bullet(
                        player.getX() + player.getWidth() / 2,
                        player.getY() + player.getHeight() / 2,
                        shootDirX * 10,
                        shootDirY * 10,
                        ProjectileType.PLAYER_BULLET,
                        ProjectileTypeMain.PLAYER_BULLET,
                        10,
                        10));

                lastShootTime = currentTime;
            }
        }/*

        /*ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
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

            if (bullet.getTypeMain() == ProjectileTypeMain.BOSS_BULLET  &&
                    bullet.bulletHit(player.x, player.y, player.width, player.height)) {
                System.out.println("hrac byl hitnut");
                player.takeDamage(bullet.getType().getDamage());
                bulletsToRemove.add(bullet);
            }

        }


        bullets.removeAll(bulletsToRemove);
        Rectangle playerBounds = new Rectangle((int)player.getX(), (int)player.getY(), player.getWidth(), player.getHeight());
        Rectangle bossBounds = new Rectangle((int)boss.getX(), (int)boss.getY(), boss.getWidth(), boss.getHeight());

        if (playerBounds.intersects(bossBounds)) {
            player.takeDamage(10);
            if (player.getX() < boss.getX()) {
                player.setX(boss.getX() - player.getWidth());
            } else {
                player.setX(boss.getX() + boss.getWidth());
            }
        }*/
    }
    /**
     * Paints the game components on the panel.
     *
     * @param g The Graphics object to draw on.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        double scaleX = getWidth() / (double) screenWidth;
        double scaleY = getHeight() / (double) screenHeight;
        g2.scale(scaleX, scaleY);
        g2.setColor(Color.GREEN);
        g2.fillRect(0, screenHeight - 50, screenWidth, 50);
        synchronized (bullets) {
            for (Bullet bullet : bullets) {
                if (bullet != null) {
                    bullet.draw(g2);
                }
            }
        }
        boss.draw(g2);
        player.draw(g2);
        g2.setColor(Color.WHITE);

        if (boss.getStage() == BossStage.STAGE_2) {
            g2.setColor(Color.MAGENTA);
            for (Rectangle r : boss.getGroundObstacles()) {
                g2.fill(r);
            }
        }
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
            g2.drawString(text, (screenWidth - textWidth) / 2, screenHeight / 2);
        }
        g2.dispose();
    }  /**
     * Resets the game to its initial stateand allowing the player to start a new game.
     */
    private void resetGame() {

        this.addKeyListener(keyHandler);


        player = new Player(screenWidth, screenHeight);
        player.x = 100;
        player.y = 100;
        boss = new Boss(0, 0, screenWidth, screenHeight, player);
        BossStage loadedStage = StageStorage.loadStage();
        if (loadedStage != null) {
            boss.initializeStage(loadedStage);
        } else {
            boss.initializeStage(BossStage.STAGE_1);
        }
        bullets.clear();
        lastShootTime = 0;
        gameStateManager.reset();

        removeGameOverButtons();

    }
    /**
     * Creates and displays the Play Againand Quit buttons when the game ends.
     */
    private void createAndShowGameOverButtons() {
        int buttonWidth = 200;
        int buttonHeight = 60;
        int buttonX = (screenWidth - buttonWidth) / 2;
        int buttonYStart = screenHeight / 2 + 20;

        playAgainButton = new JButton("Hrát znovu");
        playAgainButton.setFont(new Font("Arial", Font.BOLD, 30));
        playAgainButton.setBounds(buttonX, buttonYStart, buttonWidth, buttonHeight);
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
                paused = false;
                GamePanel.this.requestFocusInWindow();
            }
        });
        this.add(playAgainButton);

        quitButton = new JButton("Odejít");
        quitButton.setFont(new Font("Arial", Font.BOLD, 30));
        quitButton.setBounds(buttonX, buttonYStart + buttonHeight + 20, buttonWidth, buttonHeight);
        quitButton.addActionListener(new  ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.add(quitButton);

        this.revalidate();
        this.repaint();
    }
    /**
     * Removes the Play Again and Quit buttons from the panel.
     */

    private void removeGameOverButtons() {
        if (playAgainButton != null) {
            this.remove(playAgainButton);
            playAgainButton = null;
        }
        if (quitButton != null) {
            this.remove(quitButton);
            quitButton = null;
        }
        this.revalidate();
        this.repaint();
    }


    public int getScreenWidth() {
        return this.screenWidth;
    }
    public int getScreenHeight() {
        return this.screenHeight;
    }
}


