package game;

import help.Help;
import utils.Utils;

import java.io.*;
import java.util.Random;
import java.util.Scanner;
import utils.Constants;
import utils.DateAndTime;

public class Game {

    /**
     * The Scanner the method Game.readString() uses.
     *
     */
    public static final Scanner SCANNER = new Scanner(System.in);

    /**
     * The Random object used to control random events throughout the game.
     */
    public static final Random RANDOM = new Random();

    /**
     * The string used to alert the player about invalid input.
     */
    public static final String INVALID_INPUT = "Invalid input.";

    public static void main(String[] args) {
        Campaign gameCampaign = loadGameRoutine();
        gameLoop(gameCampaign);
    }

    /**
     * Check if a saved campaign exists.
     */
    private static boolean checkForExistingSave() {
        File savedCampaign = new File(Constants.CAMPAIGN_PATH);
        return savedCampaign.exists() && savedCampaign.isFile();
    }

    /**
     * Handles all the save loading at startup.
     *
     * @return a saved campaign or a new demo campaign.
     */
    private static Campaign loadGameRoutine() {
        if (checkForExistingSave()) {
            IO.writeString(Constants.SAVE_FOUND);
            if (confirmLoad()) {
                return loadCampaign();
            }
        }
        return new Campaign();
    }

    /**
     * Handles all the save loading at startup.
     *
     * @return a saved campaign or a new demo campaign.
     */
    private static void saveGameRoutine(Campaign campaign) {
        if (confirmSave()) {
            saveCampaign(campaign);
        }
    }

    /**
     * Prompts the user if he/she wants to attempt to load a saved campaign.
     *
     * @return true if the answer was positive, false otherwise.
     */
    private static boolean confirmLoad() {
        IO.writeString("Attempt to load it? ( Y / N )");
        while (true) {
            switch (IO.readString().toLowerCase()) {
                case "y":
                case "yes":
                    return true;
                case "n":
                case "no":
                    return false;
                default:
                    IO.writeString(Game.INVALID_INPUT);
            }
        }
    }

    /**
     * Prompts the user if he/she wants to attempt to save the current state of his/hers campaign.
     */
    private static boolean confirmSave() {
        IO.writeString("Save the game? ( Y / N )");
        while (true) {
            switch (IO.readString().toLowerCase()) {
                case "y":
                case "yes":
                    return true;
                case "n":
                case "no":
                    return false;
                default:
                    IO.writeString(Game.INVALID_INPUT);
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
            fileInStream = new FileInputStream(Constants.CAMPAIGN_PATH);
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
    private static void saveCampaign(Campaign campaign) {
        FileOutputStream fileOutStream;
        ObjectOutputStream objectOutStream;
        try {
            fileOutStream = new FileOutputStream(Constants.CAMPAIGN_PATH);
            objectOutStream = new ObjectOutputStream(fileOutStream);
            objectOutStream.writeObject(campaign);
            objectOutStream.close();
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
                // Stop if the player died.
                if (!campaign.getHero().isAlive()) {
                    break;
                }
            } else {
                saveGameRoutine(campaign);
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
                        Game.battle(campaign.getHero(), target);
                        if (!campaign.getHero().isAlive()) {
                            System.out.println("You died.");
                        }
                    }
                    return true;
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
                    saveGameRoutine(campaign);
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
                        IO.writeString(Game.INVALID_INPUT);
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
        survivor.addExperience(defeated.getExperienceDrop());
        survivor.addGold(defeated.getGold());
        // Remove the dead creature from the location.
        survivor.getLocation().removeAllDeadCreatures();
    }

}
