import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;



public class BossGraphicsManager {

    private Map<BossStage, BufferedImage> bossImages;

    /**
     * Konstruktor BossGraphicsManager.
     * Načítá všechny obrázky bosse pro různé fáze.
     */
    public BossGraphicsManager() {
        bossImages = new HashMap<>();
        loadBossImages();
    }

    /**
     * Načítá obrázky bosse z určených cest.
     */
    private void loadBossImages() {
        // Cesty k vašim obrázkům bosse (upravte podle skutečného umístění)
        bossImages.put(BossStage.STAGE_1, ImageLoader.loadImage("/res/tungtung (1).png"));
        bossImages.put(BossStage.STAGE_2, ImageLoader.loadImage("/res/sharko (1).png"));
        bossImages.put(BossStage.STAGE_3, ImageLoader.loadImage("/res/crocodilo (1).png"));


    }

    /**
     * Vykreslí aktuální obrázek bosse na základě jeho fáze.
     *
     * @param g2 Grafický kontext pro vykreslování.
     * @param stage Aktuální fáze bosse.
     * @param x X-souřadnice bosse.
     * @param y Y-souřadnice bosse.
     * @param width Šířka bosse.
     * @param height Výška bosse.
     */
    public void drawBoss(Graphics2D g2, BossStage stage, float x, float y, int width, int height) {
        BufferedImage currentBossImage = bossImages.get(stage);

        if (currentBossImage != null) {
            g2.drawImage(currentBossImage, (int) x, (int) y, width, height, null);
        } else {

            System.err.println("Obrázek pro fázi bosse " + stage + " nenalezen! Vykresluji výchozí.");
            g2.setColor(Color.MAGENTA); // Výchozí barva pro nouzové vykreslení
            g2.fillRect((int) x, (int) y, width, height);
        }
    }

}