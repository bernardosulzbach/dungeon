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

import org.dungeon.game.Game;
import org.dungeon.game.GameState;
import org.dungeon.game.IssuedCommand;
import org.dungeon.util.Messenger;
import org.dungeon.util.Table;
import org.dungeon.util.Utils;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Loader class that handles saving / loading the game.
 * <p/>
 * Created by Bernardo Sulzbach.
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

  /**
   * Pretty-prints all the files in the saves folder.
   */
  public static void printFilesInSavesFolder() {
    File[] files = SAVES_FOLDER.listFiles();
    if (files != null) {
      if (files.length != 0) {
        Table table = new Table("Name", "Size", "Last modified");
        int fileCount = 0;
        int byteCount = 0;
        for (File file : files) {
          fileCount += 1;
          byteCount += file.length();
          Date lastModified = new Date(file.lastModified());
          table.insertRow(file.getName(), Utils.bytesToHuman(file.length()), LAST_MODIFIED_FORMAT.format(lastModified));
        }
        if (fileCount > 1) {
          table.insertSeparator();
          table.insertRow("Sum of these " + fileCount + " files", Utils.bytesToHuman(byteCount));
        }
        table.print();
      } else {
        IO.writeString("Saves folder is empty.");
      }
    } else {
      IO.writeString("Saves folder does not exist.");
    }
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
    File defaultSave = new File(SAVES_FOLDER, DEFAULT_SAVE_NAME);
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
    IO.writeString("Created a new game.");
    return new GameState();
  }

  /**
   * Loads the default save file if it exists. If it does not exist, this returns another existing save. Lastly, if the
   * saves folder is empty or does not exist, the method returns {@code null}.
   * Note that if the user does not confirm the operation in the dialog that pops up, this method return {@code null}.
   */
  public static GameState loadGame() {
    if (checkForDefaultSave()) {
      if (confirmOperation(LOAD_CONFIRM)) {
        return loadFile(new File(SAVES_FOLDER, DEFAULT_SAVE_NAME));
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
      File save = new File(SAVES_FOLDER, argument);
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
    if (confirmOperation(SAVE_CONFIRM)) {
      saveFile(gameState, DEFAULT_SAVE_NAME);
    }
  }

  /**
   * Saves a GameState, assigning a new name for the save file, if provided.
   */
  public static void saveGame(GameState gameState, IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      if (confirmOperation(SAVE_CONFIRM)) {
        saveFile(gameState, issuedCommand.getFirstArgument());
      }
    } else {
      saveGame(gameState);
    }
  }

  /**
   * Attempts to load a GameState from a file.
   *
   * @param file a File object
   * @return a GameState or {@code null} if something goes wrong.
   */
  private static GameState loadFile(File file) {
    FileInputStream fileInStream;
    ObjectInputStream objectInStream;
    try {
      fileInStream = new FileInputStream(file);
      objectInStream = new ObjectInputStream(fileInStream);
      GameState loadedGameState = (GameState) objectInStream.readObject();
      objectInStream.close();
      String formatString = "Successfully loaded the game (read %s from %s).";
      IO.writeString(String.format(formatString, Utils.bytesToHuman(file.length()), file.getName()));
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
    name = ensureSaveEndsWithExtension(name);
    File file = new File(SAVES_FOLDER, name);
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
      long bytes = file.length();
      String formatString = "Successfully saved the game (wrote %s to %s).";
      IO.writeString(String.format(formatString, Utils.bytesToHuman(bytes), file.getName()));
    } catch (IOException bad) {
      IO.writeString("Could not save the game.");
    }
  }

}
