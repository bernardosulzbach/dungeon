package org.dungeon.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.dungeon.core.game.Campaign;
import org.dungeon.utils.Constants;

public class Loader {

    /**
     * Check if a saved campaign exists.
     */
    private static boolean checkForExistingSave() {
        File savedCampaign = new File(Constants.SAVE_NAME + Constants.SAVE_EXTENSION);
        return savedCampaign.exists() && savedCampaign.isFile();
    }

    /**
     * Handles all the save loading at startup.
     *
     * @return a saved campaign or a new demo campaign.
     */
    public static Campaign loadGameRoutine() {
        if (checkForExistingSave()) {
            IO.writeString(Constants.FILE_FOUND);
            if (confirmOperation(Constants.LOAD_CONFIRM)) {
                return loadCampaign();
            }
        }
        return new Campaign();
    }

    /**
     * Handles all the saving process.
     */
    public static void saveGameRoutine(Campaign campaign) {
        if (confirmOperation(Constants.SAVE_CONFIRM)) {
            saveCampaign(campaign, Constants.SAVE_NAME);
        }
    }

    /**
     * Handles all the saving process, assigning a new name for the save file,
     * if provided.
     */
    public static void saveGameRoutine(Campaign campaign, String[] inputWords) {
        if (inputWords.length == 1) {
            saveGameRoutine(campaign);
        } else {
            if (confirmOperation(Constants.SAVE_CONFIRM)) {
                saveCampaign(campaign, inputWords[1]);
            }
        }
    }

    /**
     * Prompt the user to confirm an operation (as saving and loading the game).
     *
     * @param confirmation
     * @return
     */
    public static boolean confirmOperation(String confirmation) {
        IO.writeString(confirmation + " ( Y / N )");
        while (true) {
            String input = IO.readString().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                IO.writeString(Constants.INVALID_INPUT);
            }
        }
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
    private static void saveCampaign(Campaign campaign, String saveName) {
        FileOutputStream fileOutStream;
        ObjectOutputStream objectOutStream;
        try {
            fileOutStream = new FileOutputStream(saveName + Constants.SAVE_EXTENSION);
            objectOutStream = new ObjectOutputStream(fileOutStream);
            objectOutStream.writeObject(campaign);
            objectOutStream.close();
            campaign.setSaved(true);
            IO.writeString(Constants.SAVE_SUCCESS);
        } catch (IOException ex) {
            IO.writeString(Constants.SAVE_ERROR);
        }
    }

}
