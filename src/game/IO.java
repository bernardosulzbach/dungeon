package game;

/**
 * IO class that encapsulates all Input/Output operations.
 *
 * @author Bernardo Sulzbach - 13/09/2014
 */
public class IO {

    /**
     * Outputs a string to the console, stripping unnecessary newlines at the end.
     *
     * @param string the string to be printed.
     */
    public static void writeString(String string) {
        while (string.endsWith("\n")) {
            string = string.substring(0, string.length() - 1);
        }
        System.out.println(string);
    }

    /**
     * Read a line of input from the user.
     *
     * @return
     */
    public static String readString() {
        String line;
        do {
            System.out.print("> ");
            line = Game.SCANNER.nextLine().trim();
        } while (line.equals(""));
        return line;
    }

    /**
     * Read a line of input from the user and returns an array with the words in that line.
     *
     * @return a String array.
     */
    public static String[] readWords() {
        return readString().split("\\s+");
    }

}
