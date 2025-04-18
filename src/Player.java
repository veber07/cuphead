import java.awt.*;

public class Player {
    public float x, y;
    public float velocityX = 0;
    public float velocityY = 0;
    public boolean onGround = false;

    private final float GRAVITY = 0.5f;
    private final float JUMP_FORCE = -10f;
    private final float MOVE_SPEED = 3f;

    public int width = 40, height = 50;

    public void update(boolean left, boolean right, boolean jump, int groundY) {
        if (left) velocityX = -MOVE_SPEED;
        else if (right) velocityX = MOVE_SPEED;
        else velocityX = 0;

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
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect((int)x, (int)y, width, height);
    }
}
