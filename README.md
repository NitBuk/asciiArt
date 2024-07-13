# asciiArt

## Description
A Java-based ASCII Art Generator that converts images to ASCII art using a customizable character set and resolution. This project includes a command-line interface for user interaction and supports multiple output formats, including console and HTML.

## Features
- Convert images to ASCII art.
- Customize the character set used for generating ASCII art.
- Adjust the resolution of the ASCII art.
- Choose between console and HTML output formats.
- Command-line interface for easy interaction.

## Getting Started

### Prerequisites
- Java 8 or higher

### Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/NitBuk/asciiArt.git
    ```
2. Navigate to the project directory:
    ```sh
    cd asciiArt
    ```
3. Compile the project:
    ```sh
    javac -d bin src/**/*.java
    ```

### Running the Application
1. Run the application:
    ```sh
    java -cp bin ascii_art.Shell
    ```

### Command-line Interface
- `exit` - Exit the program.
- `chars` - View the current character set.
- `add <char>` - Add a character to the character set.
- `add all` - Add all ASCII characters (32-126) to the character set.
- `add space` - Add a space character to the character set.
- `add <start>-<end>` - Add a range of characters to the character set.
- `remove <char>` - Remove a character from the character set.
- `remove all` - Remove all ASCII characters (32-126) from the character set.
- `remove space` - Remove a space character from the character set.
- `remove <start>-<end>` - Remove a range of characters from the character set.
- `res [up|down]` - Change the resolution of the ASCII art.
- `image <file_path>` - Change the input image.
- `output [console|html]` - Set the output format.
- `asciiArt` - Generate the ASCII art with the current settings.

## Project Structure
- `ascii_art` - Contains the main application classes.
- `ascii_art.exceptions` - Contains custom exception classes.
- `ascii_output` - Contains classes for different output formats.
- `image` - Contains classes for image manipulation.
- `image_char_matching` - Contains classes for character brightness matching.

## Detailed Class Descriptions
- **SubImgCharMatcher**: Manages the set of characters used to generate ASCII art and matches characters to image brightness.
- **Image**: Represents an image and provides methods to manipulate and retrieve image properties.
- **AsciiArtAlgorithm**: Contains the algorithm for converting images into ASCII art based on brightness values.
- **ImagePadding**: Provides utility methods for padding images to ensure they fit required dimensions.
- **Shell**: Provides a command-line interface for the ASCII art generator, allowing users to interact with the program and modify settings.
- **ImageSplitter**: Splits images into smaller segments and calculates brightness values.
- **CharConverter**: Converts characters to different formats for processing and matching.
- **ConsoleAsciiOutput**: Outputs the ASCII art to the console.
- **HtmlAsciiOutput**: Outputs the ASCII art to an HTML file.

## Exception Handling
The application uses custom exceptions to handle errors:
- **InvalidCommandException**: Thrown when an invalid command is entered.
- **ResolutionChangeException**: Thrown when there is an issue changing the resolution.
- **ImageLoadException**: Thrown when there is an issue loading the image.
- **ShellRunException**: Thrown when there is an issue running the shell.

## Author
NitBuk

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

