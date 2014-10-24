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

import org.dungeon.core.game.GameState;
import org.dungeon.utils.Constants;

import java.io.*;
import javax.swing.JOptionPane;
import org.dungeon.core.game.Game;

public class Loader {

    /**
     * Check if a saved campaign exists.
     */
    // TODO: implement this method with an argument for the save name.
    private static boolean checkForExistingSave() {
        File savedCampaign = new File(Constants.SAVE_NAME + Constants.SAVE_EXTENSION);
        return savedCampaign.exists() && savedCampaign.isFile();
    }

    /**
     * Handles all the save loading at startup.
     *
     * @return a saved campaign or a new demo campaign.
     */
    // TODO: add support to load 'filename' syntax.
    public static GameState loadGameRoutine() {
        if (checkForExistingSave()) {
            IO.writeString(Constants.FILE_FOUND);
            if (confirmOperation(Constants.LOAD_CONFIRM)) {
                return loadCampaign();
            }
            // There is a save. Do not save the new demo campaign.
            return new GameState();
        } else {
            // Could not find a saved campaign.
            // Instantiate a new demo campaign and save it to disk.
            GameState demoCampaign = new GameState();
            saveCampaign(demoCampaign, Constants.SAVE_NAME, true);
            // Return the new campaign.
            return demoCampaign;
        }
    }

    /**
     * Handles all the saving process.
     */
    public static void saveGameRoutine(GameState gameState) {
        if (confirmOperation(Constants.SAVE_CONFIRM)) {
            saveCampaign(gameState, Constants.SAVE_NAME, false);
        }
    }

    /**
     * Handles all the saving process, assigning a new name for the save file, if provided.
     */
    public static void saveGameRoutine(GameState gameState, String[] inputWords) {
        if (inputWords.length == 1) {
            saveGameRoutine(gameState);
        } else {
            if (confirmOperation(Constants.SAVE_CONFIRM)) {
                saveCampaign(gameState, inputWords[1], false);
            }
        }
    }

    /**
     * Prompts the user to confirm an operation using a dialog window.
     */
    private static boolean confirmOperation(String confirmation) {
        int result = JOptionPane.showConfirmDialog(Game.gameWindow, confirmation, null, JOptionPane.YES_NO_OPTION);
        Game.gameWindow.requestFocusOnTextField();
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Attempts to load a serialized Campaign object.
     */
    private static GameState loadCampaign() {
        FileInputStream fileInStream;
        ObjectInputStream objectInStream;
        try {
            fileInStream = new FileInputStream(Constants.SAVE_NAME + Constants.SAVE_EXTENSION);
            objectInStream = new ObjectInputStream(fileInStream);
            GameState loadedGameState = (GameState) objectInStream.readObject();
            objectInStream.close();
            IO.writeString(Constants.LOAD_SUCCESS);
            return loadedGameState;
        } catch (IOException ex) {
            IO.writeString(Constants.LOAD_ERROR);
            return new GameState();
        } catch (ClassNotFoundException ex) {
            IO.writeString(Constants.LOAD_ERROR);
            return new GameState();
        }
    }

    /**
     * Saves a Campaign object to a file.
     */
    private static void saveCampaign(GameState gameState, String saveName, boolean quiet) {
        FileOutputStream fileOutStream;
        ObjectOutputStream objectOutStream;
        try {
            fileOutStream = new FileOutputStream(saveName + Constants.SAVE_EXTENSION);
            objectOutStream = new ObjectOutputStream(fileOutStream);
            objectOutStream.writeObject(gameState);
            objectOutStream.close();
            gameState.setSaved(true);
            if (!quiet) {
                IO.writeString(Constants.SAVE_SUCCESS);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            if (!quiet) {
                IO.writeString(Constants.SAVE_ERROR);
            }
        }
    }

}
