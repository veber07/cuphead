import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Key implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed,shootPressed, upViewPressed, escape;
    private boolean gameOver = false;

    @Override
    public void keyTyped(KeyEvent e) {

    }

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

}
