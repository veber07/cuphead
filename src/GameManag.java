public class GameManag {
    private boolean running = true;
    private String resultText = "";

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
    }
}
