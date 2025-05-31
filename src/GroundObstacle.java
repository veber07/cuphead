import java.awt.Rectangle;
import java.util.Random;
/**
 * The GroundObstacle class represents an obstacle that moves along the ground and can optionally jump
 *
 */
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
    /**
     * Construction
     *
     * @param x The initial X-coordinate of the obstacle.
     * @param y The initial Y-coordinate of the obstacle.
     * @param width The width of the obstacle.
     * @param height The height of the obstacle.
     * @param screenHeight The height of the game screen, used for calculating movement bounds.
     * @param playerHeight The base height of the player, used for determining jump target height.
     */
    public GroundObstacle(int x, int y, int width, int height, int screenHeight, int playerHeight) {
        super(x, y, width, height);
        this.initialY = y;
        this.screenHeight = screenHeight;
        this.playerHeight = playerHeight;
        calculateTargetY();
    }
    /**
     * Calculates the target Y-position for the obstacle when it jumps.

     */
    private void calculateTargetY() {
        this.targetY = screenHeight - GROUND_HEIGHT - (int) (playerHeight * 1.5);

        if (this.targetY < screenHeight / 2) {
            this.targetY = screenHeight / 2;
        }
    }
    /**
     * Updates the obstacle's position and jumping state.
     */
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
    /**
     * Checks if the obstacle is currently in a jumping or falling state.
     * @return True if the obstacle is jumping or falling, false otherwise.
     */
    public boolean isJumping() {
        return jumping || falling;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
        this.falling = !jumping;

    }
}