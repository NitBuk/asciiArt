package ascii_art.exceptions;

/**
 * The ResolutionChangeException class represents an exception that is thrown
 * when there is an error related to changing the resolution in the ASCII art generation process.
 */
public class ResolutionChangeException extends Exception {
    /**
     * Constructs a new ResolutionChangeException with the specified detail message.
     *
     * @param message the detail message
     */
    public ResolutionChangeException(String message) {
        super(message);
    }
}
