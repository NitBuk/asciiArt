package ascii_art;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Small wrapper around standard input so the shell can be tested and
 * reused without depending on direct scanner usage in the main loop.
 */
public final class KeyboardInput {
    private static final BufferedReader READER =
            new BufferedReader(new InputStreamReader(System.in));

    private KeyboardInput() {
    }

    /**
     * Reads a single line from standard input.
     *
     * @return the next line, or {@code null} on end-of-stream
     * @throws IOException if the input stream cannot be read
     */
    public static String readLine() throws IOException {
        return READER.readLine();
    }
}
