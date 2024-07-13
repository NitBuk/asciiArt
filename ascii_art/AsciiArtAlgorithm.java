package ascii_art;

import image.Image;
import image.ImagePadding;
import image.ImageSplitter;
import image_char_matching.SubImgCharMatcher;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The AsciiArtAlgorithm class is responsible for generating ASCII art from an image.
 * It uses a given character set and resolution to produce a representation of the image
 * in ASCII characters.
 */
public class AsciiArtAlgorithm {
    private final Image image;
    private final int resolution;
    private final SubImgCharMatcher charMatcher;
    private final Map<Color[][], Double> brightnessCache = new HashMap<>();

    /**
     * Constructs an AsciiArtAlgorithm with the given image path, resolution, and character set.
     *
     * @param imagePath the path to the image file
     * @param resolution the number of characters per row in the resulting ASCII art
     * @param charset the character set to use for the ASCII art
     * @throws IOException if there is an error loading the image
     */
    public AsciiArtAlgorithm(String imagePath, int resolution, Set<Character> charset) throws IOException {
        this.image = new Image(imagePath);
        this.resolution = resolution;
        this.charMatcher = new SubImgCharMatcher(charset);
    }

    /**
     * Runs the ASCII art generation algorithm.
     * Pads the image, splits it into sub-images, calculates the brightness of each sub-image,
     * and maps each sub-image to the best matching character from the character set.
     *
     * @return a 2D array of characters representing the ASCII art
     */
    public char[][] run() {
        // Step 1: Padding the image
        Image paddedImage = ImagePadding.padImage(image);

        // Step 2: Dividing the image into sub-images
        int subImageSize = paddedImage.getWidth() / resolution;
        List<Color[][]> subImages = ImageSplitter.splitImage(paddedImage, subImageSize);

        // Step 3: Convert sub-images to characters
        char[][] asciiArt = new char[paddedImage.getHeight() /
                subImageSize][paddedImage.getWidth() / subImageSize];
        int index = 0;
        for (int y = 0; y < asciiArt.length; y++) {
            for (int x = 0; x < asciiArt[y].length; x++) {
                Color[][] subImage = subImages.get(index++);
                double brightness = getBrightness(subImage);
                asciiArt[y][x] = charMatcher.getCharByImageBrightness(brightness);
            }
        }
        return asciiArt;
    }

    /**
     * Gets the brightness of a given sub-image.
     * Uses a cache to avoid recalculating brightness for sub-images that have already been processed.
     *
     * @param subImage the sub-image to calculate the brightness for
     * @return the brightness value of the sub-image
     */
    private double getBrightness(Color[][] subImage) {
        if (brightnessCache.containsKey(subImage)) {
            return brightnessCache.get(subImage);
        } else {
            double brightness = ImageSplitter.calculateBrightness(subImage);
            brightnessCache.put(subImage, brightness);
            return brightness;
        }
    }
}
