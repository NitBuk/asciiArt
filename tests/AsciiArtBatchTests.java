package tests;

import ascii_art.BatchMode;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Lightweight verification for the non-interactive batch workflow.
 */
public final class AsciiArtBatchTests {
    private AsciiArtBatchTests() {
    }

    public static void main(String[] args) throws Exception {
        testParseArgsReadsBatchOptions();
        testRunWritesHtmlAndReturnsAsciiMatrix();
        testMissingImageReturnsNonZeroExitCode();
        System.out.println("All asciiArt batch tests passed.");
    }

    private static void testParseArgsReadsBatchOptions() throws Exception {
        Path imagePath = Files.createTempFile("ascii-art-batch-parse", ".png");
        try {
            BatchMode.Options options = BatchMode.parseArgs(new String[]{
                    "--image", imagePath.toString(),
                    "--resolution", "8",
                    "--output", "html",
                    "--output-file", "art.html",
                    "--charset", "printable"
            });

            assertFalse(options.isHelpRequested(), "help flag");
            assertEquals(imagePath.toString(), options.getImagePath(), "image path");
            assertEquals(8, options.getResolution(), "resolution");
            assertEquals(BatchMode.OutputMode.HTML, options.getOutputMode(), "output mode");
            assertEquals(Path.of("art.html"), options.getOutputFile(), "output file");
            assertEquals(BatchMode.CharsetPreset.PRINTABLE, options.getCharsetPreset(),
                    "charset preset");
        } finally {
            Files.deleteIfExists(imagePath);
        }
    }

    private static void testRunWritesHtmlAndReturnsAsciiMatrix() throws Exception {
        Path imagePath = createTempImage();
        Path outputPath = Files.createTempFile("ascii-art-batch-output", ".html");
        try {
            BatchMode.Options options = BatchMode.parseArgs(new String[]{
                    "--image", imagePath.toString(),
                    "--resolution", "2",
                    "--output", "html",
                    "--output-file", outputPath.toString()
            });

            char[][] asciiArt = BatchMode.run(options);

            assertEquals(2, asciiArt.length, "row count");
            assertEquals(2, asciiArt[0].length, "column count");
            String html = Files.readString(outputPath);
            assertTrue(html.contains("<!DOCTYPE html>"), "doctype present");
            assertTrue(html.contains("<pre>"), "pre block present");
        } finally {
            Files.deleteIfExists(imagePath);
            Files.deleteIfExists(outputPath);
        }
    }

    private static Path createTempImage() throws IOException {
        BufferedImage image = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                int red = x * 40;
                int green = y * 40;
                int blue = (x + y) * 20;
                image.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }
        Path file = Files.createTempFile("ascii-art-batch", ".png");
        ImageIO.write(image, "png", file.toFile());
        return file;
    }

    private static void testMissingImageReturnsNonZeroExitCode() throws Exception {
        String javaBin = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        String classpath = System.getProperty("java.class.path");
        Process process = new ProcessBuilder(
                javaBin,
                "-cp",
                classpath,
                "ascii_art.Shell",
                "--image",
                "does-not-exist.png"
        ).redirectErrorStream(true).start();

        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int exitCode = process.waitFor();

        assertEquals(1, exitCode, "missing image exit code");
        assertTrue(!output.isBlank(), "missing image output");
    }

    private static void assertTrue(boolean condition, String label) {
        if (!condition) {
            throw new AssertionError(label + ": expected true");
        }
    }

    private static void assertFalse(boolean condition, String label) {
        if (condition) {
            throw new AssertionError(label + ": expected false");
        }
    }

    private static void assertEquals(Object expected, Object actual, String label) {
        if (!expected.equals(actual)) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
    }

    private static void assertEquals(int expected, int actual, String label) {
        if (expected != actual) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
    }
}
