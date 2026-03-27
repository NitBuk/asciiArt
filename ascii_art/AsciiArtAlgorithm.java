package ascii_art;

import image.Image;
import image.ImagePadding;
import image.ImageSplitter;
import image_char_matching.SubImgCharMatcher;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Converts an image into a character matrix by padding it, splitting it into
 * square tiles, and matching each tile to the closest character brightness.
 */
public class AsciiArtAlgorithm {
    private final Image image;
    private final int resolution;
    private final SubImgCharMatcher charMatcher;

    /**
     * Constructs a new conversion pipeline.
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
     * Runs the conversion pipeline.
     *
     * @return the resulting character matrix
     */
    public char[][] run() {
        Image paddedImage = ImagePadding.padImage(image);
        int subImageSize = paddedImage.getWidth() / resolution;
        List<Color[][]> subImages = ImageSplitter.splitImage(paddedImage, subImageSize);

        char[][] asciiArt = new char[paddedImage.getHeight() /
                subImageSize][paddedImage.getWidth() / subImageSize];
        int index = 0;
        for (int y = 0; y < asciiArt.length; y++) {
            for (int x = 0; x < asciiArt[y].length; x++) {
                Color[][] subImage = subImages.get(index++);
                double brightness = ImageSplitter.calculateBrightness(subImage);
                asciiArt[y][x] = charMatcher.getCharByImageBrightness(brightness);
            }
        }
        return asciiArt;
    }
}
