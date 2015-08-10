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
 * DungeonLogger static class that provides the thread-safe logging methods that should be used throughout the
 * application.
 */
public final class DungeonLogger {

  private static final String LOG_FILE_PATH = "logs/";
  private static final String LOG_FILE_NAME = "log.txt";
  private static final Logger logger;

  static {
    logger = Logger.getLogger("org.dungeon");
    logger.setUseParentHandlers(false);
    logger.setLevel(Level.ALL);
    try { // Try to add the file handler.
      Handler handler = new FileHandler(getCompleteLogFilePath(), true);
      handler.setFormatter(new LoggerDateFormatter());
      logger.addHandler(handler);
    } catch (IOException ignored) {
    }
  }

  private DungeonLogger() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Logs a fine message. This should be used for tracing information.
   *
   * If the file handler could not be initialized, the message will be unceremoniously discarded.
   *
   * @param message the log message
   */
  public static void fine(String message) {
    logger.fine(message);
  }

  /**
   * Logs an info message. This should be used for application-related information.
   *
   * If the file handler could not be initialized, the message will be unceremoniously discarded.
   *
   * @param message the log message
   */
  public static void info(String message) {
    logger.info(message);
  }

  /**
   * Logs a warning message. This should be used for non-fatal exceptions.
   *
   * If the file handler could not be initialized, the message will be unceremoniously discarded.
   *
   * @param message the log message
   */
  public static void warning(String message) {
    logger.warning(message);
  }

  /**
   * Logs a severe message. This should be used for unrecoverable errors that cause application termination.
   *
   * If the file handler could not be initialized, the message will be unceremoniously discarded.
   *
   * @param message the log message
   */
  public static void severe(String message) {
    logger.severe(message);
  }

  /**
   * Retrieves the path of a plain text file to be used to store logging messages. If the logging directory does not
   * exist, it will be created.
   *
   * @return the file path of a text file to be used by the FileHandler constructor.
   */
  private static String getCompleteLogFilePath() {
    File logFolder = new File(LOG_FILE_PATH);
    if (!logFolder.exists()) {
      if (!logFolder.mkdir()) {
        Messenger.printFailedToCreateDirectoryMessage(LOG_FILE_PATH);
      }
    }
    return LOG_FILE_PATH + LOG_FILE_NAME;
  }

}