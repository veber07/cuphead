public class GameManag {
    private boolean running = true;
    private String resultText = "";
    private boolean gameOverHandled = false;

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

