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

import org.dungeon.commands.IssuedCommand;
import org.dungeon.date.EarthTimeUnit;
import org.dungeon.date.TimeStringBuilder;
import org.dungeon.game.Game;
import org.dungeon.game.GameState;
import org.dungeon.util.Messenger;
import org.dungeon.util.StopWatch;
import org.dungeon.util.Table;

import org.jetbrains.annotations.NotNull;
import org.joda.time.Period;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import javax.swing.JOptionPane;

/**
 * Loader class that handles saving and loading the game.
 */
public final class Loader {

  private static final File SAVES_FOLDER = new File("saves/");
  private static final String SAVE_EXTENSION = ".dungeon";
  private static final String DEFAULT_SAVE_NAME = "default" + SAVE_EXTENSION;
  private static final String SAVE_CONFIRM = "Do you want to save the game?";
  private static final String LOAD_CONFIRM = "Do you want to load the game?";
  private static final SimpleDateFormat LAST_MODIFIED_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private Loader() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  private static void sortFileArrayByLastModifiedDate(@NotNull File[] array) {
    Arrays.sort(array, new Comparator<File>() {
      @Override
      public int compare(File o1, File o2) {
        return Long.compare(o2.lastModified(), o1.lastModified()); // Newer files on the beginning.
      }
    });
  }

  /**
   * Pretty-prints all the files in the saves folder.
   */
  public static void printFilesInSavesFolder() {
    File[] files = SAVES_FOLDER.listFiles();
    if (files != null) {
      sortFileArrayByLastModifiedDate(files);
      if (files.length != 0) {
        Table table = new Table("Name", "Size", "Last modified");
        int fileCount = 0;
        int byteCount = 0;
        for (File file : files) {
          fileCount += 1;
          byteCount += file.length();
          Date lastModified = new Date(file.lastModified());
          String periodString = makePeriodString(lastModified.getTime(), System.currentTimeMillis());
          String lastModifiedString = String.format("%s (%s)", LAST_MODIFIED_FORMAT.format(lastModified), periodString);
          table.insertRow(file.getName(), bytesToHuman(file.length()), lastModifiedString);
        }
        if (fileCount > 1) {
          table.insertSeparator();
          table.insertRow("Sum of these " + fileCount + " files", bytesToHuman(byteCount));
        }
        table.print();
      } else {
        IO.writeString("Saves folder is empty.");
      }
    } else {
      IO.writeString("Saves folder does not exist.");
    }
  }

  private static String makePeriodString(long start, long end) {
    Period period = new Period(start, end);
    TimeStringBuilder builder = new TimeStringBuilder();
    builder.set(EarthTimeUnit.YEAR, period.getYears());
    builder.set(EarthTimeUnit.MONTH, period.getMonths());
    builder.set(EarthTimeUnit.DAY, period.getDays());
    builder.set(EarthTimeUnit.HOUR, period.getHours());
    builder.set(EarthTimeUnit.MINUTE, period.getMinutes());
    builder.set(EarthTimeUnit.SECOND, period.getSeconds());
    return builder.toString(2) + " ago";
  }

  /**
   * Evaluates if a File is a save file by checking that it both is a file and
   *
   * @param file a File object
   * @return true if the specified File is not {@code null} and has the properties of a save file
   */
  private static boolean isSaveFile(File file) {
    return file != null && file.getName().endsWith(SAVE_EXTENSION) && file.isFile();
  }

  /**
   * Check if the default save file exists.
   */
  private static boolean checkForDefaultSave() {
    File defaultSave = createFileFromName(DEFAULT_SAVE_NAME);
    return isSaveFile(defaultSave);
  }

  /**
   * Checks if any file in the saves folder ends with the save extension.
   */
  public static boolean checkForAnySave() {
    File[] files = SAVES_FOLDER.listFiles();
    if (files != null) {
      for (File file : files) {
        if (isSaveFile(file)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Prompts the user to confirm an operation using a dialog window.
   */
  private static boolean confirmOperation(String confirmation) {
    int result = JOptionPane.showConfirmDialog(Game.getGameWindow(), confirmation, null, JOptionPane.YES_NO_OPTION);
    Game.getGameWindow().requestFocusOnTextField();
    return result == JOptionPane.YES_OPTION;
  }

  /**
   * Appends the save file extension to a file name it if it does not ends with it already.
   *
   * @param name a file name
   * @return a String ending with the file extension
   */
  private static String ensureSaveEndsWithExtension(String name) {
    return name.endsWith(SAVE_EXTENSION) ? name : name + SAVE_EXTENSION;
  }

  /**
   * Generates a new GameState and returns it.
   */
  public static GameState newGame() {
    GameState gameState = new GameState();
    IO.writeString("Created a new game.");
    IO.writeNewLine();
    IO.writeString(gameState.getPreface());
    Game.getGameWindow().requestFocusOnTextField();
    return gameState;
  }

  /**
   * Loads the default save file if it exists. If it does not exist, this returns another existing save. Lastly, if the
   * saves folder is empty or does not exist, the method returns {@code null}.
   * Note that if the user does not confirm the operation in the dialog that pops up, this method return {@code null}.
   */
  public static GameState loadGame() {
    if (checkForDefaultSave()) {
      if (confirmOperation(LOAD_CONFIRM)) {
        return loadFile(createFileFromName(DEFAULT_SAVE_NAME));
      }
    } else if (checkForAnySave()) {
      if (confirmOperation(LOAD_CONFIRM)) {
        File[] files = SAVES_FOLDER.listFiles();
        if (files != null) {
          File firstListedFile = files[0];
          return loadFile(firstListedFile);
        }
      }
    }
    return null;
  }

  /**
   * Attempts to load the save file indicated by the first argument of the "load" command. If the load command was
   * issued without arguments, this method delegates save loading to {@code Loader.loadGame()}.
   */
  public static GameState loadGame(IssuedCommand issuedCommand) {
    if (issuedCommand == null) {
      DLogger.warning("Passed null to Loader.loadGame(IssuedCommand)!");
      return null;
    }
    if (issuedCommand.hasArguments()) {
      // A save name was provided.
      String argument = issuedCommand.getFirstArgument();
      argument = ensureSaveEndsWithExtension(argument);
      File save = createFileFromName(argument);
      if (isSaveFile(save)) {
        return loadFile(save);
      } else {
        IO.writeString(save.getName() + " does not exist or is not a file.");
        return null;
      }
    } else {
      return loadGame();
    }
  }

  /**
   * Saves the specified GameState to the default save file.
   */
  public static void saveGame(GameState gameState) {
    saveGame(gameState, null);
  }

  /**
   * Saves the specified GameState, using the default save file or the one defined in the IssuedCommand.
   * <p/>
   * Only asks for confirmation if there already is a save file with the name.
   */
  public static void saveGame(GameState gameState, IssuedCommand issuedCommand) {
    String saveName = DEFAULT_SAVE_NAME;
    if (issuedCommand != null && issuedCommand.hasArguments()) {
      saveName = issuedCommand.getFirstArgument();
    }
    if (saveFileDoesNotExist(saveName) || confirmOperation(SAVE_CONFIRM)) {
      saveFile(gameState, saveName);
    }
  }

  /**
   * Tests whether the save file corresponding to the provided name does not exist.
   *
   * @param name the provided filename
   * @return true if the corresponding file does not exist
   */
  private static boolean saveFileDoesNotExist(String name) {
    return !createFileFromName(name).exists();
  }

  /**
   * Returns a File object for the corresponding save file for a specified name.
   *
   * @param name the provided filename
   * @return a File object
   */
  private static File createFileFromName(String name) {
    return new File(SAVES_FOLDER, ensureSaveEndsWithExtension(name));
  }

  /**
   * Attempts to load a GameState from a file.
   *
   * @param file a File object
   * @return a GameState or {@code null} if something goes wrong.
   */
  private static GameState loadFile(File file) {
    StopWatch stopWatch = new StopWatch();
    FileInputStream fileInStream;
    ObjectInputStream objectInStream;
    try {
      fileInStream = new FileInputStream(file);
      objectInStream = new ObjectInputStream(fileInStream);
      GameState loadedGameState = (GameState) objectInStream.readObject();
      objectInStream.close();
      loadedGameState.setSaved(true); // It is saved, we just loaded it (needed as it now defaults to false).
      String sizeString = bytesToHuman(file.length());
      DLogger.info(String.format("Loaded %s in %s.", sizeString, stopWatch.toString()));
      IO.writeString(String.format("Successfully loaded the game (read %s from %s).", sizeString, file.getName()));
      return loadedGameState;
    } catch (Exception bad) {
      IO.writeString("Could not load the saved game.");
      return null;
    }
  }

  /**
   * Serializes the specified {@code GameState} state to a file.
   *
   * @param state a GameState
   * @param name  the name of the file
   */
  private static void saveFile(GameState state, String name) {
    StopWatch stopWatch = new StopWatch();
    File file = createFileFromName(name);
    FileOutputStream fileOutStream;
    ObjectOutputStream objectOutStream;
    try {
      if (!SAVES_FOLDER.exists()) {
        if (!SAVES_FOLDER.mkdir()) {
          Messenger.printFailedToCreateDirectoryMessage(SAVES_FOLDER.getName());
          return;
        }
      }
      fileOutStream = new FileOutputStream(file);
      objectOutStream = new ObjectOutputStream(fileOutStream);
      objectOutStream.writeObject(state);
      objectOutStream.close();
      state.setSaved(true);
      String sizeString = bytesToHuman(file.length());
      DLogger.info(String.format("Saved %s in %s.", sizeString, stopWatch.toString()));
      IO.writeString(String.format("Successfully saved the game (wrote %s to %s).", sizeString, file.getName()));
    } catch (IOException bad) {
      IO.writeString("Could not save the game.");
    }
  }

  /**
   * Converts a given number of bytes to a human readable format.
   *
   * @return a String
   */
  private static String bytesToHuman(long bytes) {
    if (bytes < 1024) {
      return bytes + " B";
    }
    // 2 ^ 10 (1 kB) has (63 - 10) = 53 leading zeros.
    // 2 ^ 20 (1 MB) has (63 - 20) = 43 leading zeros.
    // And so forth.
    // Bits used to represent the number of bytes = number of bits available - number of leading zeros.
    int bitsUsed = 63 - Long.numberOfLeadingZeros(bytes);
    // (1L << (bitsUsed - bitsUsed % 10)) shifts the one (in binary) to the left by a multiple of 10.
    // This is a fast way to get the power of 1024 by which we must divide the number of bytes.
    double significand = (double) bytes / (1L << (bitsUsed - bitsUsed % 10));
    // By dividing the number of bits used by 10, get the prefix that should be used.
    // Subtract one as Strings are zero indexed.
    char prefix = "kMGTPE".charAt(bitsUsed / 10 - 1);
    return String.format("%.1f %sB", significand, prefix);
  }

}
