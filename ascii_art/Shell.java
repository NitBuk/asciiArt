package ascii_art;

import ascii_art.exceptions.*;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Shell class provides a command-line interface for generating ASCII art from images.
 * It allows users to manage character sets, adjust resolution, change the input image,
 * and choose the output format.
 */
public class Shell {
    private static final String DEFAULT_IMAGE_PATH = "cat.jpeg";
    private static final int DEFAULT_RESOLUTION = 128;
    private static final char[] DEFAULT_CHARSET = {'1', '2', '3', '4', '5', '6',
             '7', '8', '9', '0'};
    private static final String EXIT_COMMAND = "exit";
    private static final String CHARS_COMMAND = "chars";
    private static final String ADD_COMMAND = "add";
    private static final String REMOVE_COMMAND = "remove";
    private static final String RES_COMMAND = "res";
    private static final String IMAGE_COMMAND = "image";
    private static final String OUTPUT_COMMAND = "output";
    private static final String ASCII_ART_COMMAND = "asciiArt";
    private static final String CONSOLE_OUTPUT = "console";
    private static final String HTML_OUTPUT = "html";
    private static final String ADD_ALL = "all";
    private static final String ADD_SPACE = "space";
    private static final String RES_UP = "up";
    private static final String RES_DOWN = "down";
    private static final int ASCII_MIN = 32;
    private static final int ASCII_MAX = 126;

    private Image image;
    private int resolution;
    private final SubImgCharMatcher charMatcher;
    private boolean outputToConsole;
    private String imagePath;

    /**
     * This class is managing the user interface
     * @throws IOException if the default image cannot be loaded.
     */
    public Shell() throws IOException {
        this.image = new Image(DEFAULT_IMAGE_PATH);
        this.imagePath = DEFAULT_IMAGE_PATH;
        this.resolution = DEFAULT_RESOLUTION;
        this.charMatcher = new SubImgCharMatcher(DEFAULT_CHARSET);
        this.outputToConsole = true;
    }

    /**
     * The main method to run the Shell.
     *
     * @param args command-line arguments (not used).
     * @throws ShellRunException if the Shell fails to initialize.
     */
    public static void main(String[] args) throws ShellRunException {
        try {
            Shell shell = new Shell();
            shell.run();
        } catch (IOException e) {
            throw new ShellRunException("Failed to initialize the Shell", e);
        }
    }

    /**
     * Runs the Shell, processing user commands in a loop.
     */
    public void run() {
        String command;
        while (true) {
            try {
                System.out.print(">>> ");
                command = KeyboardInput.readLine().trim();
                String primaryCommand = command.split(" ")[0];
                String arguments = command.substring(primaryCommand.length()).trim().split(" ")[0];
                switch (primaryCommand) {
                    case EXIT_COMMAND -> {
                        return;
                    }
                    case CHARS_COMMAND -> printChars();
                    case ADD_COMMAND -> addChars(arguments);
                    case REMOVE_COMMAND -> removeChars(arguments);
                    case RES_COMMAND -> changeResolution(arguments);
                    case IMAGE_COMMAND -> changeImage(arguments);
                    case OUTPUT_COMMAND -> changeOutput(arguments);
                    case ASCII_ART_COMMAND -> generateAsciiArt();
                    default -> throw new
                            InvalidCommandException("Did not execute due to incorrect command.");
                }
            } catch (InvalidCommandException | ImageLoadException | ResolutionChangeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printChars() {
        List<Character> sortedChars = new ArrayList<>(charMatcher.getCharset());
        Collections.sort(sortedChars);
        if (!sortedChars.isEmpty()) {
            for (char c : sortedChars) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    private void addChars(String args) throws InvalidCommandException {
        processChars(args, true);
    }

    private void removeChars(String args) throws InvalidCommandException {
        processChars(args, false);
    }

    private void processChars(String args, boolean isAdd) throws InvalidCommandException {
        if (args.equals(ADD_ALL)) {
            for (char c = ASCII_MIN; c <= ASCII_MAX; c++) {
                if (isAdd) charMatcher.addChar(c);
                else charMatcher.removeChar(c);
            }
        } else if (args.equals(ADD_SPACE)) {
            if (isAdd) charMatcher.addChar(' ');
            else charMatcher.removeChar(' ');
        } else if (args.length() == 1) {
            char c = args.charAt(0);
            if (c >= ASCII_MIN && c <= ASCII_MAX) {
                if (isAdd) charMatcher.addChar(c);
                else charMatcher.removeChar(c);
            } else {
                throw new InvalidCommandException(isAdd ? "Did not add due to incorrect format."
                                                        : "Did not remove due to incorrect format.");
            }
        } else if (args.contains("-")) {
            String[] parts = args.split("-");
            if (parts.length == 2 && parts[0].length() == 1 && parts[1].length() == 1) {
                char start = parts[0].charAt(0);
                char end = parts[1].charAt(0);
                if (start >= ASCII_MIN && end <= ASCII_MAX && start != end) {
                    if (start < end) {
                        for (char c = start; c <= end; c++) {
                            if (isAdd) charMatcher.addChar(c);
                            else charMatcher.removeChar(c);
                        }
                    } else {
                        for (char c = start; c >= end; c--) {
                            if (isAdd) charMatcher.addChar(c);
                            else charMatcher.removeChar(c);
                        }
                    }
                } else {
                    throw new InvalidCommandException(isAdd ? "Did not add due to incorrect format."
                                                            : "Did not remove due to incorrect format.");
                }
            } else {
                throw new InvalidCommandException(isAdd ? "Did not add due to incorrect format."
                                                        : "Did not remove due to incorrect format.");
            }
        } else {
            throw new InvalidCommandException(isAdd ? "Did not add due to incorrect format."
                                                    : "Did not remove due to incorrect format.");
        }
    }

    private void changeResolution(String args) throws ResolutionChangeException {
        if (args.isEmpty()) {
            System.out.println("Resolution set to " + resolution);
        } else if (args.equals(RES_UP)) {
            int newResolution = resolution * 2;
            if (newResolution <= image.getWidth()) {
                resolution = newResolution;
                System.out.println("Resolution set to " + resolution);
            } else {
                System.out.println("Did not change resolution due to exceeding boundaries.");
            }
        } else if (args.equals(RES_DOWN)) {
            int newResolution = resolution / 2;
            int minCharsInRow = Math.max(1, image.getWidth() / image.getHeight());
            if (newResolution >= minCharsInRow) {
                resolution = newResolution;
                System.out.println("Resolution set to " + resolution);
            } else {
                System.out.println("Did not change resolution due to exceeding boundaries.");
            }
        } else {
            throw new ResolutionChangeException("Did not change resolution due to incorrect format.");
        }
    }

    private void changeImage(String imagePath) throws ImageLoadException {
        if (imagePath.isEmpty()) {
            System.out.println("Did not change image method due to incorrect format.");
            return;
        }
        try {
            image = new Image(imagePath);
            this.imagePath = imagePath;
            if (resolution > image.getWidth()) {
                resolution = 2;
            }
        } catch (IOException e) {
            throw new ImageLoadException("Did not execute due to problem with image file.");
        }
    }

    private void changeOutput(String output) throws InvalidCommandException {
        if (output.equals(CONSOLE_OUTPUT)) {
            outputToConsole = true;
        } else if (output.equals(HTML_OUTPUT)) {
            outputToConsole = false;
        } else {
            throw new InvalidCommandException("Did not change output method due to incorrect format.");
        }
    }

    private void generateAsciiArt() throws InvalidCommandException {
        if (charMatcher.getCharset().size() < 2) {
            throw new InvalidCommandException("Did not execute. Charset is too small.");
        }

        try {
            AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(imagePath, resolution,
                    charMatcher.getCharset());
            char[][] asciiArt = asciiArtAlgorithm.run();

            if (outputToConsole) {
                new ConsoleAsciiOutput().out(asciiArt);
            } else {
                new HtmlAsciiOutput("out.html", "Courier New").out(asciiArt);
            }
        } catch (IOException e) {
            System.out.println("Error generating ASCII art.");
        }
    }
}
