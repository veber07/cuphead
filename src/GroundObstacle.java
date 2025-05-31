import java.awt.Rectangle;
import java.util.Random;

public class GroundObstacle extends Rectangle {
    private int initialY;
    private int targetY;
    private boolean jumping = false;
    private boolean falling = false;
    private boolean hasJumped = false;
    private static final int JUMP_SPEED = 8;
    private static final double JUMP_CHANCE = 0.005;
    private Random random = new Random();

    private int playerHeight;
    private int screenHeight;
    private static final int GROUND_HEIGHT = 50;

    public GroundObstacle(int x, int y, int width, int height, int screenHeight, int playerHeight) {
        super(x, y, width, height);
        this.initialY = y;
        this.screenHeight = screenHeight;
        this.playerHeight = playerHeight;
        calculateTargetY();
    }

    private void calculateTargetY() {
        this.targetY = screenHeight - GROUND_HEIGHT - (int) (playerHeight * 1.5);

        if (this.targetY < screenHeight / 2) {
            this.targetY = screenHeight / 2;
        }
    }

    public void update() {
        x -= 4;

        if (jumping) {
            y -= JUMP_SPEED;
            if (y <= targetY) {
                y = targetY;
                jumping = false;
                falling = true;
            }
        } else if (falling) {
            y += JUMP_SPEED;
            if (y >= initialY) {
                y = initialY;
                falling = false;
                hasJumped = true;
            }
        } else {

            if (!hasJumped && random.nextDouble() < JUMP_CHANCE) {
                jumping = true;
            }
        }
    }

    public boolean isJumping() {
        return jumping || falling;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
        this.falling = !jumping;

    }
}