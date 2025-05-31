import java.awt.*;

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
    }

    public void update() {
        x += dx;
        y += dy;
    }
    public boolean isOutOfScreen(int screenWidth, int screenHeight) {
        return x < 0 || x > screenWidth || y < 0 || y > screenHeight;
    }

    public boolean bulletHit(float targetX, float targetY, int targetWidth, int targetHeight) {
        Rectangle bulletRect = new Rectangle((int) x, (int) y, bulletWidht, bulletHeight);
        Rectangle targetRect = new Rectangle((int) targetX, (int) targetY, targetWidth, targetHeight);

        return bulletRect.intersects(targetRect);
    }

    public void draw(Graphics2D g2) {
        if (type == ProjectileType.LASER_SHORT) {
            g2.setColor(Color.CYAN);
        } else if (type == ProjectileType.LASER_LONG) {
            g2.setColor(Color.BLUE);
        } else {
            g2.setColor(Color.ORANGE);
        }
        g2.fillRect((int) x, (int) y, bulletWidht, bulletHeight);
        System.out.println("Kulka vykreslena na pozici  x=" + x + ", y=" + y);

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
