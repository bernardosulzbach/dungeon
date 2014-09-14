package utils;

import game.IO;

/**
 * General utility class.
 *
 * @author Bernardo Sulzbach
 */
public class Utils {



    /**
     * Prints the startup heading.
     */
    public static void printHeading() {
        IO.writeString(Constants.HEADING);
    }

    /**
     * Checks if a string is a valid name in the game.
     *
     * @param name
     * @return
     */
    public static boolean isValidName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Names must be at least one character long.");
        } else if (!StringUtils.isAlphabetic(name)) {
            throw new IllegalArgumentException("Names must contain only letters.");
        } else {
            return true;
        }
    }

}
