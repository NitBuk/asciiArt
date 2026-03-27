package ascii_art.exceptions;

/**
 * Signals that the requested input image could not be loaded.
 */
public class ImageLoadException extends Exception {
    /**
     * Creates a new exception with a helpful message.
     *
     * @param message error description
     */
    public ImageLoadException(String message) {
        super(message);
    }
}
