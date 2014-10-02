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

public class Game {

    /**
     * The Random object used to control random events throughout the game.
     */
    public static final Random RANDOM = new Random();

    public static void main(String[] args) {
        Help.initializeCommandList();
        Campaign gameCampaign = Loader.loadGameRoutine();
        gameLoop(gameCampaign);
    }

    /**
     * The main game loop. Continuously prompts the player for input.
     */
    private static void gameLoop(Campaign campaign) {
        Utils.printHeading();
        while (true) {
            // Let the player play a turn and get its length (in seconds).
            int turnLength = getTurn(campaign);
            // -1 is returned by getTurn when the player issues a quit command.
            if (turnLength == -1) {
                if (!campaign.isSaved()) {
                    Loader.saveGameRoutine(campaign);
                }
                break;
            }
            if (campaign.getHero().isDead()) {
                IO.writeString("You died.", WriteStyle.MARGIN);
                // After the player's death, just prompt to load the default save file.
                campaign = Loader.loadGameRoutine();
                continue;
            }
            // Advance the campaign's world date.
            campaign.getWorld().rollDate(turnLength);
            // Refresh the campaign state.
            campaign.refresh();
            // After a turn, the campaign is not saved anymore.
            campaign.setSaved(false);
        }
    }

    /**
     * Let the player play a turn. Many actions are not considered a turn (e.g.: look).
     * <p/>
     * Returns how many seconds the player's turn took. Returns -1 if the player issued a quit command.
     */
    private static int getTurn(Campaign campaign) {
        String s;
        String inputString;
        String[] inputWords;
        while (true) {
            // IO.readString() never returns a blank string.
            inputString = IO.readString();
            // Add the command the user entered to the campaign's command history.
            campaign.getCommandHistory().addCommand(inputString);
            // Split the command into words.
            inputWords = StringUtils.split(inputString);
            s = inputWords[0].toLowerCase();
            if (s.equals("rest")) {
                return campaign.getHero().rest();
            }
            //
            else if (s.equals("look") || s.equals("peek")) {
                campaign.getHero().look();
            }
            //
            else if (s.equals("inventory") || s.equals("items")) {
                campaign.getHero().printInventory();
            }
            //
            else if (s.equals("loot") || s.equals("pick")) {
                campaign.getHero().pickItem(inputWords);
                return 120;
            }
            //
            else if (s.equals("equip")) {
                campaign.getHero().parseEquip(inputWords);
            } else if (s.equals("unequip")) {
                campaign.getHero().unequipWeapon();
            }
            //
            else if (s.equals("eat") || s.equals("devour")) {
                campaign.getHero().eatItem(inputWords);
                return 120;
            } else if (s.equals("walk") || s.equals("go")) {
                return campaign.parseHeroWalk(inputWords);
            }
            //
            else if (s.equals("drop")) {
                campaign.getHero().dropItem(inputWords);
            }
            //
            else if (s.equals("destroy") || s.equals("crash")) {
                campaign.getHero().destroyItem(inputWords);
                return 120;
            }
            //
            else if (s.equals("status")) {
                campaign.getHero().printAllStatus();
            }
            //
            else if (s.equals("hero") || s.equals("me")) {
                campaign.getHero().printHeroStatus();
            }
            else if (s.equals("age")) {
                campaign.getHero().printAge();
            }
            //
            else if (s.equals("weapon")) {
                campaign.getHero().printWeaponStatus();
            }
            //
            else if (s.equals("kill") || s.equals("attack")) {
                Creature target = campaign.getHero().selectTarget(inputWords);
                if (target != null) {
                    // Add this battle to the battle counter.
                    int lastBattleTurns = Game.battle(campaign.getHero(), target);
                    // A battle turn takes half a minute.
                    return lastBattleTurns * 30;
                }
            }
            // Campaign-related commands.
            //
            else if (s.equals("commandcount")) { // TODO: think of a better name for this.
                campaign.printCommandCount();
            }
            //
            else if (s.equals("whoami")) {
                IO.writeString(campaign.getHeroInfo(), WriteStyle.MARGIN);
            }
            //
            else if (s.equals("whereami")) {
                IO.writeString(campaign.getHeroPosition().toString(), WriteStyle.MARGIN);
            }
            //
            else if (s.equals("achievements")) {
                campaign.printUnlockedAchievements();
                // World-related commands.
            }
            //
            else if (s.equals("now")) {
                campaign.getWorld().printDateAndTime();
            }
            //
            else if (s.equals("spawns")) {
                campaign.getWorld().printSpawnCounters();
                // Utility commands.
            }
            //
            else if (s.equals("time")) {
                DateAndTime.printTime();
            }
            //
            else if (s.equals("date")) {
                DateAndTime.printDate();
                // Help commands.
            }
            //
            else if (s.equals("help") || s.equals("?")) {
                Help.printCommandHelp(inputWords);
            }
            //
            else if (s.equals("commands")) {
                Help.printCommandList();
                // Game commands.
            }
            //
            else if (s.equals("save")) {
                Loader.saveGameRoutine(campaign, inputWords);
            }
            //
            else if (s.equals("quit") || s.equals("exit")) {
                return -1;
            }
            //
            else if (s.equals("credits") || s.equals("about")) {
                Utils.printCredits();
            }
            //
            else if (s.equals("license") || s.equals("copyright")) {
                LicenseUtils.printLicense();
            }
            //
            else if (s.equals("hint") || s.equals("tip")) {
                campaign.printNextHint();
            }
            //
            else if (s.equals("version")) {
                Utils.printVersion();
                // The user issued a command, but it was not recognized.
            }
            //
            else {
                Utils.printInvalidCommandMessage(inputWords[0]);
            }
        }
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
                IO.writeString(Constants.SUICIDE_ATTEMPT_1, WriteStyle.MARGIN);
            } else {
                IO.writeString(Constants.SUICIDE_ATTEMPT_2, WriteStyle.MARGIN);
            }
            return 0;
        }
        /** A counter variable that register how many turns the battle had. */
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
        IO.writeString(survivor.getName() + " managed to kill " + defeated.getName() + ".", WriteStyle.MARGIN);
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
