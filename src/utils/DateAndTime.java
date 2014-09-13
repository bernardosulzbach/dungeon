package utils;

import game.IO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTime {

    // DateFormats for time and date printing.
    private static final DateFormat TIME = new SimpleDateFormat("HH:mm:ss");
    private static final DateFormat DATE = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Print the current time according to the final SimpleDateFormat TIME.
     */
    public static void printTime() {
        IO.writeString(TIME.format(new Date()));
    }

    /**
     * Print the current date according to the final SimpleDateFormat DATE.
     */
    public static void printDate() {
        IO.writeString(DATE.format(new Date()));
    }

}
