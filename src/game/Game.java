package game;

import help.Help;
import utils.Utils;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private static final String TITLE = "Dungeon";

    private static final String CAMPAIGN_PATH = "campaign.dungeon";

    private static final String SAVE_ERROR = "Could not save the game.";
    private static final String LOAD_ERROR = "Could not load a saved game.";

    // The Scanner the method Game.readString() uses.
    public static final Scanner SCANNER = new Scanner(System.in);

    // The Random object used to control random events.
    public static final Random RANDOM = new Random();

    /**
     * The string used to alert the player about invalid input.
     */
    public static final String INVALID_INPUT = "Invalid input.";

    // Two 79-character long strings used to improve readability.
    public static final String LINE_1 = Utils.makeRepeatedCharacterString(79, '-');
    public static final String LINE_2 = Utils.makeRepeatedCharacterString(79, '=');

    public static void main(String[] args) {
        gameLoop(new Campaign());
    }

    /**
     * Prompts the user if he/she wants to attempt to load a serialized World object.
     *
     * @return true if the answer was positive, false otherwise.
     */
    private static boolean promptAttemptToLoad() {
        Game.writeString("Attempt to load a saved world? ( Y / N )");
        while (true) {
            String input = Game.readString();
            switch (input.toLowerCase()) {
                case "y":
                case "yes":
                    return true;
                case "n":
                case "no":
                    return false;
                default:
                    Game.writeString(Game.INVALID_INPUT);
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
            fileInStream = new FileInputStream(CAMPAIGN_PATH);
            objectInStream = new ObjectInputStream(fileInStream);
            Campaign loadedCampaign = (Campaign) objectInStream.readObject();
            objectInStream.close();
            return loadedCampaign;
        } catch (IOException | ClassNotFoundException ex) {
            Game.writeString(LOAD_ERROR);
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
            fileOutStream = new FileOutputStream(CAMPAIGN_PATH);
            objectOutStream = new ObjectOutputStream(fileOutStream);
            objectOutStream.writeObject(campaign);
            objectOutStream.close();
        } catch (IOException ex) {
            Game.writeString(SAVE_ERROR);
        }
    }

    /**
     * The main game loop. Continuously prompts the player for input.
     */
    private static void gameLoop(Campaign campaign) {
        // Print the game heading.
        Game.writeString(LINE_2);
        Game.writeString(TITLE);
        Game.writeString(LINE_2);

        // Enter the main game loop.
        while (true) {
            // getTurn returns true if the Player did not issue an exit command.
            if (getTurn(campaign)) {
                // Stop if the player died.
                if (!campaign.getHero().isAlive()) {
                    break;
                }
            } else {
                saveCampaign(campaign);
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
            inputWords = Game.readWords();
            switch (inputWords[0].toLowerCase()) {
                case "look":
                case "peek":
                    campaign.getHero().look();
                    break;
                case "drop":
                    campaign.getHero().dropWeapon();
                    break;
                case "spawns":
                    campaign.getWorld().printSpawnCounters();
                    break;
                case "loot":
                case "pick":
                    campaign.getHero().pickWeapon(inputWords);
                    break;
                case "hero":
                case "char":
                    campaign.getHero().printHeroStatus();
                case "weapon":
                    campaign.getHero().printWeaponStatus();
                    break;
                case "destroy":
                    campaign.getHero().destroyItem(inputWords);
                    break;
                case "rest":
                    campaign.getHero().rest();
                    return true;
                case "status":
                    campaign.getHero().printAllStatus();
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
                case "time":
                    Utils.printTime();
                    break;
                case "date":
                    Utils.printDate();
                    break;
                case "help":
                case "?":
                    Help.printCommandHelp(inputWords);
                    break;
                case "commands":
                    Help.printCommandList();
                    break;
                case "quit":
                case "exit":
                    return false;
                default:
                    // The user issued a command, but it was not recognized.
                    if (!inputWords[0].isEmpty()) {
                        printInvalidCommandMessage(inputWords[0]);
                    } else {
                        // The user just pressed Enter.
                        Game.writeString(INVALID_INPUT);
                    }
                    break;
            }
        }
    }

    private static void printInvalidCommandMessage(String command) {
        Game.writeString(command + " is not a valid command.\nSee 'commands' for a list of valid commands.");
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
        Game.writeString(survivor.getName() + " managed to kill " + defeated.getName() + ".");
        aftermath(survivor, defeated);
    }

    /**
     * Add the the surviving creature the gold and experience the defeated had.
     */
    private static void aftermath(Creature survivor, Creature defeated) {
        survivor.addExperience(defeated.getExperienceDrop());
        survivor.addGold(defeated.getGold());
        survivor.getLocation().removeAllDeadCreatures();
    }

    /**
     * Read a line of input from the user and returns an array with the words in that line.
     *
     * @return a String array.
     */
    public static String[] readWords() {
        return readString().split("\\s+");
    }

    /**
     * Read a line of input from the user.
     *
     * @return
     */
    public static String readString() {
        String line;
        do {
            System.out.print("> ");
            line = SCANNER.nextLine().trim();
        } while (line.equals(""));
        return line;
    }

    /**
     * Outputs a string to the console, stripping unnecessary newlines at the end.
     *
     * @param string the string to be printed.
     */
    public static void writeString(String string) {
        while (string.endsWith("\n")) {
            // Remove the newline.
            string = string.substring(0, string.length() - 1);
        }
        System.out.println(string);
    }

}
