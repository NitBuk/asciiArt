# asciiArt

A compact Java CLI that turns a local image into ASCII art by sampling the image in square tiles, matching each tile to a character palette, and rendering the result to the terminal or to HTML.

## 30-Second Overview

This repo is a small but complete engineering project: it includes a command-line shell, image preprocessing, character-to-brightness matching, and two output modes. The focus is on clear pipeline design rather than on model training or AI APIs.

## What It Demonstrates

- CLI command parsing and interactive workflow design.
- Image preprocessing: padding, tiling, and brightness calculation.
- Character matching based on measured brightness.
- Two render targets: terminal output and HTML output.
- Lightweight exception handling and testable helper logic.

## Project Structure

- `ascii_art/` - CLI entrypoint, shell logic, and pipeline orchestration.
- `ascii_art/exceptions/` - shell-specific exception types.
- `ascii_output/` - console and HTML renderers.
- `image/` - image wrapper, padding, and splitting helpers.
- `image_char_matching/` - character brightness measurement and matching.
- `tests/` - lightweight deterministic checks for core helpers.
- `UML.pdf` - legacy course design artifact kept for historical context.

## Quickstart

Requirements: Java 8 or newer.

```sh
make build
make test
make run
```

If you prefer direct `javac`/`java` calls:

```sh
javac -d build/classes $(find ascii_art image image_char_matching ascii_output tests -name '*.java')
java -cp build/classes ascii_art.Shell
java -cp build/classes tests.AsciiArtCoreTests
```

## How To Use

The shell starts with the default character palette already loaded. Before generating art, load a local image:

```text
>>> image /path/to/photo.png
Loaded image: /path/to/photo.png
>>> res 64
Resolution set to 64
>>> output console
Output set to console.
>>> asciiArt
```

The console mode prints the art directly in the terminal. HTML mode writes `out.html` in the project root:

```text
>>> output html
Output set to html.
>>> asciiArt
ASCII art written to out.html
```

### Supported Commands

- `chars` - print the current character palette.
- `add <char>` - add one character.
- `add all` - add printable ASCII characters.
- `add space` - add the space character.
- `add <start>-<end>` - add a character range.
- `remove <char>` - remove one character.
- `remove all` - remove printable ASCII characters.
- `remove space` - remove the space character.
- `remove <start>-<end>` - remove a character range.
- `res` - show the current resolution.
- `res up` / `res down` - adjust resolution in powers of two.
- `image <path>` - load a local image file.
- `output console` - render to the terminal.
- `output html` - render to `out.html`.
- `asciiArt` - generate the output using the current settings.
- `exit` - quit the shell.

## Architecture

The flow is intentionally simple:

1. `Shell` reads and validates user commands.
2. `Image` loads the source file into an in-memory pixel grid.
3. `ImagePadding` pads the image to power-of-two dimensions.
4. `ImageSplitter` splits the image into square tiles and measures brightness.
5. `SubImgCharMatcher` maps each tile brightness to the nearest character.
6. `ConsoleAsciiOutput` or `HtmlAsciiOutput` renders the resulting matrix.

## Limitations

- The repo does not bundle sample images, so you need to point the shell at a local JPEG/PNG.
- HTML output is intentionally minimal and focuses on readability, not theming.
- The character matching uses rendered glyph brightness, so the exact art depends on the available fonts on your system.

## Development Notes

- Build: `make build`
- Test: `make test`
- Run: `make run`
- Main entrypoint: `ascii_art.Shell`
- Output files: `out.html` is generated when HTML mode is selected.

## Recent Improvements

- Restored a working output package and input helper so the project compiles again.
- Added a small deterministic test harness for padding, splitting, and brightness calculations.
- Reworked the README to match the actual repo layout and runtime flow.
- Added `.gitignore` coverage for build artifacts and IDE files.

## Roadmap

- Add a bundled sample image for a one-command demo.
- Add a richer HTML stylesheet or alternate output format.
- Split shell parsing into smaller helpers if the command set grows.

## About `UML.pdf`

`UML.pdf` is a legacy design artifact from the course project. It is kept for reference, but the source code and README are the current source of truth for how the app works today.
