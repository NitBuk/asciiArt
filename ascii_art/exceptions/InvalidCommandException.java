package ascii_art.exceptions;

/**
 * The InvalidCommandException class represents an exception that is thrown
 * when an invalid command is encountered in the ASCII art generation process.
 */
public class InvalidCommandException extends Exception {
    /**
     * Constructs a new InvalidCommandException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
