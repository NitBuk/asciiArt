package image;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The ImageSplitter class provides methods to split an image into sub-images
 * and to calculate the brightness of a given sub-image.
 */
public class ImageSplitter {

    /**
     * Splits the given image into sub-images of the specified size.
     * Each sub-image is a square of pixels, and sub-images are padded with white pixels if necessary.
     *
     * @param image the original image to be split
     * @param subImageSize the size of each sub-image (each sub-image will be subImageSize x subImageSize)
     * @return a list of sub-images, each represented as a 2D array of Colors
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
     * Calculates the brightness of a given sub-image.
     * The brightness is calculated by converting each pixel to a grayscale value
     * and then averaging these values over the entire sub-image.
     *
     * @param subImage the sub-image to calculate the brightness for
     * @return the normalized brightness value of the sub-image, between 0 and 1
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
