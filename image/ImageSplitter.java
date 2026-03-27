package image;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Splits padded images into square tiles and measures tile brightness.
 */
public class ImageSplitter {

    /**
     * Splits the image into square sub-images.
     *
     * @param image input image
     * @param subImageSize tile size in pixels
     * @return tiles in row-major order
     */
    public static List<Color[][]> splitImage(Image image, int subImageSize) {
        List<Color[][]> subImages = new ArrayList<>();

        for (int y = 0; y < image.getHeight(); y += subImageSize) {
            for (int x = 0; x < image.getWidth(); x += subImageSize) {
                Color[][] subImage = new Color[subImageSize][subImageSize];
                for (int i = 0; i < subImageSize; i++) {
                    for (int j = 0; j < subImageSize; j++) {
                        if (y + i < image.getHeight() && x + j < image.getWidth()) {
                            subImage[i][j] = image.getPixel(y + i, x + j);
                        } else {
                            subImage[i][j] = Color.WHITE;
                        }
                    }
                }
                subImages.add(subImage);
            }
        }

        return subImages;
    }

    /**
     * Calculates normalized brightness for a tile.
     *
     * @param subImage tile pixels
     * @return brightness in the range {@code [0, 1]}
     */
    public static double calculateBrightness(Color[][] subImage) {
        double totalBrightness = 0;
        int totalPixels = subImage.length * subImage[0].length;

        for (Color[] row : subImage) {
            for (Color pixel : row) {
                double greyPixel = pixel.getRed() * 0.2126 + pixel.getGreen() * 0.7152
                        + pixel.getBlue() * 0.0722;
                totalBrightness += greyPixel;
            }
        }

        return totalBrightness / (totalPixels * 255); // Normalized to 0-1
    }
}
