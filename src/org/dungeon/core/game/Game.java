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

import org.dungeon.core.creatures.Creature;
import org.dungeon.core.creatures.Hero;
import org.dungeon.help.Help;
import org.dungeon.io.IO;
import org.dungeon.io.Loader;
import org.dungeon.io.WriteStyle;
import org.dungeon.utils.*;

import java.util.Random;
import org.dungeon.gui.GameWindow;

public class Game {

    /** The Random object used to control random events throughout the game. */
    public static final Random RANDOM = new Random();

    /** The window to what the games write output and get user input. */
    public static GameWindow gameWindow;
    public static Campaign campaign;

    public static void main(String[] args) {

        boolean initializeHelp = true;

        // Enables the user not to load the help strings. This may improve performance and start-up speed.
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--no-help")) {
                initializeHelp = false;
            }
        }
        if (initializeHelp) {
            Help.initialize();
        }

        // Instantiate the game window.
        gameWindow = new GameWindow();

        campaign = Loader.loadGameRoutine();

        Utils.printHeading();
    }

    /**
     * Renders a turn based on an input string.
     *
     * @param inputString an input string provided by the player.
     * @return true if the game did not end. False otherwise.
     */
    public static boolean renderTurn(String inputString) {
        // Let the player play a turn and get its length (in seconds).
        int turnLength = processInput(inputString);
        // -1 is returned by getTurn when the player issues a quit command.
        if (turnLength == -1) {
            if (!campaign.isSaved()) {
                Loader.saveGameRoutine(campaign);
            }
            return false;
        }
        if (campaign.getHero().isDead()) {
            IO.writeString("You died.");
            // After the player's death, just prompt to load the default save file.
            campaign = Loader.loadGameRoutine();
        }
        // Advance the campaign's world date.
        campaign.getWorld().rollDate(turnLength);
        // Refresh the campaign state.
        campaign.refresh();
        // After a player's turn, the campaign is not saved anymore.
        campaign.setSaved(false);
        return true;
    }

    /**
     * Let the player play a turn. Many actions are not considered a turn (e.g.: look).
     * <p/>
     * Returns how many seconds the player's turn took. Returns -1 if the player issued a quit command.
     */
    private static int processInput(String inputString) {
        String firstWord;
        String[] inputWords;
        // Add the command the user entered to the campaign's command history.
        campaign.getCommandHistory().addCommand(inputString);
        // Split the command into words.
        inputWords = StringUtils.split(inputString);
        firstWord = inputWords[0].toLowerCase();
        if (firstWord.equals("rest")) {
            return campaign.getHero().rest();
        } //
        else if (firstWord.equals("look") || firstWord.equals("peek")) {
            campaign.getHero().look();
        } //
        else if (firstWord.equals("inventory") || firstWord.equals("items")) {
            campaign.getHero().printInventory();
        } //
        else if (firstWord.equals("loot") || firstWord.equals("pick")) {
            campaign.getHero().pickItem(inputWords);
            return 120;
        } //
        else if (firstWord.equals("equip")) {
            campaign.getHero().parseEquip(inputWords);
        } else if (firstWord.equals("unequip")) {
            campaign.getHero().unequipWeapon();
        } //
        else if (firstWord.equals("eat") || firstWord.equals("devour")) {
            campaign.getHero().eatItem(inputWords);
            return 120;
        } else if (firstWord.equals("walk") || firstWord.equals("go")) {
            return campaign.parseHeroWalk(inputWords);
        } //
        else if (firstWord.equals("drop")) {
            campaign.getHero().dropItem(inputWords);
        } //
        else if (firstWord.equals("destroy") || firstWord.equals("crash")) {
            campaign.getHero().destroyItem(inputWords);
            return 120;
        } //
        else if (firstWord.equals("status")) {
            campaign.getHero().printAllStatus();
        } //
        else if (firstWord.equals("hero") || firstWord.equals("me")) {
            campaign.getHero().printHeroStatus();
        } else if (firstWord.equals("age")) {
            campaign.getHero().printAge();
        } //
        else if (firstWord.equals("weapon")) {
            campaign.getHero().printWeaponStatus();
        } //
        else if (firstWord.equals("kill") || firstWord.equals("attack")) {
            Creature target = campaign.getHero().selectTarget(inputWords);
            if (target != null) {
                // Add this battle to the battle counter.
                int lastBattleTurns = Game.battle(campaign.getHero(), target);
                // A battle turn takes half a minute.
                return lastBattleTurns * 30;
            }
        } // Campaign-related commands.
        //
        else if (firstWord.equals("commandcount")) { // TODO: think of a better name for this.
            campaign.printCommandCount();
        } //
        else if (firstWord.equals("whoami")) {
            IO.writeString(campaign.getHeroInfo());
        } //
        else if (firstWord.equals("whereami")) {
            IO.writeString(campaign.getHeroPosition().toString());
        } //
        else if (firstWord.equals("achievements")) {
            campaign.printUnlockedAchievements();
            // World-related commands.
        } //
        else if (firstWord.equals("now")) {
            campaign.getHero().printDateAndTime();
        } //
        else if (firstWord.equals("spawns")) {
            campaign.getWorld().printSpawnCounters();
            // Utility commands.
        } //
        else if (firstWord.equals("time")) {
            DateAndTime.printTime();
        } //
        else if (firstWord.equals("date")) {
            DateAndTime.printDate();
            // Help commands.
        } //
        else if (firstWord.equals("help") || firstWord.equals("?")) {
            Help.printHelp(inputWords);
        } //
        else if (firstWord.equals("commands")) {
            Help.printCommandList();
            // Game commands.
        } //
        else if (firstWord.equals("save")) {
            Loader.saveGameRoutine(campaign, inputWords);
        } //
        else if (firstWord.equals("quit") || firstWord.equals("exit")) {
            return -1;
        } //
        else if (firstWord.equals("credits") || firstWord.equals("about")) {
            Utils.printCredits();
        } //
        else if (firstWord.equals("license") || firstWord.equals("copyright")) {
            LicenseUtils.printLicense();
        } //
        else if (firstWord.equals("hint")) {
            campaign.printNextHint();
        } //
        else if (firstWord.equals("version")) {
            Utils.printVersion();
            // The user issued a command, but it was not recognized.
        } else if (firstWord.equals("clear")) {
            gameWindow.clearTextPane();
        } else {
            Utils.printInvalidCommandMessage(inputWords[0]);
        }
        return 0;
    }

    /**
     * Simulates a battle between a Hero and a Creature.
     * <p/>
     * Returns the number of turns the battle had.
     */
    private static int battle(Hero attacker, Creature defender) {
        if (attacker == defender) {
            // Two different messages.
            if (RANDOM.nextBoolean()) {
                IO.writeString(Constants.SUICIDE_ATTEMPT_1);
            } else {
                IO.writeString(Constants.SUICIDE_ATTEMPT_2);
            }
            return 0;
        }
        /**
         * A counter variable that register how many turns the battle had.
         */
        int turns = 0;
        while (attacker.isAlive() && defender.isAlive()) {
            attacker.hit(defender);
            turns++;
            if (defender.isAlive()) {
                defender.hit(attacker);
                turns++;
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
        // Add information about this battle to the Hero's battle log.
        attacker.getBattleLog().addBattle(attacker, defender, attacker == survivor, turns);
        battleCleanup(survivor, defeated);
        return turns;
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
