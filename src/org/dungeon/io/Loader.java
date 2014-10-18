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

import org.dungeon.core.game.Campaign;
import org.dungeon.utils.Constants;

import java.io.*;
import javax.swing.JOptionPane;

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
    public static Campaign loadGameRoutine() {
        if (checkForExistingSave()) {
            IO.writeString(Constants.FILE_FOUND);
            if (confirmOperation(Constants.LOAD_CONFIRM)) {
                return loadCampaign();
            }
            // There is a save. Do not save the new demo campaign.
            return new Campaign();
        } else {
            // Could not find a saved campaign.
            // Instantiate a new demo campaign and save it to disk.
            Campaign demoCampaign = new Campaign();
            saveCampaign(demoCampaign, Constants.SAVE_NAME, true);
            // Return the new campaign.
            return demoCampaign;
        }
    }

    /**
     * Handles all the saving process.
     */
    public static void saveGameRoutine(Campaign campaign) {
        if (confirmOperation(Constants.SAVE_CONFIRM)) {
            saveCampaign(campaign, Constants.SAVE_NAME, false);
        }
    }

    /**
     * Handles all the saving process, assigning a new name for the save file, if provided.
     */
    public static void saveGameRoutine(Campaign campaign, String[] inputWords) {
        if (inputWords.length == 1) {
            saveGameRoutine(campaign);
        } else {
            if (confirmOperation(Constants.SAVE_CONFIRM)) {
                saveCampaign(campaign, inputWords[1], false);
            }
        }
    }

    /**
     * Prompts the user to confirm an operation using a dialog window.
     */
    public static boolean confirmOperation(String confirmation) {
        return JOptionPane.showConfirmDialog(null, confirmation, "Confirm operation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    /**
     * Attempts to load a serialized Campaign object.
     */
    private static Campaign loadCampaign() {
        FileInputStream fileInStream;
        ObjectInputStream objectInStream;
        try {
            fileInStream = new FileInputStream(Constants.SAVE_NAME + Constants.SAVE_EXTENSION);
            objectInStream = new ObjectInputStream(fileInStream);
            Campaign loadedCampaign = (Campaign) objectInStream.readObject();
            objectInStream.close();
            IO.writeString(Constants.LOAD_SUCCESS);
            return loadedCampaign;
        } catch (IOException ex) {
            IO.writeString(Constants.LOAD_ERROR);
            return new Campaign();
        } catch (ClassNotFoundException ex) {
            IO.writeString(Constants.LOAD_ERROR);
            return new Campaign();
        }
    }

    /**
     * Saves a Campaign object to a file.
     */
    private static void saveCampaign(Campaign campaign, String saveName, boolean quiet) {
        FileOutputStream fileOutStream;
        ObjectOutputStream objectOutStream;
        try {
            fileOutStream = new FileOutputStream(saveName + Constants.SAVE_EXTENSION);
            objectOutStream = new ObjectOutputStream(fileOutStream);
            objectOutStream.writeObject(campaign);
            objectOutStream.close();
            campaign.setSaved(true);
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
