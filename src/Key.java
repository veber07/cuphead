import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 * The Key class implements KeyListener to handle keyboard input for player controls and game actions. It tracks the pressed state of various keys.
 *
 */
public class Key implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed,shootPressed, upViewPressed, escape;
    private boolean gameOver = false;

    @Override
    public void keyTyped(KeyEvent e) {

    }
    /**
     * Invoked when a key has been pressednd wets the corresponding boolean flag to true for the pressed key.

     * @param e The KeyEvent representing the key pressed event.
     */
    @Override
    public void keyPressed(KeyEvent e) {


        int code = e.getKeyCode();

        if (code == KeyEvent.VK_SPACE) upPressed = true;
        if (code == KeyEvent.VK_S) downPressed = true;
        if (code == KeyEvent.VK_A) leftPressed = true;
        if (code == KeyEvent.VK_D) rightPressed = true;
        if (code == KeyEvent.VK_P) shootPressed = true;
        if(code == KeyEvent.VK_W) upViewPressed = true;
        if(code == KeyEvent.VK_ESCAPE) escape =true;


    }
    /**
     * Invoked when a key has been released and sets the corresponding boolean flag to false for the released key.
     * @param e The KeyEvent representing the key released event.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_SPACE) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_P) shootPressed = false;
        if(code == KeyEvent.VK_W) upViewPressed = false;
        if (code == KeyEvent.VK_ESCAPE) escape = false;
    }
    public void resetKeys() {
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        shootPressed = false;
        upViewPressed = false;
        escape = false;
        System.out.println("Key states reset.");
    }

}
