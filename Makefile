JAVA := java
JAVAC := javac
BUILD_DIR := build/classes
SRC_DIRS := ascii_art image image_char_matching ascii_output tests
SOURCES := $(shell find $(SRC_DIRS) -name '*.java')
MAIN_CLASS := ascii_art.Shell
TEST_CLASSES := tests.AsciiArtCoreTests tests.AsciiArtBatchTests
BATCH_ARGS ?=

.DEFAULT_GOAL := build

.PHONY: build run test clean
.PHONY: batch

build:
	mkdir -p $(BUILD_DIR)
	$(JAVAC) -d $(BUILD_DIR) $(SOURCES)

run: build
	$(JAVA) -cp $(BUILD_DIR) $(MAIN_CLASS)

batch: build
	$(JAVA) -cp $(BUILD_DIR) $(MAIN_CLASS) $(BATCH_ARGS)

test: build
	@set -e; for test_class in $(TEST_CLASSES); do $(JAVA) -cp $(BUILD_DIR) $$test_class; done

clean:
	rm -rf build
