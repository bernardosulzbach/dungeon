package utils;

import game.Game;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * General utility class.
 *
 * @author Bernardo Sulzbach
 */
public class Utils {

    /**
     * String used to improve output readability.
     */
    public static final String MARGIN = "  ";

    // DateFormats for time and date printing.
    private static final DateFormat TIME = new SimpleDateFormat("HH:mm:ss");
    private static final DateFormat DATE = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Print the current date according to the final SimpleDateFormat DATE.
     */
    public static void printDate() {
        Game.writeString(Utils.DATE.format(new Date()));
    }

    /**
     * Print the current time according to the final SimpleDateFormat TIME.
     */
    public static void printTime() {
        Game.writeString(Utils.TIME.format(new Date()));
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
     * Checks if a string is a valid name in the game.
     *
     * @param name
     * @return
     */
    public static boolean isValidName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Names must be at least one character long.");
        } else if (!Utils.isAlphabetic(name)) {
            throw new IllegalArgumentException("Names must contain only letters.");
        } else {
            return true;
        }
    }

}
