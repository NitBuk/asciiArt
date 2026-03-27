package image;

import java.awt.Color;

/**
 * Pads an image with white borders until both dimensions are powers of two.
 */
public class ImagePadding {

    /**
     * Returns a centered, padded copy of the input image.
     *
     * @param image original image
     * @return padded image
     */
    public static Image padImage(Image image) {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        int newWidth = getNextPowerOfTwo(originalWidth);
        int newHeight = getNextPowerOfTwo(originalHeight);

        Color[][] newPixelArray = new Color[newHeight][newWidth];

        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                newPixelArray[i][j] = Color.WHITE;
            }
        }

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
     * Returns the next power of two greater than or equal to {@code n}.
     */
    private static int getNextPowerOfTwo(int n) {
        if (n <= 0) return 1;
        return (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
    }
}
