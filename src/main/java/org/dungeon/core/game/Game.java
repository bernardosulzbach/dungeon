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
import org.dungeon.gui.GameWindow;
import org.dungeon.help.Help;
import org.dungeon.io.DLogger;
import org.dungeon.io.IO;
import org.dungeon.io.Loader;
import org.dungeon.utils.*;
import org.dungeon.utils.Math;

import java.awt.*;
import java.util.Random;

public class Game {

    // The single Random object used by all the methods.
    public static final Random RANDOM = new Random();

    private static final LastInputProcessResult lastInputProcessResult = new LastInputProcessResult();

    private static GameWindow gameWindow;
    private static GameState gameState;

    public static void main(String[] args) {

        boolean noHelp = false, noLog = false, noPoems = false;

        for (String arg : args) {
            if (arg.equalsIgnoreCase("--no-help")) {
                noHelp = true;
            } else if (arg.equalsIgnoreCase("--no-poems")) {
                noPoems = true;
            } else if (arg.equalsIgnoreCase("--no-log")) {
                noLog = true;
            }
        }

        if (!noHelp) {
            Help.initialize();
        }

        if (!noLog) {
            DLogger.initialize();
        }

        GameData.loadGameData(noPoems);

        gameWindow = new GameWindow();

        gameState = Loader.loadGame(null);
    }

    public static GameWindow getGameWindow() {
        return gameWindow;
    }

    public static GameState getGameState() {
        return gameState;
    }

    // Renders a turn based on an input string.
    public static void renderTurn(String inputString) {
        // Clears the text pane.
        getGameWindow().clearTextPane();
        processInput(inputString);
        if (gameState.getHero().isDead()) {
            IO.writeString("You died.");
            // After the player's death, just prompt to load the default save file.
            gameState = Loader.loadGame(null);
        }
        // Advance the campaign's world date.
        gameState.getWorld().rollDate(lastInputProcessResult.turnLength);
        // Refresh the campaign state.
        Engine.refresh();
        // After a turn that consumed time, the campaign is not saved anymore.
        if (lastInputProcessResult.turnLength != 0 || lastInputProcessResult.configurationsChanged) {
            gameState.setSaved(false);
        }
    }

    // Processes the player input.
    private static void processInput(String inputString) {
        String firstWord;
        String[] inputWords;
        // Add the command the user entered to the campaign's command history.
        gameState.getCommandHistory().addCommand(inputString);
        // Split the command into words.
        inputWords = Utils.split(inputString);
        firstWord = inputWords[0].toLowerCase();
        lastInputProcessResult.reset();
        if (firstWord.equals("rest")) {
            lastInputProcessResult.turnLength = gameState.getHero().rest();
        } else if (firstWord.equals("look") || firstWord.equals("peek")) {
            gameState.getHero().look();
        } else if (firstWord.equals("inventory") || firstWord.equals("items")) {
            gameState.getHero().printInventory();
        } else if (firstWord.equals("loot") || firstWord.equals("pick")) {
            gameState.getHero().pickItem(inputWords);
            lastInputProcessResult.turnLength = 120;
        } else if (firstWord.equals("equip")) {
            gameState.getHero().parseEquip(inputWords);
        } else if (firstWord.equals("unequip")) {
            gameState.getHero().unequipWeapon();
        } else if (firstWord.equals("eat") || firstWord.equals("devour")) {
            gameState.getHero().eatItem(inputWords);
            lastInputProcessResult.turnLength = 120;
        } else if (firstWord.equals("walk") || firstWord.equals("go")) {
            lastInputProcessResult.turnLength = Engine.parseHeroWalk(inputWords);
        } else if (firstWord.equals("drop")) {
            gameState.getHero().dropItem(inputWords);
        } else if (firstWord.equals("destroy") || firstWord.equals("crash")) {
            gameState.getHero().destroyItem(inputWords);
            lastInputProcessResult.turnLength = 120;
        } else if (firstWord.equals("status")) {
            gameState.getHero().printAllStatus();
        } else if (firstWord.equals("hero") || firstWord.equals("me")) {
            gameState.getHero().printHeroStatus();
        } else if (firstWord.equals("age")) {
            gameState.getHero().printAge();
        } else if (firstWord.equals("weapon")) {
            gameState.getHero().printWeaponStatus();
        } else if (firstWord.equals("kill") || firstWord.equals("attack")) {
            lastInputProcessResult.turnLength = gameState.getHero().attackTarget(inputWords);
        } else if (firstWord.equals("statistics")) {
            gameState.printGameStatistics();
        } else if (firstWord.equals("achievements")) {
            gameState.printUnlockedAchievements();
        } else if (firstWord.equals("spawns")) {
            gameState.getWorld().printSpawnCounters();
        } else if (firstWord.equals("time") || firstWord.equals("date")) {
            lastInputProcessResult.turnLength = gameState.getHero().printDateAndTime();
        } else if (firstWord.equals("system")) {
            SystemInfo.printSystemInfo();
        } else if (firstWord.equals("help") || firstWord.equals("?")) {
            Help.printHelp(inputWords);
        } else if (firstWord.equals("commands")) {
            Help.printCommandList(inputWords);
        } else if (firstWord.equals("save")) {
            Loader.saveGame(gameState, inputWords);
        } else if (firstWord.equals("load")) {
            GameState loadedGameState = Loader.loadGame(inputWords);
            if (loadedGameState != null) {
                gameState = loadedGameState;
            }
        } else if (firstWord.equals("quit") || firstWord.equals("exit")) {
            Game.exit();
        } else if (firstWord.equals("license") || firstWord.equals("copyright")) {
            LicenseUtils.printLicense();
        } else if (firstWord.equals("fibonacci")) {
            if (inputWords.length > 1) {
                Math.fibonacci(inputWords[1]);
            }
        } else if (firstWord.equals("hint") || firstWord.equals("tip")) {
            gameState.printNextHint();
        } else if (firstWord.equals("poem")) {
            gameState.printNextPoem();
        } else if (firstWord.equals("version")) {
            Utils.printVersion();
        } else if (firstWord.equals("debug")) {
            DebugTools.parseDebugCommand(inputWords);
        } else if (firstWord.equals("config")) {
            lastInputProcessResult.configurationsChanged = ConfigTools.parseConfigCommand(inputWords);
        } else {
            // The user issued a command, but it was not recognized.
            Utils.printInvalidCommandMessage(inputWords[0]);
        }
    }

    // Simulates a battle between a Hero and a Creature and returns the number of turns the battle had.
    public static int battle(Hero attacker, Creature defender) {
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
        IO.writeString(String.format("%s managed to kill %s.", survivor.getName(), defeated.getName()), Color.CYAN);
        // Add information about this battle to the Hero's battle log.
        attacker.getBattleStatistics().addBattle(attacker, defender, attacker == survivor, turns);
        attacker.getExplorationLog().addKill(getGameState().getHeroPosition());
        battleCleanup(survivor, defeated);
        return turns;
    }

    // Add the the surviving creature some experience and removes the dead creature from the battle location.
    private static void battleCleanup(Creature survivor, Creature defeated) {
        if (survivor instanceof Hero) {
            survivor.addExperience(defeated.getExperienceDrop());
        }
        // Remove the dead creature from the location.
        survivor.getLocation().removeCreature(defeated);
    }

    // Exits the game, prompting the user if the current state should be saved if it is not already saved.
    public static void exit() {
        if (!gameState.isSaved()) {
            Loader.saveGame(gameState);
        }
        DLogger.finest("Exited with no problems.");
        System.exit(0);
    }

}

class LastInputProcessResult {

    public int turnLength = 0;
    public boolean configurationsChanged = false;

    void reset() {
        turnLength = 0;
        configurationsChanged = false;
    }

}