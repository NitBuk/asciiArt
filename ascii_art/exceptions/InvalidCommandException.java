package ascii_art.exceptions;

/**
 * Signals a malformed or unsupported shell command.
 */
public class InvalidCommandException extends Exception {
    /**
     * Creates a new exception with a helpful message.
     *
     * @param message error description
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
