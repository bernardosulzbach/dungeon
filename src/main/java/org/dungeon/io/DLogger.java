package org.dungeon.io;

import org.dungeon.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dungeon's very own logger class.
 * <p/>
 * Created by Bernardo Sulzbach on 14/11/14.
 */
public class DLogger {

    private static Logger logger;
    private static final String LOG_FILE_PATH = "logs/";
    private static final String LOG_FILE_NAME = "log.txt";

    private DLogger() {
    }

    public static void initialize() {
        if (logger == null) {
            try {
                logger = Logger.getLogger("org.dungeon");
                Handler handler = new FileHandler(getLogFilePath(), true);
                handler.setFormatter(new DFormatter());
                logger.addHandler(handler);
                logger.setLevel(Level.ALL);
            } catch (IOException ignored) {
            }
        }
    }

    // These methods can be called (and actually are) even if the logger was not initialized. In this case, the messages
    // are discarded.
    public static void finest(String message) {
        if (logger != null) {
            logger.finest(message);
        }
    }

    public static void severe(String message) {
        if (logger != null) {
            logger.severe(message);
        }
    }

    /**
     * Retrieves the path of a plain text file to be used to store logging messages. If the logging directory does not
     * exist, it will be created.
     *
     * @return the file path of a text file to be used by the FileHandler constructor.
     */
    public static String getLogFilePath() {
        File logFolder = new File(LOG_FILE_PATH);
        if (!logFolder.exists()) {
            if (!logFolder.mkdir()) {
                Utils.printFailedToCreateDirectoryMessage(LOG_FILE_PATH);
            }
        }
        return LOG_FILE_PATH + LOG_FILE_NAME;
    }

}