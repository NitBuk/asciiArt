package ascii_art.exceptions;

/**
 * Signals that the shell could not be started cleanly.
 */
public class ShellRunException extends Exception {
    /**
     * Creates a new exception with a helpful message.
     *
     * @param message error description
     */
    public ShellRunException(String message) {
        super(message);
    }

    /**
     * Creates a new exception with a helpful message and cause.
     *
     * @param message error description
     * @param cause underlying failure
     */
    public ShellRunException(String message, Throwable cause) {
        super(message, cause);
    }
}
