package ascii_art.exceptions;

/**
 * Signals a bad resolution command or an invalid resolution transition.
 */
public class ResolutionChangeException extends Exception {
    /**
     * Creates a new exception with a helpful message.
     *
     * @param message error description
     */
    public ResolutionChangeException(String message) {
        super(message);
    }
}
