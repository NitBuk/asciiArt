package ascii_output;

import java.io.IOException;

/**
 * Writes ASCII art directly to standard output.
 */
public class ConsoleAsciiOutput implements AsciiOutput {
    @Override
    public void out(char[][] asciiArt) throws IOException {
        for (char[] row : asciiArt) {
            System.out.println(new String(row));
        }
    }
}
