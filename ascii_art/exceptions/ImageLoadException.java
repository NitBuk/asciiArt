package ascii_art.exceptions;

/**
 * The ImageLoadException class represents an exception that is thrown
 * when there is an error related to loading an image in the ASCII art generation process.
 */
public class ImageLoadException extends Exception {
    /**
     * Constructs a new ImageLoadException with the specified detail message.
     *
     * @param message the detail message
     */
    public ImageLoadException(String message) {
        super(message);
    }
}
