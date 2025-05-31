import java.awt.*;
import java.awt.image.BufferedImage;
/**
 * The Player class represents the playable character in the game.

 */
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
    private BufferedImage playerIdle;
    private BufferedImage playerWalk1;
    private BufferedImage playerWalk2;
    private BufferedImage playerJump;
    private BufferedImage playerCrouch;


    public int facingY = 0;
    public int facingX = 0;

    public int width;
    private  int height;
    private int health = 3;
    private long animationTimer = 0;
    private int animationFrame = 0;
    private final long ANIMATION_SPEED = 150;
    /**
     * Construction
     *
     * @param screenWidth The width of the game screen, used for initial positioning and scaling.
     * @param screenHeight The height of the game screen, used for initial positioning and scaling.
     */
    public Player(int screenWidth, int screenHeight) {

        this.width = screenWidth / 20;
        this.height = screenHeight / 10;





        this.x = Math.max(screenWidth / 10, 0);
        this.y = Math.max(screenHeight - (screenHeight / 18) - this.height, 0);
        this.baseHeight = this.height;

        System.out.println("DEBUG: Player width: " + this.width + ", height: " + this.height);
        loadPlayerImages();
    }
    /**
     * Loads the image assets for the player's various animations.
     */
    private void loadPlayerImages() {
try {


    playerIdle = ImageLoader.loadImage("/res/brr.png");
    playerWalk1 = ImageLoader.loadImage("/res/brrgo.png");
    playerWalk2 = ImageLoader.loadImage("/res/brrgo2.png");
    playerJump = ImageLoader.loadImage("/res/brrjump.png");
    playerCrouch = ImageLoader.loadImage("/res/brrcrouch.png");
}catch (Exception e) {
    System.out.println("nenacetli se obrazky");
}

    }
    /**
     * Updates the player's state, including movement, aiming, crouching,jumping, and gravity applicationand loading its animation images.
     * @Auhor ChatGPT
     * @param left True if the left movement key is pressed.
     * @param right True if the right movement key is pressed.
     * @param jump True if the jump key is pressed.
     * @param down True if the down/crouch key is pressed.
     * @param up True if the up/aim up key is pressed.
     * @param groundY The Y-coordinate of the ground level, used for collision with the floor.
     */
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
        animationTimer += 1000 / 60;
        if (animationTimer >= ANIMATION_SPEED) {
            animationFrame = (animationFrame + 1) % 2;
            animationTimer = 0;
        }

        if (invincible && System.currentTimeMillis() - lastHitTime >= 3000) {
            invincible = false;
        }

    }
    /**
     * Reduces the player's health by the specified damage amount and The player becomes temporarily invincible after taking damage.

     *
     * @param dmg The amount of damage to take.
     */
    public void takeDamage(int dmg) {
        if (!invincible) {
            health = health- dmg;
            if (health < 0) health = 0;
            invincible = true;
            lastHitTime = System.currentTimeMillis();
        }
    }
    /**
     * * Draws the player character on the provided Graphics2D context
     *
     * @param g2 The Graphics2D object to draw on.
     */
    public void draw(Graphics2D g2) {
        BufferedImage currentImage = playerIdle;

        if (isCrouching()) {
            currentImage = playerCrouch;
        } else if (!isOnGround()) {
            currentImage = playerJump;
        } else if (velocityX != 0) {
            if (animationFrame == 0) {
                currentImage = playerWalk1;
            } else {
                currentImage = playerWalk2;
            }
        }

        RenderingHints originalHints = g2.getRenderingHints();


        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }

        if (currentImage != null) {
            if (facingX == -1) {
                g2.drawImage(currentImage, (int) x + width, (int) y, -width, height, null);
            } else {
                g2.drawImage(currentImage, (int) x, (int) y, width, height, null);
            }
        } else {
            g2.setColor(Color.BLUE);
            g2.fillRect((int)x, (int)y, width, height);
        }

        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }


        g2.setRenderingHints(originalHints);


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
