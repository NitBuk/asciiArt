package ascii_art;

import ascii_art.exceptions.InvalidCommandException;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

/**
 * Parses and runs the non-interactive ASCII-art workflow.
 */
public final class BatchMode {
    /**
     * Supported batch output targets.
     */
    public enum OutputMode {
        CONSOLE,
        HTML
    }

    /**
     * Supported character palette presets.
     */
    public enum CharsetPreset {
        DEFAULT,
        PRINTABLE
    }

    /**
     * Parsed batch-mode command-line options.
     */
    public static final class Options {
        private final boolean helpRequested;
        private final String imagePath;
        private final int resolution;
        private final OutputMode outputMode;
        private final Path outputFile;
        private final CharsetPreset charsetPreset;

        private Options(boolean helpRequested, String imagePath, int resolution,
                        OutputMode outputMode, Path outputFile, CharsetPreset charsetPreset) {
            this.helpRequested = helpRequested;
            this.imagePath = imagePath;
            this.resolution = resolution;
            this.outputMode = outputMode;
            this.outputFile = outputFile;
            this.charsetPreset = charsetPreset;
        }

        public boolean isHelpRequested() {
            return helpRequested;
        }

        public String getImagePath() {
            return imagePath;
        }

        public int getResolution() {
            return resolution;
        }

        public OutputMode getOutputMode() {
            return outputMode;
        }

        public Path getOutputFile() {
            return outputFile;
        }

        public CharsetPreset getCharsetPreset() {
            return charsetPreset;
        }
    }

    private static final int DEFAULT_RESOLUTION = 128;

    private BatchMode() {
    }

    /**
     * Parses CLI arguments into batch-mode options.
     *
     * @param args command-line arguments
     * @return parsed options
     * @throws InvalidCommandException if the arguments are malformed
     */
    public static Options parseArgs(String[] args) throws InvalidCommandException {
        boolean helpRequested = false;
        String imagePath = null;
        int resolution = DEFAULT_RESOLUTION;
        OutputMode outputMode = OutputMode.CONSOLE;
        Path outputFile = Path.of("out.html");
        CharsetPreset charsetPreset = CharsetPreset.DEFAULT;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "--help", "-h" -> helpRequested = true;
                case "--image" -> imagePath = requireValue(args, ++i, arg);
                case "--resolution" -> resolution = parseResolution(requireValue(args, ++i, arg));
                case "--output" -> outputMode = parseOutputMode(requireValue(args, ++i, arg));
                case "--output-file" -> outputFile = Path.of(requireValue(args, ++i, arg));
                case "--charset" -> charsetPreset = parseCharsetPreset(requireValue(args, ++i, arg));
                default -> throw new InvalidCommandException("Unknown argument: " + arg);
            }
        }

        if (!helpRequested && imagePath == null) {
            throw new InvalidCommandException("Missing required --image argument.");
        }

        return new Options(helpRequested, imagePath, resolution, outputMode, outputFile,
                charsetPreset);
    }

    /**
     * Runs the batch workflow and returns the generated ASCII art.
     *
     * @param options parsed batch options
     * @return generated ASCII art
     * @throws IOException if the image or output file cannot be accessed
     * @throws InvalidCommandException if the palette is invalid
     */
    public static char[][] run(Options options) throws IOException, InvalidCommandException {
        System.setProperty("java.awt.headless", "true");
        if (options.isHelpRequested()) {
            return new char[0][0];
        }

        Set<Character> charset = options.getCharsetPreset() == CharsetPreset.PRINTABLE
                ? CharacterSets.printableCharset()
                : CharacterSets.defaultCharset();
        if (charset.size() < 2) {
            throw new InvalidCommandException("Did not execute. Charset is too small.");
        }

        Image image = new Image(options.getImagePath());
        int effectiveResolution = normalizeResolution(options.getResolution(), image.getWidth());
        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(image, effectiveResolution,
                charset);
        char[][] asciiArt = asciiArtAlgorithm.run();

        if (options.getOutputMode() == OutputMode.CONSOLE) {
            new ConsoleAsciiOutput().out(asciiArt);
        } else {
            new HtmlAsciiOutput(options.getOutputFile().toString(), "Courier New").out(asciiArt);
            System.out.println("ASCII art written to " + options.getOutputFile());
        }

        return asciiArt;
    }

    private static int normalizeResolution(int requestedResolution, int imageWidth) {
        return Math.max(1, Math.min(requestedResolution, imageWidth));
    }

    private static int parseResolution(String value) throws InvalidCommandException {
        try {
            int resolution = Integer.parseInt(value);
            if (resolution <= 0) {
                throw new NumberFormatException();
            }
            return resolution;
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Resolution must be a positive integer.");
        }
    }

    private static OutputMode parseOutputMode(String value) throws InvalidCommandException {
        return switch (value) {
            case "console" -> OutputMode.CONSOLE;
            case "html" -> OutputMode.HTML;
            default -> throw new InvalidCommandException(
                    "Unsupported output mode: " + value + ".");
        };
    }

    private static CharsetPreset parseCharsetPreset(String value) throws InvalidCommandException {
        return switch (value) {
            case "default" -> CharsetPreset.DEFAULT;
            case "printable", "ascii" -> CharsetPreset.PRINTABLE;
            default -> throw new InvalidCommandException(
                    "Unsupported charset preset: " + value + ".");
        };
    }

    private static String requireValue(String[] args, int index, String flag)
            throws InvalidCommandException {
        if (index >= args.length) {
            throw new InvalidCommandException("Missing value for " + flag + ".");
        }
        return args[index];
    }
}
