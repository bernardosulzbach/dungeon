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

import java.util.Random;

import org.dungeon.core.creatures.Creature;
import org.dungeon.core.creatures.Hero;
import org.dungeon.help.Help;
import org.dungeon.io.IO;
import org.dungeon.io.Loader;
import org.dungeon.utils.Constants;
import org.dungeon.utils.DateAndTime;
import org.dungeon.utils.Utils;

public class Game {

    /**
     * The Random object used to control random events throughout the game.
     */
    public static final Random RANDOM = new Random();

    public static void main(String[] args) {
        Campaign gameCampaign = Loader.loadGameRoutine();
        gameLoop(gameCampaign);
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
                    Loader.saveGameRoutine(campaign);
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
                case "go":
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
                        campaign.getBattleCounter().incrementCounter(target.getId());
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
                    Loader.saveGameRoutine(campaign, inputWords);
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
