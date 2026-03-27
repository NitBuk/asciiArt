package ascii_art;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Shared character palettes used by the interactive shell and batch mode.
 */
public final class CharacterSets {
    private static final char[] DEFAULT_CHARSET = {'1', '2', '3', '4', '5', '6',
            '7', '8', '9', '0'};
    private static final int ASCII_MIN = 32;
    private static final int ASCII_MAX = 126;

    private CharacterSets() {
    }

    /**
     * Returns the default shell palette as an array.
     *
     * @return default palette
     */
    public static char[] defaultCharArray() {
        return DEFAULT_CHARSET.clone();
    }

    /**
     * Returns the default shell palette as a set.
     *
     * @return default palette
     */
    public static Set<Character> defaultCharset() {
        return toSet(DEFAULT_CHARSET);
    }

    /**
     * Returns the printable ASCII palette.
     *
     * @return printable ASCII characters
     */
    public static Set<Character> printableCharset() {
        Set<Character> charset = new LinkedHashSet<>();
        for (char c = ASCII_MIN; c <= ASCII_MAX; c++) {
            charset.add(c);
        }
        return charset;
    }

    private static Set<Character> toSet(char[] chars) {
        Set<Character> charset = new LinkedHashSet<>();
        for (char c : chars) {
            charset.add(c);
        }
        return charset;
    }
}
