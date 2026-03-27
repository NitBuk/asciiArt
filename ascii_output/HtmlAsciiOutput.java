package ascii_output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes ASCII art to a simple HTML file wrapped in a monospace <pre> block.
 */
public class HtmlAsciiOutput implements AsciiOutput {
    private final String fileName;
    private final String fontFamily;

    public HtmlAsciiOutput(String fileName, String fontFamily) {
        this.fileName = fileName;
        this.fontFamily = fontFamily;
    }

    @Override
    public void out(char[][] asciiArt) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html lang=\"en\">\n<head>\n");
            writer.write("<meta charset=\"utf-8\">\n");
            writer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n");
            writer.write("<title>ASCII Art</title>\n");
            writer.write("<style>body{margin:0;padding:24px;background:#f7f7f7;}"
                    + "pre{font-family:'" + escapeHtml(fontFamily)
                    + "',monospace;font-size:12px;line-height:1;white-space:pre;}"
                    + "</style>\n");
            writer.write("</head>\n<body>\n<pre>\n");
            for (char[] row : asciiArt) {
                writer.write(escapeHtml(new String(row)));
                writer.write('\n');
            }
            writer.write("</pre>\n</body>\n</html>\n");
        }
    }

    private String escapeHtml(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
