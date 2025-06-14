import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The Bullet class represents a projectile fired in the game.
 * It handles its position, movement, type, and collision detection.
 */
public class Bullet {
   private float x, y;
   private int dx, dy;
  private float speed = 10f;
   private int bulletWidht = 10;
    private int bulletHeight = 5;
  private int direction = 0;
    private int directionY = 0;
    private ProjectileType type;
    private ProjectileTypeMain typeMain;
    private BufferedImage bulletImage;


    /**
     * Construction
     *
     * @param x The initial X-coordinate of the bullet.
     * @param y The initial Y-coordinate of the bullet.
     * @param dx The horizontal velocity of the bullet.
     * @param dy The vertical velocity of the bullet.
     * @param type The specific ProjectileType of this bullet.
     * @param typeMain The main ProjectileTypeMain category of this bullet.
     * @param width The width of the bullet.
     * @param height The height of the bullet.
     */
    public Bullet(float x, float y, int dx, int dy, ProjectileType type, ProjectileTypeMain typeMain, int width, int height) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.type = type;
        this.typeMain = typeMain;
        this.bulletWidht = width;
        this.bulletHeight = height;
        System.out.println("    BULELTRD" + x + ", y=" + y + ", dx=" + dx + ", dy=" + dy + ", width=" + width + ", height=" + height);
        loadBulletImage();
    }
    private void loadBulletImage() {
        try {
            switch (type) {
                case LASER_SHORT:
                    bulletImage = ImageLoader.loadImage("/res/tungbullet.png");
                    break;
                case LASER_LONG:
                    bulletImage = ImageLoader.loadImage("/res/tungbullet.png");
                    break;
                case BOSS_BALL:
                    bulletImage = ImageLoader.loadImage("/res/tralalabullet.png");
                    break;
                case BOSS_BIG_BALL:
                    bulletImage = ImageLoader.loadImage("/res/bombaaCrocodilo.png");
                    break;
                case BOSS_SPREAD:
                    bulletImage = ImageLoader.loadImage("/res/bulletCrocodilooo.png");
                case PLAYER_BULLET:
                    bulletImage = ImageLoader.loadImage("/res/list.png");
                    break;
                default:
                    System.out.println("No specific image for bullet type: " + type + ", using default color.");

                    break;
            }
        } catch (Exception e) {
            System.err.println("Failed to load image for bullet type " + type + ": " + e.getMessage());
            bulletImage = null;
        }
    }
    /**
     * Updates the bullet's position based on its velocity.
     */
    public void update() {
        x += dx;
        y += dy;
    }

    /**
     * Checks if this bullet has collided with a target.
     *
     * @param targetX The X-coordinate of the target.
     * @param targetY The Y-coordinate of the target.
     * @param targetWidth The width of the target.
     * @param targetHeight The height of the target.
     * @return True if the bullet's bounding box intersects the target's bounding box, false otherwise.
     */
    public boolean bulletHit(float targetX, float targetY, int targetWidth, int targetHeight) {
        Rectangle bulletRect = new Rectangle((int) x, (int) y, bulletWidht, bulletHeight);
        Rectangle targetRect = new Rectangle((int) targetX, (int) targetY, targetWidth, targetHeight);

        return bulletRect.intersects(targetRect);
    }
    /**
     * Draws the bullet on the Graphics2D
     *
     * @param g2 The Graphics2D object to draw on.
     */
    public void draw(Graphics2D g2) {
        if (bulletImage != null) {
            g2.drawImage(bulletImage, (int) x, (int) y, bulletWidht, bulletHeight, null); // Vykreslení obrázku
        } else {
            // Původní kód pro vykreslení barevného obdélníku, pokud se obrázek nenačte
            if (type == ProjectileType.LASER_SHORT) {
                g2.setColor(Color.CYAN);
            } else if (type == ProjectileType.LASER_LONG) {
                g2.setColor(Color.BLUE);
            } else {
                g2.setColor(Color.ORANGE);
            }
            g2.fillRect((int) x, (int) y,bulletHeight, bulletHeight); // Opravený název proměnné
            System.out.println("Kulka vykreslena na pozici x=" + x + ", y=" + y + " (bez obrázku)");
        }

    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getBulletWidht() {
        return bulletWidht;
    }

    public void setBulletWidht(int bulletWidht) {
        this.bulletWidht = bulletWidht;
    }

    public int getHeight() {
        return bulletHeight;
    }

    public void setHeight(int height) {
        this.bulletHeight = height;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }





    public ProjectileType getType() {
        return type;
    }



    public ProjectileTypeMain getTypeMain() {
        return typeMain;
    }

    public void setTypeMain(ProjectileTypeMain typeMain) {
        this.typeMain = typeMain;
    }
}
