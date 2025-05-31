import java.awt.*;
/**
 * The Platform class represents a stationary platform in the game.
 */
public class Platform {
    private int x, y, width, height;
    /**
     * Construction
     *
     * @param x The X-coordinate of the top-left corner of the platform.
     * @param y The Y-coordinate of the top-left corner of the platform.
     * @param width The width of the platform.
     * @param height The height of the platform.
     */
    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }



    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
}

