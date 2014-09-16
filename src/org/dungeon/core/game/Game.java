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
package org.dungeon.core.game;

import org.dungeon.help.Help;
import org.dungeon.utils.Utils;

import java.io.*;
import java.util.Random;
import org.dungeon.utils.Constants;
import org.dungeon.utils.DateAndTime;

public class Game {

    /**
     * The Random object used to control random events throughout the game.
     */
    public static final Random RANDOM = new Random();

    public static void main(String[] args) {
        Campaign gameCampaign = loadGameRoutine();
        gameLoop(gameCampaign);
    }

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
    private static Campaign loadGameRoutine() {
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
     *
     * @return a saved campaign or a new demo campaign.
     */
    private static void saveGameRoutine(Campaign campaign) {
        if (confirmOperation(Constants.SAVE_CONFIRM)) {
            saveCampaign(campaign, Constants.SAVE_NAME);
        }
    }

    /**
     * Handles all the saving process, assigning a new name for the save file, if provided.
     *
     * @return a saved campaign or a new demo campaign.
     */
    private static void saveGameRoutine(Campaign campaign, String[] inputWords) {
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
    private static boolean confirmOperation(String confirmation) {
        IO.writeString(confirmation + " ( Y / N )");
        while (true) {
            switch (IO.readString().toLowerCase()) {
                case "y":
                case "yes":
                    return true;
                case "n":
                case "no":
                    return false;
                default:
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
        } catch (IOException | ClassNotFoundException ex) {
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

    /**
     * The main game loop. Continuously prompts the player for input.
     */
    private static void gameLoop(Campaign campaign) {
        Utils.printHeading();
        while (true) {
            // getTurn returns true if the Player did not issue an exit command.
            if (getTurn(campaign)) {
                // Handle player death.
                if (!campaign.getHero().isAlive()) {
                    IO.writeString("You died.");
                    break;
                }
                // Refresh the campaign state.
                campaign.refresh();
                // After a turn, the campaign is not saved.
                campaign.setSaved(false);
            } else {
                if (!campaign.isSaved()) {
                    saveGameRoutine(campaign);
                }
                break;
            }
        }
    }

    /**
     * Let the player play a turn. Many actions are not considered a turn (e.g.: look).
     *
     * @param campaign
     * @return false if the player issued an exit command. True if the player played a turn.
     */
    private static boolean getTurn(Campaign campaign) {
        String[] inputWords;
        while (true) {
            inputWords = IO.readWords();
            switch (inputWords[0].toLowerCase()) {
                // Hero-related commands.
                case "rest":
                    campaign.getHero().rest();
                    return true;
                case "look":
                case "peek":
                    campaign.getHero().look();
                    break;
                case "loot":
                case "pick":
                    campaign.getHero().pickWeapon(inputWords);
                    break;
                case "walk":
                    campaign.parseHeroWalk(inputWords);
                    break;
                case "drop":
                    campaign.getHero().dropWeapon();
                    break;
                case "destroy":
                case "crash":
                    campaign.getHero().destroyItem(inputWords);
                    break;
                case "status":
                    campaign.getHero().printAllStatus();
                    break;
                case "hero":
                case "me":
                    campaign.getHero().printHeroStatus();
                    break;
                case "weapon":
                    campaign.getHero().printWeaponStatus();
                    break;
                case "kill":
                case "attack":
                    Creature target = campaign.getHero().selectTarget(inputWords);
                    if (target != null) {
                        // Add this battle to the battle counter.
                        campaign.getBattleCounter().incrementCreatureCount(target.getId());
                        Game.battle(campaign.getHero(), target);
                    }
                    return true;
                // Campaign-related commands.
                case "achievements":
                    campaign.printUnlockedAchievements();
                    break;
                // World-related commands.
                case "spawns":
                    campaign.getWorld().printSpawnCounters();
                    break;
                // Utility commands.
                case "time":
                    DateAndTime.printTime();
                    break;
                case "date":
                    DateAndTime.printDate();
                    break;
                // Help commands.
                case "help":
                case "?":
                    Help.printCommandHelp(inputWords);
                    break;
                case "commands":
                    Help.printCommandList();
                    break;
                // Game commands.
                case "save":
                    saveGameRoutine(campaign, inputWords);
                    break;
                case "quit":
                case "exit":
                    return false;
                // The user issued a command, but it was not recognized.
                default:
                    if (!inputWords[0].isEmpty()) {
                        printInvalidCommandMessage(inputWords[0]);
                    } else {
                        // The user pressed enter without typing anything.
                        IO.writeString(Constants.INVALID_INPUT);
                    }
                    break;
            }
        }
    }

    /**
     * Prints a message reporting the usage of an invalid command.
     *
     * @param command
     */
    private static void printInvalidCommandMessage(String command) {
        IO.writeString(command + " is not a valid command.\nSee 'commands' for a list of valid commands.");
    }

    /**
     * Simulates a battle between two creatures.
     */
    private static void battle(Creature attacker, Creature defender) {
        if (attacker == defender) {
            IO.writeString("You cannot attempt suicide.");
            return;
        }
        while (attacker.isAlive() && defender.isAlive()) {
            attacker.hit(defender);
            if (defender.isAlive()) {
                defender.hit(attacker);
            }
        }
        Creature survivor;
        Creature defeated;
        if (attacker.isAlive()) {
            survivor = attacker;
            defeated = defender;
        } else {
            survivor = defender;
            defeated = attacker;
        }
        IO.writeString(survivor.getName() + " managed to kill " + defeated.getName() + ".");
        battleCleanup(survivor, defeated);
    }

    /**
     * Add the the surviving creature the gold and experience the defeated had.
     */
    private static void battleCleanup(Creature survivor, Creature defeated) {
        if (survivor instanceof Hero) {
            survivor.addExperience(defeated.getExperienceDrop());
            survivor.addGold(defeated.getGold());
        }
        // Remove the dead creature from the location.
        survivor.getLocation().removeCreature(defeated);
    }

}
