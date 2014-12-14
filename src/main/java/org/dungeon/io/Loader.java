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

import org.dungeon.game.Engine;
import org.dungeon.game.Game;
import org.dungeon.game.GameState;
import org.dungeon.game.IssuedCommand;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Utils;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Loader class that handles saving / loading the game.
 * <p/>
 * On using this class:
 * <p/>
 * <p/>
 * By Bernardo Sulzbach.
 */
public class Loader {

  // File-related strings.
  private static final String DEFAULT_SAVE_PATH = "saves/";
  // The directory.
  private static final File SAVES_FOLDER = new File(DEFAULT_SAVE_PATH);
  private static final String DEFAULT_SAVE_NAME = "campaign";
  private static final String SAVE_EXTENSION = ".dungeon";
  // Messages.
  private static final String SAVE_ERROR = "Could not save the game.";
  private static final String SAVE_SUCCESS = "Successfully saved the game.";
  private static final String SAVE_CONFIRM = "Do you want to save the game?";

  private static final String LOAD_ERROR = "Could not load the saved game.";
  private static final String LOAD_SUCCESS = "Successfully loaded the game.";
  private static final String LOAD_CONFIRM = "Do you want to load the game?";

  // Format strings.
  private static final String FILE_ENTRY = "%-60s|%s";

  /**
   * Check if the default save file exists.
   */
  private static boolean checkForDefaultSave() {
    File savedCampaign = new File(SAVES_FOLDER, DEFAULT_SAVE_NAME + SAVE_EXTENSION);
    return savedCampaign.exists() && savedCampaign.isFile();
  }

  /**
   * Pretty-prints all the files in the saves folder.
   */
  // TODO: implement a FileTable data structure that eases (and improves) this.
  public static void printFilesInSavesFolder() {
    File[] files = SAVES_FOLDER.listFiles();
    if (files != null) {
      if (files.length != 0) {
        IO.writeString(String.format(FILE_ENTRY, "Name", "Size"));
        for (File file : files) {
          IO.writeString(String.format(FILE_ENTRY, file.getName(), Utils.bytesToHuman(file.length())));
        }
      } else {
        if (Engine.RANDOM.nextBoolean()) {
          IO.writeString("Saves folder is empty.", Color.RED);
        } else {
          IO.writeString("There is not even a single save game for you to see.", Color.RED);
        }
      }
    } else {
      if (Engine.RANDOM.nextBoolean()) {
        IO.writeString("Saves folder does not exist.", Color.RED);
      } else {
        IO.writeString("What did you do to the saves folder?", Color.RED);
      }
    }
  }

  /**
   * Handles all the save loading at startup and during gameplay.
   *
   * @return a saved campaign or a new demo campaign.
   */
  public static GameState loadGame(IssuedCommand issuedCommand) {
    if (issuedCommand != null && issuedCommand.hasArguments()) {
      // A save name was provided.
      File save;
      if (issuedCommand.getFirstArgument().contains(SAVE_EXTENSION)) {
        save = new File(SAVES_FOLDER, issuedCommand.getFirstArgument());
      } else {
        save = new File(SAVES_FOLDER, issuedCommand.getFirstArgument() + SAVE_EXTENSION);
      }
      if (save.exists() && save.isFile()) {
        return loadFile(save);
      } else {
        IO.writeString(save.getName() + " does not exist or is not a file.");
        return null;
      }
    } else {
      // A save name was not provided.
      if (checkForDefaultSave()) {
        IO.writeString(Constants.FILE_FOUND);
        if (confirmOperation(LOAD_CONFIRM)) {
          return loadFile(new File(SAVES_FOLDER, DEFAULT_SAVE_NAME + SAVE_EXTENSION));
        }
        // There is a save. Do not save the new demo campaign.
        return new GameState();
      } else {
        // Could not find a saved campaign.
        // Instantiate a new demo campaign and save it to disk.
        GameState newGameState = new GameState();
        saveFile(newGameState, DEFAULT_SAVE_NAME, true);
        // Return the new campaign.
        return newGameState;
      }
    }
  }

  /**
   * Handles all the saving process.
   */
  public static void saveGame(GameState gameState) {
    if (confirmOperation(SAVE_CONFIRM)) {
      saveFile(gameState, DEFAULT_SAVE_NAME, false);
    }
  }

  /**
   * Handles all the saving process, assigning a new name for the save file, if provided.
   */
  public static void saveGame(GameState gameState, IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      if (confirmOperation(SAVE_CONFIRM)) {
        saveFile(gameState, issuedCommand.getFirstArgument(), false);
      }
    } else {
      saveGame(gameState);
    }
  }

  /**
   * Prompts the user to confirm an operation using a dialog window.
   */
  private static boolean confirmOperation(String confirmation) {
    int result = JOptionPane.showConfirmDialog(Game.getGameWindow(), confirmation, null, JOptionPane.YES_NO_OPTION);
    Game.getGameWindow().requestFocusOnTextField();
    return result == JOptionPane.YES_OPTION;
  }

  // The following two methods are the only methods that perform (de)serialization.
  // Loads game state from a file.
  private static GameState loadFile(File file) {
    FileInputStream fileInStream;
    ObjectInputStream objectInStream;
    try {
      fileInStream = new FileInputStream(file);
      objectInStream = new ObjectInputStream(fileInStream);
      GameState loadedGameState = (GameState) objectInStream.readObject();
      objectInStream.close();
      IO.writeString(LOAD_SUCCESS);
      IO.writeString("Read " + Utils.bytesToHuman(file.length()) + " from " + file.getName());
      loadedGameState.setSaved(true);
      return loadedGameState;
    } catch (IOException ex) {
      IO.writeString(LOAD_ERROR);
      return new GameState();
    } catch (ClassNotFoundException ex) {
      IO.writeString(LOAD_ERROR);
      return new GameState();
    }
  }

  // Serializes the current game state to a file.
  private static void saveFile(GameState state, String name, boolean quiet) {
    File file = new File(SAVES_FOLDER, name + SAVE_EXTENSION);
    FileOutputStream fileOutStream;
    ObjectOutputStream objectOutStream;
    try {
      if (!SAVES_FOLDER.exists()) {
        if (!SAVES_FOLDER.mkdir()) {
          Utils.printFailedToCreateDirectoryMessage(DEFAULT_SAVE_PATH);
          return;
        }
      }
      fileOutStream = new FileOutputStream(file);
      objectOutStream = new ObjectOutputStream(fileOutStream);
      objectOutStream.writeObject(state);
      objectOutStream.close();
      state.setSaved(true);
      if (!quiet) {
        IO.writeString(SAVE_SUCCESS);
        long bytes = file.length();
        IO.writeString("Wrote " + Utils.bytesToHuman(bytes) + " to " + file.getName());
      }
    } catch (IOException ex) {
      if (!quiet) {
        IO.writeString(SAVE_ERROR);
      }
    }
  }

}
