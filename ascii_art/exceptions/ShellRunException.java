package ascii_art.exceptions;

/**
 * The ShellRunException class represents an exception that is thrown
 * when there is a general error during the run of the Shell.
 */
public class ShellRunException extends Exception {
    /**
     * Constructs a new ShellRunException with the specified detail message.
     *
     * @param message the detail message
     */
    public ShellRunException(String message) {
        super(message);
    }

    /**
     * Constructs a new ShellRunException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public ShellRunException(String message, Throwable cause) {
        super(message, cause);
    }
}
