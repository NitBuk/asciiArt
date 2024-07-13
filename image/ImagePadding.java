package image;

import java.awt.Color;

/**
 * The ImagePadding class provides a method to pad an image with white pixels
 * so that its dimensions are powers of two.
 */
public class ImagePadding {

    /**
     * Pads the given image with white pixels so that its dimensions are powers of two.
     *
     * @param image the original image to be padded
     * @return a new image with dimensions padded to the nearest power of two
     */
    public static Image padImage(Image image) {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        int newWidth = getNextPowerOfTwo(originalWidth);
        int newHeight = getNextPowerOfTwo(originalHeight);

        Color[][] newPixelArray = new Color[newHeight][newWidth];

        // Initialize with white pixels
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                newPixelArray[i][j] = Color.WHITE;
            }
        }

        // Copy original pixels to the center of the new image
        int xOffset = (newWidth - originalWidth) / 2;
        int yOffset = (newHeight - originalHeight) / 2;

        for (int y = 0; y < originalHeight; y++) {
            for (int x = 0; x < originalWidth; x++) {
                newPixelArray[y + yOffset][x + xOffset] = image.getPixel(y, x);
            }
        }

        return new Image(newPixelArray, newWidth, newHeight);
    }

    /**
     * Returns the next power of two greater than or equal to the given number.
     *
     * @param n the number to find the next power of two for
     * @return the next power of two greater than or equal to n
     */
    private static int getNextPowerOfTwo(int n) {
        if (n <= 0) return 1;
        return (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
    }
}
