import java.awt.*;

public class Player {
    public float x, y;
    public float velocityX = 0;
    public float velocityY = 0;
    public boolean onGround = false;
    public int aimX = 1;
    public int aimY = 0;

    private final float GRAVITY = 0.5f;
    private final float JUMP_FORCE = -10f;
    private final float MOVE_SPEED = 3f;

    public int facingY = 0;
    public int facingX = 0;

    public int width = 80, height = 100;
    private int health = 100;

    public void update(boolean left, boolean right, boolean jump,boolean up, int groundY) {
        final int JUMP_FORCE = -10;
        velocityX = 0;
        if (left) {
            velocityX = -MOVE_SPEED;
            facingX = -1;
            aimX= -1;
            aimY = 0;
        } else if (right) {
            velocityX = MOVE_SPEED;
            facingX = 1;
            aimX= 1;
            aimY = 0;

        }



        if(up) {
            facingY = -1;
            aimY = -1;
        }else {
            aimY = 0;
        }

        if (!left && !right) {
            facingX = 0;
        }

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
    }

    public void takeDamage(int dmg) {
        health -= dmg;
        if (health < 0) health = 0;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect((int)x, (int)y, width, height);

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
