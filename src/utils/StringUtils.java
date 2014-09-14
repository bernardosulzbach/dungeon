package utils;

/**
 *
 * @author Bernardo Sulzbach
 */
public class StringUtils {

    /**
     * Centers a string.
     *
     * @param string the string to be centered.
     * @param width the width of the resulting string.
     * @param fill the filling character.
     * @return a centered string.
     */
    public static String centerString(String string, int width, char fill) {
        int length = string.length();
        if (length > width) {
            throw new IllegalArgumentException("String is bigger than the desired width.");
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < (width - length) / 2; i++) {
                builder.append(fill);
            }
            builder.append(string);
            for (int i = 0; i < (width - length) / 2; i++) {
                builder.append(fill);
            }
            if (builder.length() < width) {
                builder.append(fill);
            }
            return builder.toString();
        }
    }

    /**
     * Checks if a string is alphabetic (only contains letters).
     *
     * @param name
     * @return
     */
    public static boolean isAlphabetic(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (!Character.isAlphabetic(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a string of repeated characters.
     *
     * @param repetitions
     * @param character
     * @return
     */
    public static String makeRepeatedCharacterString(int repetitions, char character) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < repetitions; i++) {
            builder.append(character);
        }
        return builder.toString();
    }

}
