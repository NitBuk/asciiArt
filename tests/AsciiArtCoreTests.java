package tests;

import image.Image;
import image.ImagePadding;
import image.ImageSplitter;

import java.awt.Color;
import java.util.List;

/**
 * Lightweight verification for the deterministic image helpers.
 *
 * Run with: {@code make test} or {@code java -cp build/classes tests.AsciiArtCoreTests}
 */
public final class AsciiArtCoreTests {
    private AsciiArtCoreTests() {
    }

    public static void main(String[] args) {
        testPadImageCentersAndExpandsToPowerOfTwo();
        testCalculateBrightnessIsNormalized();
        testSplitImageUsesRowMajorTileOrder();
        System.out.println("All asciiArt tests passed.");
    }

    private static void testPadImageCentersAndExpandsToPowerOfTwo() {
        Color[][] pixels = new Color[][]{
                {Color.RED, Color.GREEN, Color.BLUE},
                {Color.CYAN, Color.MAGENTA, Color.YELLOW}
        };
        Image image = new Image(pixels, 3, 2);

        Image padded = ImagePadding.padImage(image);

        assertEquals(4, padded.getWidth(), "padded width");
        assertEquals(2, padded.getHeight(), "padded height");
        assertEquals(Color.RED, padded.getPixel(0, 0), "leftmost pixel preserved");
        assertEquals(Color.BLUE, padded.getPixel(0, 2), "rightmost pixel preserved");
        assertEquals(Color.CYAN, padded.getPixel(1, 0), "bottom-left pixel preserved");
    }

    private static void testCalculateBrightnessIsNormalized() {
        Color[][] pixels = new Color[][]{
                {Color.WHITE, Color.BLACK},
                {Color.BLACK, Color.WHITE}
        };

        double brightness = ImageSplitter.calculateBrightness(pixels);

        assertClose(0.5, brightness, 1e-9, "normalized brightness");
    }

    private static void testSplitImageUsesRowMajorTileOrder() {
        Color[][] pixels = new Color[4][4];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                pixels[row][col] = new Color(row * 40, col * 40, (row + col) * 20);
            }
        }
        Image image = new Image(pixels, 4, 4);

        List<Color[][]> tiles = ImageSplitter.splitImage(image, 2);

        assertEquals(4, tiles.size(), "tile count");
        assertEquals(image.getPixel(0, 0), tiles.get(0)[0][0], "top-left tile origin");
        assertEquals(image.getPixel(0, 2), tiles.get(1)[0][0], "top-right tile origin");
        assertEquals(image.getPixel(2, 0), tiles.get(2)[0][0], "bottom-left tile origin");
        assertEquals(image.getPixel(3, 3), tiles.get(3)[1][1], "bottom-right tile corner");
    }

    private static void assertEquals(Object expected, Object actual, String label) {
        if (!expected.equals(actual)) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
    }

    private static void assertEquals(int expected, int actual, String label) {
        if (expected != actual) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
    }

    private static void assertClose(double expected, double actual, double tolerance, String label) {
        if (Math.abs(expected - actual) > tolerance) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
    }
}
