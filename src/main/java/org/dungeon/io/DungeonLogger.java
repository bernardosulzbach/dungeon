/*
 * Copyright (C) 2014 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dungeon.io;

import org.dungeon.util.Messenger;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dungeon logger class.
 */
public final class DungeonLogger {

  private static final String LOG_FILE_PATH = "logs/";
  private static final String LOG_FILE_NAME = "log.txt";
  private static Logger logger;

  static {
    try {
      logger = Logger.getLogger("org.dungeon");
      Handler handler = new FileHandler(getLogFilePath(), true);
      handler.setFormatter(new LoggerDateFormatter());
      logger.setUseParentHandlers(false);
      logger.addHandler(handler);
      logger.setLevel(Level.ALL);
    } catch (IOException ignored) {
      // It is not possible to log an exception that prevented us from getting a logger.
    }
  }

  private DungeonLogger() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Logs an info message. If the logger could not be initialized, the message will be unceremoniously discarded.
   *
   * @param message the log message
   */
  public static void info(String message) {
    if (logger != null) {
      logger.info(message);
    }
  }

  /**
   * Logs a warning message. If the logger could not be initialized, the message will be unceremoniously discarded.
   *
   * @param message the log message
   */
  public static void warning(String message) {
    if (logger != null) {
      logger.warning(message);
    }
  }

  /**
   * Logs a WARNING message with filename and line number.
   *
   * <p> The message is produced by the following String concatenation
   *
   * <p> {@code "Line " + lineNumber + " of " + filename + messageEnd}
   *
   * @param filename the name of the file
   * @param lineNumber the number of the line that caused the warning
   * @param messageEnd how the message should end
   */
  public static void warning(String filename, int lineNumber, String messageEnd) {
    warning("Line " + lineNumber + " of " + filename + messageEnd);
  }

  /**
   * Logs a severe message. Only errors that cause fatal application termination are considered to be severe. If the
   * logger could not be initialized, the message will be unceremoniously discarded.
   *
   * @param message the log message
   */
  public static void severe(String message) {
    if (logger != null) {
      logger.severe(message);
    }
  }

  /**
   * Logs an inventory management message.
   *
   * @param message the log message
   */
  public static void inventoryManagement(String message) {
    if (logger != null) {
      logger.fine(message);
    }
  }

  /**
   * Retrieves the path of a plain text file to be used to store logging messages. If the logging directory does not
   * exist, it will be created.
   *
   * @return the file path of a text file to be used by the FileHandler constructor.
   */
  private static String getLogFilePath() {
    File logFolder = new File(LOG_FILE_PATH);
    if (!logFolder.exists()) {
      if (!logFolder.mkdir()) {
        Messenger.printFailedToCreateDirectoryMessage(LOG_FILE_PATH);
      }
    }
    return LOG_FILE_PATH + LOG_FILE_NAME;
  }

}