
/**
 * The GameManag class  manages the overall state of the game -game is running, and determines win/loss conditions.
 */
public class GameManag {
    private boolean running = true;
    private String resultText = "";
    private boolean gameOverHandled = false;
    /**
     * Updates the game state based on the player's and boss's health and determines if the gsme has ended (win or lose).

     *
     * @param player The Player object.
     * @param boss The Boss object.
     */
    public void update(Player player, Boss boss) {
        if (!running) return;

        if (boss.getHealth() <= 0) {
            running = false;
            resultText = "You Win";
        }

        if (player.getHealth() <= 0) {
            running = false;
            resultText = "You Lose";
        }
    }

    public boolean isRunning() {
        return running;
    }

    public String getResultText() {
        return resultText;
    }
    /**
     * Resets the game manager to its initial state, allowing a new game to start.
     */
    public void reset() {
        running = true;
        resultText = "";
        gameOverHandled = false;
    }


    public boolean isGameOverHandled() {
        return gameOverHandled;
    }

    public void setGameOverHandled(boolean handled) {
        this.gameOverHandled = handled;
    }
}

