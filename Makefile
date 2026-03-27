JAVA := java
JAVAC := javac
BUILD_DIR := build/classes
SRC_DIRS := ascii_art image image_char_matching ascii_output tests
SOURCES := $(shell find $(SRC_DIRS) -name '*.java')
MAIN_CLASS := ascii_art.Shell
TEST_CLASS := tests.AsciiArtCoreTests

.DEFAULT_GOAL := build

.PHONY: build run test clean

build:
	mkdir -p $(BUILD_DIR)
	$(JAVAC) -d $(BUILD_DIR) $(SOURCES)

run: build
	$(JAVA) -cp $(BUILD_DIR) $(MAIN_CLASS)

test: build
	$(JAVA) -cp $(BUILD_DIR) $(TEST_CLASS)

clean:
	rm -rf build
