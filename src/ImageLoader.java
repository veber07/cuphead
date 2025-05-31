import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
/**
 *  class provides a utility method for loading images into BufferedImage objects.
 */
public class ImageLoader {
    /**
     * Loads an image from the specified path
     *
     * @param path The path to the image resource
     * @return A `BufferedImage` object representing the loaded image.
     */
    public static BufferedImage loadImage(String path) {
        try {

            return ImageIO.read(ImageLoader.class.getResourceAsStream(path));

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Chyba při načítání obrázku: " + path);
            return null;
        }
    }
}