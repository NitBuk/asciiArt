package ascii_output;

import java.io.IOException;

/**
 * Common contract for rendering an ASCII-art matrix to a destination.
 */
public interface AsciiOutput {
    /**
     * Renders the provided character matrix.
     *
     * @param asciiArt the art to render
     * @throws IOException if the output destination cannot be written
     */
    void out(char[][] asciiArt) throws IOException;
}
