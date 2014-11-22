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

import org.dungeon.gui.GameWindow;
import org.dungeon.help.Help;
import org.dungeon.io.DLogger;
import org.dungeon.io.IO;
import org.dungeon.io.Loader;
import org.dungeon.utils.LicenseUtils;
import org.dungeon.utils.Math;
import org.dungeon.utils.SystemInfo;
import org.dungeon.utils.Utils;

public class Game {

    private static int turnLength = 0;
    private static boolean configurationsChanged = false;

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
    public static void renderTurn(Command command) {
        // Clears the text pane.
        getGameWindow().clearTextPane();
        processInput(command);
        if (gameState.getHero().isDead()) {
            IO.writeString("You died.");
            // After the player's death, just prompt to load the default save file.
            gameState = Loader.loadGame(null);
        } else {
            // Advance the campaign's world date.
            gameState.getWorld().rollDate(turnLength);
            // Refresh the campaign state.
            Engine.refresh();
            // After a turn that consumed time, the campaign is not saved anymore.
            if (turnLength != 0 || configurationsChanged) {
                gameState.setSaved(false);
            }
        }
    }

    // Processes the player input.
    private static void processInput(Command command) {
        gameState.getCommandHistory().addCommand(command);
        // Reset the turn variables.
        turnLength = 0;
        configurationsChanged = false;
        if (command.firstTokenEquals("rest")) {
            turnLength = gameState.getHero().rest();
        } else if (command.firstTokenEquals("sleep")) {
            turnLength = gameState.getHero().sleep();
        } else if (command.firstTokenEquals("look") || command.firstTokenEquals("peek")) {
            gameState.getHero().look();
        } else if (command.firstTokenEquals("inventory") || command.firstTokenEquals("items")) {
            gameState.getHero().printInventory();
        } else if (command.firstTokenEquals("loot") || command.firstTokenEquals("pick")) {
            gameState.getHero().pickItem(command);
            turnLength = 120;
        } else if (command.firstTokenEquals("equip")) {
            gameState.getHero().parseEquip(command);
        } else if (command.firstTokenEquals("unequip")) {
            gameState.getHero().unequipWeapon();
        } else if (command.firstTokenEquals("eat") || command.firstTokenEquals("devour")) {
            gameState.getHero().eatItem(command);
            turnLength = 120;
        } else if (command.firstTokenEquals("walk") || command.firstTokenEquals("go")) {
            turnLength = Engine.parseHeroWalk(command);
        } else if (command.firstTokenEquals("drop")) {
            gameState.getHero().dropItem(command);
        } else if (command.firstTokenEquals("destroy") || command.firstTokenEquals("crash")) {
            gameState.getHero().destroyItem(command);
            turnLength = 120;
        } else if (command.firstTokenEquals("status")) {
            gameState.getHero().printAllStatus();
        } else if (command.firstTokenEquals("hero") || command.firstTokenEquals("me")) {
            gameState.getHero().printHeroStatus();
        } else if (command.firstTokenEquals("age")) {
            gameState.getHero().printAge();
        } else if (command.firstTokenEquals("weapon")) {
            gameState.getHero().printWeaponStatus();
        } else if (command.firstTokenEquals("kill") || command.firstTokenEquals("attack")) {
            turnLength = gameState.getHero().attackTarget(command);
        } else if (command.firstTokenEquals("statistics")) {
            gameState.printGameStatistics();
        } else if (command.firstTokenEquals("achievements")) {
            gameState.printUnlockedAchievements();
        } else if (command.firstTokenEquals("spawns")) {
            gameState.getWorld().printSpawnCounters();
        } else if (command.firstTokenEquals("time") || command.firstTokenEquals("date")) {
            turnLength = gameState.getHero().printDateAndTime();
        } else if (command.firstTokenEquals("system")) {
            SystemInfo.printSystemInfo();
        } else if (command.firstTokenEquals("help") || command.firstTokenEquals("?")) {
            Help.printHelp(command);
        } else if (command.firstTokenEquals("commands")) {
            Help.printCommandList(command);
        } else if (command.firstTokenEquals("save")) {
            Loader.saveGame(gameState, command);
        } else if (command.firstTokenEquals("load")) {
            GameState loadedGameState = Loader.loadGame(command);
            if (loadedGameState != null) {
                gameState = loadedGameState;
            }
        } else if (command.firstTokenEquals("quit") || command.firstTokenEquals("exit")) {
            Game.exit();
        } else if (command.firstTokenEquals("license") || command.firstTokenEquals("copyright")) {
            LicenseUtils.printLicense();
        } else if (command.firstTokenEquals("fibonacci")) {
            Math.fibonacci(command);
        } else if (command.firstTokenEquals("hint") || command.firstTokenEquals("tip")) {
            gameState.printNextHint();
        } else if (command.firstTokenEquals("poem")) {
            gameState.printNextPoem();
        } else if (command.firstTokenEquals("version")) {
            Utils.printVersion();
        } else if (command.firstTokenEquals("debug")) {
            DebugTools.parseDebugCommand(command);
        } else if (command.firstTokenEquals("config")) {
            configurationsChanged = ConfigTools.parseConfigCommand(command);
        } else {
            // The user issued a command, but it was not recognized.
            Utils.printInvalidCommandMessage(command.getFirstToken());
        }
    }

    // Exits the game, prompting the user if the current state should be saved if it is not already saved.
    public static void exit() {
        if (!gameState.isSaved()) {
            Loader.saveGame(gameState);
        }
        DLogger.info("Exited with no problems.");
        System.exit(0);
    }

}
