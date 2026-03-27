package image_char_matching;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Maintains a character palette and maps image brightness to the closest match.
 */
public class SubImgCharMatcher {
    private final Map<Character, Double> charBrightnessMap;
    private boolean needsNormalization;

    /**
     * Creates a matcher from an initial palette.
     *
     * @param charset initial characters
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
     * Creates a matcher from an initial palette.
     *
     * @param charset initial characters
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
     * Recomputes the per-character brightness cache and normalizes it.
     */
    private void calculateCharBrightness() {
        charBrightnessMap.replaceAll((c, brightness) ->
                calculateBrightness(CharConverter.convertToBoolArray(c)));
        normalizeBrightness();
    }

    /**
     * Counts the fraction of white pixels in a rendered character.
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
     * Normalizes all cached brightness values to {@code [0, 1]}.
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
     * Returns the closest-matching character for the given brightness.
     *
     * @param brightness normalized tile brightness
     * @return selected character
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
     * Adds a character to the palette.
     *
     * @param c character to add
     */
    public void addChar(char c) {
        double brightness = calculateBrightness(CharConverter.convertToBoolArray(c));
        charBrightnessMap.put(c, brightness);
        needsNormalization = true;
    }

    /**
     * Removes a character from the palette.
     *
     * @param c character to remove
     */
    public void removeChar(char c) {
        charBrightnessMap.remove(c);
        needsNormalization = true;
    }

    /**
     * Returns the current palette.
     *
     * @return current characters
     */
    public Set<Character> getCharset() {
        return charBrightnessMap.keySet();
    }
}
