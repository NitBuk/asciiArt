package image_char_matching;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The SubImgCharMatcher class is responsible for managing a character set and calculating their brightness.
 * It provides methods to add, remove, and retrieve characters, and to find the best matching character
 * based on image brightness.
 */
public class SubImgCharMatcher {
    private final Map<Character, Double> charBrightnessMap;
    private boolean needsNormalization;

    /**
     * Constructs a SubImgCharMatcher with the given character set.
     *
     * @param charset the character set to initialize the matcher with
     */
    public SubImgCharMatcher(char[] charset) {
        this.charBrightnessMap = new HashMap<>();
        this.needsNormalization = true;
        for (char c : charset) {
            double brightness = calculateBrightness(CharConverter.convertToBoolArray(c));
            this.charBrightnessMap.put(c, brightness);
        }
    }

    /**
     * Constructs a SubImgCharMatcher with the given character set.
     *
     * @param charset the character set to initialize the matcher with
     */
    public SubImgCharMatcher(Set<Character> charset) {
        this.charBrightnessMap = new HashMap<>();
        this.needsNormalization = true;
        for (char c : charset) {
            double brightness = calculateBrightness(CharConverter.convertToBoolArray(c));
            this.charBrightnessMap.put(c, brightness);
        }
    }

    /**
     * Calculates brightness for each character in the character set.
     */
    private void calculateCharBrightness() {
        charBrightnessMap.replaceAll((c, brightness) ->
                calculateBrightness(CharConverter.convertToBoolArray(c)));
        normalizeBrightness();
    }

    /**
     * Calculates the brightness of a given boolean array representation of a character.
     *
     * @param charImage the boolean array representation of the character
     * @return the brightness value of the character
     */
    private double calculateBrightness(boolean[][] charImage) {
        int whitePixels = 0;
        int totalPixels = charImage.length * charImage[0].length;

        for (boolean[] row : charImage) {
            for (boolean pixel : row) {
                if (pixel) {
                    whitePixels++;
                }
            }
        }
        return (double) whitePixels / totalPixels;
    }

    /**
     * Normalizes the brightness values of all characters in the character set.
     */
    private void normalizeBrightness() {
        double minBrightness = Collections.min(charBrightnessMap.values());
        double maxBrightness = Collections.max(charBrightnessMap.values());

        for (Map.Entry<Character, Double> entry : charBrightnessMap.entrySet()) {
            double normalizedBrightness = (entry.getValue() - minBrightness)
                    / (maxBrightness - minBrightness);
            entry.setValue(normalizedBrightness);
        }
        needsNormalization = false;
    }

    /**
     * Gets the best matching character for a given brightness value.
     *
     * @param brightness the brightness value to match
     * @return the character with the closest brightness value
     */
    public char getCharByImageBrightness(double brightness) {
        if (needsNormalization) {
            calculateCharBrightness();
        }

        double minDifference = Double.MAX_VALUE;
        char bestMatch = 0;

        for (Map.Entry<Character, Double> entry : charBrightnessMap.entrySet()) {
            double difference = Math.abs(entry.getValue() - brightness);
            if (difference < minDifference ||
                    (difference == minDifference && entry.getKey() < bestMatch)) {
                minDifference = difference;
                bestMatch = entry.getKey();
            }
        }

        return bestMatch;
    }

    /**
     * Adds a character to the character set.
     *
     * @param c the character to add
     */
    public void addChar(char c) {
        double brightness = calculateBrightness(CharConverter.convertToBoolArray(c));
        charBrightnessMap.put(c, brightness);
        needsNormalization = true;
    }

    /**
     * Removes a character from the character set.
     *
     * @param c the character to remove
     */
    public void removeChar(char c) {
        charBrightnessMap.remove(c);
        needsNormalization = true;
    }

    /**
     * Gets the character set.
     *
     * @return a set of characters in the character set
     */
    public Set<Character> getCharset() {
        return charBrightnessMap.keySet();
    }
}
