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

package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.GameState;
import org.mafagafogigante.dungeon.logging.DungeonLogger;
import org.mafagafogigante.dungeon.util.Messenger;
import org.mafagafogigante.dungeon.util.StopWatch;

import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

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

  private Loader() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Evaluates if a File is a save file by checking that it is a file and ends with the save extension.
   *
   * @param file a File object
   * @return true if the specified File is not {@code null} and has the properties of a save file
   */
  private static boolean isSaveFile(File file) {
    return file != null && file.getName().endsWith(SAVE_EXTENSION) && file.isFile();
  }

  /**
   * Checks if any file in the saves folder ends with the save extension.
   */
  public static boolean checkForSave() {
    File[] sortedArrayOfSavedFiles = getSortedArrayOfSavedFiles();
    return (sortedArrayOfSavedFiles != null && sortedArrayOfSavedFiles.length != 0);
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
    DungeonString string = new DungeonString();
    string.append("Created a new game.\n\n");
    string.append(gameState.getPreface());
    string.append("\n");
    Writer.write(string);
    Game.getGameWindow().requestFocusOnTextField();
    return gameState;
  }

  /**
   * Loads the newest save file if there is a save file. Otherwise, returns {@code null}.
   *
   * <p>Note that if the user does not confirm the operation in the dialog that pops up, this method return {@code
   * null}.
   *
   * @param requireConfirmation whether or not this method should require confirmation from the user
   * @return a GameState or null
   */
  public static GameState loadGame(boolean requireConfirmation) {
    if (checkForSave()) {
      if (!requireConfirmation || confirmOperation(LOAD_CONFIRM)) {
        return loadFile(getMostRecentlySavedFile());
      }
    }
    return null;
  }

  /**
   * Attempts to load the save file indicated by the first argument of the "load" command.
   *
   * <p>If the filename was provided but didn't match any files, a message about this is written.
   *
   * <p>If the load command was issued without arguments, this method delegates save loading to {@code
   * Loader.loadGame(false)}.
   *
   * <p>If no save file could be found, a message is written.
   *
   * <p>This method guarantees that the if null is returned, something is written to the screen.
   */
  public static GameState parseLoadCommand(String[] arguments) {
    if (arguments.length != 0) {
      // A save name was provided.
      String argument = arguments[0];
      argument = ensureSaveEndsWithExtension(argument);
      File save = createFileFromName(argument);
      if (isSaveFile(save)) {
        return loadFile(save);
      } else {
        Writer.write(save.getName() + " does not exist or is not a file.");
        return null;
      }
    } else {
      GameState loadResult = loadGame(false); // Don't ask for confirmation. Typing load is not an easy mistake.
      if (loadResult == null) {
        Writer.write("No saved game could be found.");
      }
      return loadResult;
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
   *
   * <p>Only asks for confirmation if there already is a save file with the name.
   */
  public static void saveGame(GameState gameState, String[] arguments) {
    String saveName = DEFAULT_SAVE_NAME;
    if (arguments != null && arguments.length != 0) {
      saveName = arguments[0];
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
    FSTObjectInput objectInStream;
    try {
      fileInStream = new FileInputStream(file);
      objectInStream = new FSTObjectInput(fileInStream);
      GameState loadedGameState = (GameState) objectInStream.readObject();
      objectInStream.close();
      loadedGameState.setSaved(true); // It is saved, we just loaded it (needed as it now defaults to false).
      String sizeString = Converter.bytesToHuman(file.length());
      DungeonLogger.info(String.format("Loaded %s in %s.", sizeString, stopWatch.toString()));
      Writer.write(String.format("Successfully loaded the game (read %s from %s).", sizeString, file.getName()));
      return loadedGameState;
    } catch (Exception bad) {
      Writer.write("Could not load the saved game.");
      return null;
    }
  }

  /**
   * Serializes the specified {@code GameState} state to a file.
   *
   * @param state a GameState
   * @param name the name of the file
   */
  private static void saveFile(GameState state, String name) {
    StopWatch stopWatch = new StopWatch();
    File file = createFileFromName(name);
    FileOutputStream fileOutStream;
    FSTObjectOutput objectOutStream;
    try {
      if (!SAVES_FOLDER.exists()) {
        if (!SAVES_FOLDER.mkdir()) {
          Messenger.printFailedToCreateDirectoryMessage(SAVES_FOLDER.getName());
          return;
        }
      }
      fileOutStream = new FileOutputStream(file);
      objectOutStream = new FSTObjectOutput(fileOutStream);
      objectOutStream.writeObject(state);
      objectOutStream.close();
      state.setSaved(true);
      String sizeString = Converter.bytesToHuman(file.length());
      DungeonLogger.info(String.format("Saved %s in %s.", sizeString, stopWatch.toString()));
      Writer.write(String.format("Successfully saved the game (wrote %s to %s).", sizeString, file.getName()));
    } catch (IOException bad) {
      Writer.write("Could not save the game.");
    }
  }

  /**
   * Returns an array of abstract pathnames denoting the files and directories in the saves folder that end with a valid
   * extension. Returns null if an I/O error occurs.
   */
  public static File[] getSortedArrayOfSavedFiles() {
    File[] saveFiles = SAVES_FOLDER.listFiles(DungeonFilenameFilters.getExtensionFilter());
    if (saveFiles != null) {
      Arrays.sort(saveFiles, new FileLastModifiedComparator());
    }
    return saveFiles;
  }

  private static File getMostRecentlySavedFile() {
    File[] saveFiles = getSortedArrayOfSavedFiles();
    if (saveFiles.length == 0) {
      return null;
    } else {
      return saveFiles[0];
    }
  }

}
