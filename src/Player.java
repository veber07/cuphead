import java.awt.*;

public class Player {
    public float x, y;
    public float velocityX = 0;
    public float velocityY = 0;
    public boolean onGround = false;
    public int aimX = 1;
    public int aimY = 0;
   private boolean crouching = false;
    private long lastHitTime = 0;
    private boolean invincible = false;
    private final int baseHeight;

    private final float GRAVITY = 0.5f;
    private final float JUMP_FORCE = -10f;
    private final float MOVE_SPEED = 5f;


    public int facingY = 0;
    public int facingX = 0;

    public int width;
    private  int height;
    private int health = 100;

    public Player(int screenWidth, int screenHeight) {

        this.width = screenWidth / 20;
        this.height = screenHeight / 8;


        this.x = Math.max(screenWidth / 10, 0);
        this.y = Math.max(screenHeight - (screenHeight / 18) - this.height, 0);
        this.baseHeight = screenHeight / 10;
        this.height = baseHeight;
    }

    public void update(boolean left, boolean right, boolean jump,boolean down, boolean up, int groundY) {
        final int JUMP_FORCE = -10;
        velocityX = 0;
        velocityX = 0;

        if (left) {
            velocityX = -MOVE_SPEED;
            facingX = -1;
        } else if (right) {
            velocityX = MOVE_SPEED;
            facingX = 1;
        }

        if (up) {
            aimX = 0;
            aimY = -1;
        } else if (left) {
            aimX = -1;
            aimY = 0;
        } else if (right) {
            aimX = 1;
            aimY = 0;
        } else {
            aimX = facingX;
            aimY = facingY;
        }

        crouching = down;
        if (crouching) {
            height = baseHeight / 2;
        } else {
          height = baseHeight;

        }
       // if (!left && !right) {
       //     facingX = 0;
     //   }
        if (jump && onGround) {
            velocityY = JUMP_FORCE;
            onGround = false;
        }
        velocityY += GRAVITY;
        x += velocityX;
        y += velocityY;
        if (y + height >= groundY) {
            y = groundY - height;
            velocityY = 0;
            onGround = true;
        } else   {
            onGround = false;
            
        }
        if (invincible && System.currentTimeMillis() - lastHitTime >= 3000) {
            invincible = false;
        }

    }

    public void takeDamage(int dmg) {
        if (!invincible) {
            health = health- dmg;
            if (health < 0) health = 0;
            invincible = true;
            lastHitTime = System.currentTimeMillis();
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect((int)x, (int)y, width, height);
        //System.out.println("Player: x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);

    }

    public boolean isInvincible() {
        return invincible;
    }

    public boolean isCrouching() {
        return crouching;
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

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public int getAimX() {
        return aimX;
    }

    public void setAimX(int aimX) {
        this.aimX = aimX;
    }

    public int getAimY() {
        return aimY;
    }

    public void setAimY(int aimY) {
        this.aimY = aimY;
    }

    public float getGRAVITY() {
        return GRAVITY;
    }

    public float getJUMP_FORCE() {
        return JUMP_FORCE;
    }

    public float getMOVE_SPEED() {
        return MOVE_SPEED;
    }

    public int getFacingY() {
        return facingY;
    }

    public void setFacingY(int facingY) {
        this.facingY = facingY;
    }

    public int getFacingX() {
        return facingX;
    }

    public void setFacingX(int facingX) {
        this.facingX = facingX;
    }

    public int getWidth() {
        return width;
    }

    public int getBaseHeight() {
        return baseHeight;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
