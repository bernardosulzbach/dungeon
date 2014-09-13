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

}
