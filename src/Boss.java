import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;
/**
 * The Boss class represents the boss entity in the game,stages,healt, attacks, and interactions with the player and game environment.
 */
public class Boss {
    private ArrayList<Platform> platforms = new ArrayList<>();
    private boolean platformsSpawnedForStage2 = false;

    private float x, y;
    private int width = 150;
    private int height = 450;
    public int health = 300;
    private long lastShotTime = 0;
    private long shootCooldown = 1500;
    public BossStage stage = BossStage.STAGE_1;
    private boolean longLaserActive = false;
    private long longLaserStartTime = 0;
    private static final long LONG_LASER_DURATION = 3000;
    // private boolean movingUp = false; // Tato proměnná se nepoužívá, lze odstranit
    private ArrayList<Rectangle> groundObstacles = new ArrayList<>(); // Toto zůstává ArrayList<Rectangle>
    private static final int GROUND_HEIGHT = 50;
    private int stage2PlatformY = -1;
    Player player;

    private int screenWidth;
    private int screenHeight;

    private long shootInterval = 1500;


    private long lastObstacleSpawnTime = 0;
    private static final long OBSTACLE_SPAWN_INTERVAL = 500;

    /**
     *constructor.
     *
     * @param x The initial X-coordinate of the boss.
     * @param y The initial Y-coordinate of the boss.
     * @param screenWidth The width of the game screen.
     * @param screenHeight The height of the game screen.
     * @param player A reference to the Player object.
     */
    public Boss(float x, float y, int screenWidth, int screenHeight, Player player) {
        this.x = x;
        this.y = y;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.player = player;
        initializeStage(this.stage);
    }
    /**
     * Initializes the boss to a new stage, resetting health, platforms, and obstacles.
     *
     * @param newStage The BossStage to initialize to.
     */
    public void initializeStage(BossStage newStage) {
        this.stage = newStage;

        platforms.clear();
        groundObstacles.clear();
        platformsSpawnedForStage2 = false;

        switch (newStage) {
            case STAGE_1:
                this.health = 300;
                break;
            case STAGE_2:
                this.health = 200;

                if (!platformsSpawnedForStage2) {
                    spawnStage2Platforms(this.screenWidth, this.screenHeight);
                    platformsSpawnedForStage2 = true;
                }
                lastObstacleSpawnTime = System.currentTimeMillis();
                break;
            case STAGE_3:
                this.health = 100;
                break;
            default:
                this.health = 300;
                break;
        }
    }

    /**
     * Reduces the boss's health by the specified damage amount and updates the boss's stage if health drops below limit.
     * @param dmg The amount of damage to take.
     */
    public void takeDamage(int dmg) {
        health -= dmg;
        if (health < 0) health = 0;
        updateStage();
    }
    /**
     * Moves the boss to the bottom-right corner of the screen.
     *
     * @param screenWidth The width of the game screen.
     * @param screenHeight The height of the game screen.
     */
    private void moveToBottomRight(int screenWidth, int screenHeight) {
        this.x = screenWidth - this.width;
        this.y = screenHeight - GROUND_HEIGHT - this.height;
    }
    /**
     * The boss shoots short and long lasers towards the player's Y-position.
     *
     * @param player The Player object.
     * @param bullets The ArrayList of bullets to add new boss projectiles to.
     */
    private void stage1Attack(Player player, ArrayList<Bullet> bullets) {
        long now = System.currentTimeMillis();

        if (longLaserActive) {
            if (now - longLaserStartTime >= LONG_LASER_DURATION) {
                longLaserActive = false;
            }
            return;
        }

        if (Math.random() < 0.2 && now - lastShotTime >= shootInterval * 2) {
            float laserStartX = this.x;
            float laserYOffset = player.getHeight() / 4f;
            float laserY = player.getY() + (Math.random() < 0.5 ? laserYOffset : player.getHeight() - laserYOffset - 40);

            int laserLongWidth = 500;
            int laserLongHeight = 40;

            bullets.add(new Bullet(laserStartX, laserY, -10, 0,
                    ProjectileType.LASER_LONG, ProjectileTypeMain.BOSS_BULLET,
                    laserLongWidth, laserLongHeight));
            longLaserActive = true;
            longLaserStartTime = now;
            lastShotTime = now;
            return;
        }

        if (now - lastShotTime >= shootInterval) {
            float laserStartX = this.x;
            float laserYOffset = player.getHeight() / 4f;
            float laserY = player.getY() + (Math.random() < 0.5 ? laserYOffset : player.getHeight() - laserYOffset - 20);

            int laserShortWidth = 300;
            int laserShortHeight = 20;

            bullets.add(new Bullet(laserStartX, laserY, -10, 0,
                    ProjectileType.LASER_SHORT, ProjectileTypeMain.BOSS_BULLET,
                    laserShortWidth, laserShortHeight));
            lastShotTime = now;
        }
    }
    /**
     *
     * Spawns ground obstacles at regular intervals.
     *
     * @param bullets The ArrayList of bullets (not directly used in Stage 2 attack logic, but passed).
     * @param groundObstacles The ArrayList of ground obstacles to add new obstacles to.
     * @param screenWidth The width of the game screen.
     * @param screenHeight The height of the game screen.
     */
    public void stage2Attack(ArrayList<Bullet> bullets, ArrayList<Rectangle> groundObstacles, int screenWidth, int screenHeight) {
        long now = System.currentTimeMillis();


        if (now - lastObstacleSpawnTime >= OBSTACLE_SPAWN_INTERVAL) {
            int obstacleWidth = (int)(screenWidth * 0.08);
            int obstacleHeight = (int)(screenHeight * 0.05);

            groundObstacles.add(new GroundObstacle(screenWidth, screenHeight - GROUND_HEIGHT - obstacleHeight,
                    obstacleWidth, obstacleHeight, screenHeight,stage2PlatformY));
            lastObstacleSpawnTime = now;
        }
        updateGroundObstacles();
    }
    /**
     * Checks for collisions between the player and any active ground obstacles.
     *
     * @param player The Player object to check collision with.
     */
    public void checkPlayerObstacleCollision(Player player) {
        Rectangle playerRect = new Rectangle((int)player.getX(), (int)player.getY(), player.getWidth(), player.getHeight());

        Iterator<Rectangle> iter = groundObstacles.iterator();
        while (iter.hasNext()) {
            Rectangle r = iter.next();
            if (r instanceof GroundObstacle) {
                GroundObstacle go = (GroundObstacle) r;
                if (playerRect.intersects(go.getBounds())) {

                    if (go.isJumping() && player.isOnGround()) {
                        player.takeDamage(10);
                        go.setJumping(false);
                    } else if (!go.isJumping() && player.isOnGround()) {

                        player.takeDamage(5);
                    }
                }
            }
        }
    }
    /**
     * Updates the boss's stage based on its current health.
     * Resets  platforms, obstacles  specific to each stage change.
     */
    private void updateStage() {
        if (health <= 100) {
            stage = BossStage.STAGE_3;
            if (!platforms.isEmpty()) {
                platforms.clear();
            }
            groundObstacles.clear();
            platformsSpawnedForStage2 = false;
        } else if (health <= 200) {
            if (stage != BossStage.STAGE_2) {
                stage = BossStage.STAGE_2;
                if (!platformsSpawnedForStage2) {
                    spawnStage2Platforms(this.screenWidth, this.screenHeight);
                    platformsSpawnedForStage2 = true;
                }
                lastObstacleSpawnTime = System.currentTimeMillis();
            }
        } else {
            stage = BossStage.STAGE_1;
            if (!platforms.isEmpty()) {
                platforms.clear();
            }
            groundObstacles.clear();
            platformsSpawnedForStage2 = false;
        }
    }
    /**
     * Spawns a set of platforms
     * These platforms are positioned relative to the player's standing height.
     *
     * @param screenWidth The width of the game screen.
     * @param screenHeight The height of the game screen.
     */
    public void spawnStage2Platforms(int screenWidth, int screenHeight) {
        platforms.clear();

        int platformWidth = (int) (screenWidth * 0.15);
        int platformHeight = (int) (screenHeight * 0.02);

        int playerStandingHeight = player.getBaseHeight();

        int commonPlatformY = screenHeight - GROUND_HEIGHT - (int) (playerStandingHeight * 1.5);

        if (commonPlatformY < screenHeight / 2) {
            commonPlatformY = screenHeight / 2;
        }
        if (commonPlatformY > screenHeight - GROUND_HEIGHT - platformHeight - 10) {
            commonPlatformY = screenHeight - GROUND_HEIGHT - platformHeight - 10;
        }
        this.stage2PlatformY = commonPlatformY;


        int horizontalSpacing = (int) (screenWidth * 0.08);
        int startX = (int) (screenWidth * 0.1);


        platforms.add(new Platform(startX, commonPlatformY, platformWidth, platformHeight));
        platforms.add(new Platform(startX + horizontalSpacing + platformWidth, commonPlatformY, platformWidth, platformHeight));
        platforms.add(new Platform(startX + 2 * (horizontalSpacing + platformWidth), commonPlatformY, platformWidth, platformHeight));

        System.out.println("DEBUG: Stage 2 platforms spawned at fixed height.");
        for (Platform p : platforms) {
            Rectangle b = p.getBounds();
            System.out.printf("DEBUG: Platform: x=%d, y=%d, width=%d, height=%d%n",
                    b.x, b.y, b.width, b.height);
        }
    }
    /**
     * Updates the position and state of all active ground obstacles.
     * Removes obstacles that move off-screen to the left.
     */
    private void updateGroundObstacles() {
        Iterator<Rectangle> iter = groundObstacles.iterator();
        while (iter.hasNext()) {
            Rectangle r = iter.next();

            if (r instanceof GroundObstacle) {
                ((GroundObstacle) r).update();
            }

            if (r.x + r.width < 0) {
                iter.remove();
            }
        }
    }

    private long lastSideShotTime = 0;
    private long sideShotCooldown = 2500;
    /**
     * The boss shoots large balls from the top and spread projectiles from the right side.
     *
     * @param player The Player object.
     * @param bullets The ArrayList of bullets to add new boss projectiles to.
     * @param screenWidth The width of the game screen.
     */

    private void stage3Attack(Player player, ArrayList<Bullet> bullets, int screenWidth) {
        long now = System.currentTimeMillis();

        if (now - lastShotTime >= shootInterval) {
            float spawnXOffset = (float) (Math.random() * player.getWidth() * 2) - player.getWidth();
            float bulletX = player.getX() + player.getWidth() / 2 + spawnXOffset;
            float bulletY = 0;
            bullets.add(new Bullet(
                    bulletX,
                    bulletY,
                    0,
                    10,
                    ProjectileType.BOSS_BIG_BALL,
                    ProjectileTypeMain.BOSS_BULLET,
                    40,
                    40
            ));
            lastShotTime = now;
        }

        if (now - lastSideShotTime >= sideShotCooldown) {
            float bulletX = screenWidth - 1;
            float bulletY = player.getY() + player.getHeight() / 2;
            bullets.add(new Bullet(
                    bulletX,
                    bulletY,
                    -10,
                    0,
                    ProjectileType.BOSS_SPREAD,
                    ProjectileTypeMain.BOSS_BULLET,
                    20,
                    10
            ));
            lastSideShotTime = now;
        }
    }
    /**
     * Updates the boss's state, including its position and initiating attacksbased on its current stage
     *
     * @param bullets The ArrayList of bullets in the game.
     * @param player The Player object.
     * @param screenWidth The width of the game screen.
     * @param screenHeight The height of the game screen.
     */
    public void update(ArrayList<Bullet> bullets, Player player, int screenWidth, int screenHeight) {
        switch (stage) {
            case STAGE_1:
                moveToBottomRight(screenWidth, screenHeight);
                stage1Attack(player, bullets);
                if (!platforms.isEmpty()) {
                    platforms.clear();
                    platformsSpawnedForStage2 = false;
                }
                groundObstacles.clear();
                break;
            case STAGE_2:
                moveToBottomRight(screenWidth, screenHeight);
                stage2Attack(bullets, groundObstacles, screenWidth, screenHeight);
                checkPlayerObstacleCollision(player);
                break;
            case STAGE_3:
                moveToTopRight(screenWidth);
                stage3Attack(player, bullets, screenWidth);
                if (!platforms.isEmpty()) {
                    platforms.clear();
                    platformsSpawnedForStage2 = false;
                }
                groundObstacles.clear();
                break;
        }
    }
    /**
     * Moves the boss to the top-right corner of the screen.
     *
     * @param screenWidth The width of the game screen.
     */
    private void moveToTopRight(int screenWidth) {
        this.x = screenWidth - this.width;
        this.y = 0;
    }

    public ArrayList<Rectangle> getGroundObstacles() {
        return groundObstacles;
    }
    /**
     * Draws the boss, its platforms, and ground obstacles on the screen.
     *
     * @param g2 The Graphics2D object used for drawing.
     */
    public void draw(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        g2.fillRect((int) x, (int) y, width, height);

        for (Platform p : platforms) {
            p.draw(g2);
        }

        g2.setColor(Color.ORANGE);
        for (Rectangle r : groundObstacles) {
            g2.fillRect(r.x, r.y, r.width, r.height);
        }
    }

    public BossStage getStage() {
        return stage;
    }

    public void setStage(BossStage stage) {
        this.stage = stage;
    }

    public long getShootCooldown() {
        return shootCooldown;
    }

    public ArrayList<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(ArrayList<Platform> platforms) {
        this.platforms = platforms;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }
}