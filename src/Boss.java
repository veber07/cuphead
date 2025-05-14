import java.awt.*;
import java.util.ArrayList;

public class Boss {
   private float x, y;
   private int width = 90;
    private int height = 300;
    public int health = 300;
    private long lastShotTime = 0;
    private long shootCooldown = 1500;
    public BossStage stage = BossStage.STAGE_1;

    public Boss(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void takeDamage(int dmg) {
        health -= dmg;
        if (health < 0) health = 0;
        updateStage();
    }

    private void updateStage() {
        if (health <= 100) {
            stage = BossStage.STAGE_3;
        } else if (health <= 200) {
            stage = BossStage.STAGE_2;
        } else {
            stage = BossStage.STAGE_1;
        }
    }

    public void update(ArrayList<Bullet> bullets) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= shootCooldown) {
            Bullet bullet;
            switch (stage) {
                case STAGE_1:
                    bullet = new Bullet(x, y + height / 2, -1, 0, ProjectileType.BOSS_BALL);
                    bullet.setWidth(30);
                    bullet.setHeight(15);
                case STAGE_2:
                    bullets.add(new Bullet(x, y + height / 3, -1, 0, ProjectileType.BOSS_BALL));
                    bullets.add(new Bullet(x, y + 2 * height / 3, -1, -1, ProjectileType.BOSS_BALL));
                    break;
                case STAGE_3:
                    bullets.add(new Bullet(x, y + height / 4, -1, -1, ProjectileType.BOSS_BALL));
                    bullets.add(new Bullet(x, y + height / 2, -1, 0, ProjectileType.BOSS_BALL));
                    bullets.add(new Bullet(x, y + 3 * height / 4, -1, 1, ProjectileType.BOSS_BALL));
                    break;
            }
            lastShotTime = currentTime;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        g2.fillRect((int) x, (int) y, width, height);
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

    public void setShootCooldown(long shootCooldown) {
        this.shootCooldown = shootCooldown;
    }

    public long getLastShotTime() {
        return lastShotTime;
    }

    public void setLastShotTime(long lastShotTime) {
        this.lastShotTime = lastShotTime;
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
