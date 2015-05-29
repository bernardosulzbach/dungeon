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

package org.dungeon.game;

import org.dungeon.debug.DebugTools;
import org.dungeon.gui.GameWindow;
import org.dungeon.io.DLogger;
import org.dungeon.io.IO;
import org.dungeon.io.Loader;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.CommandHelp;
import org.dungeon.util.Math;
import org.dungeon.util.Messenger;
import org.dungeon.util.SystemInfo;
import org.dungeon.util.Utils;
import org.dungeon.wiki.Wiki;

import java.util.ArrayList;
import java.util.List;

public class Game {

  private static final TurnResult turnResult = new TurnResult();
  private static GameWindow gameWindow;
  private static GameState gameState;
  private static List<Command> commandList;

  public static void main(String[] args) {
    GameData.loadGameData();
    initializeCommands();
    gameWindow = new GameWindow();
    GameState loadedGameState = Loader.loadGame();
    if (loadedGameState == null) {
      setGameState(Loader.newGame());
      // Note that loadedGameState may be null even if a save exists (if the player declined to load it).
      // So check for any save in the folder.
      if (!Loader.checkForAnySave()) { // Suggest the tutorial only if no saved game exists.
        suggestTutorial();
      }
    } else {
      setGameState(loadedGameState);
    }
    Engine.refresh();
  }

  /**
   * Creates the anonymous subclasses of Command and add them to the List of Commands.
   */
  private static void initializeCommands() {
    // When referring to the playable character, "character" should be used instead of "you" or "player".
    // Also, note that the gender is unspecified.
    commandList = new ArrayList<Command>();
    // Commands, alphabetically sorted using the name as key.
    commandList.add(new Command("achievements", "Displays the achievements the character already unlocked.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        gameState.printUnlockedAchievements();
      }
    });
    commandList.add(new Command("age", "Displays the character's age.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        gameState.getHero().printAge();
      }
    });
    commandList.add(new Command("commands", "Displays a list of valid commands.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        CommandHelp.printCommandList(issuedCommand);
      }
    });
    commandList.add(new Command("debug", "Invokes a debugging command.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        DebugTools.parseDebugCommand(issuedCommand);
      }
    });
    commandList.add(new Command("destroy", "Destroys an item on the ground.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        turnResult.turnLength = gameState.getHero().destroyItem(issuedCommand);
      }
    });
    commandList.add(new Command("drop", "Drops the specified item.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        turnResult.turnLength = gameState.getHero().dropItem(issuedCommand);
      }
    });
    commandList.add(new Command("eat", "Eats an item.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        turnResult.turnLength = gameState.getHero().eatItem(issuedCommand);
      }
    });
    commandList.add(new Command("equip", "Equips the specified item.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        turnResult.turnLength = gameState.getHero().parseEquip(issuedCommand);
      }
    });
    commandList.add(new Command("exit", "Exits the game.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        Game.exit();
      }
    });
    commandList.add(new Command("fibonacci", "Displays the specified term of the Fibonacci's sequence.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        Math.parseFibonacci(issuedCommand);
      }
    });
    commandList.add(new Command("go", "Makes the character move in the specified direction.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        turnResult.turnLength = Engine.parseHeroWalk(issuedCommand);
      }
    });
    commandList.add(new Command("help", "Displays the help text for a specified command.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        CommandHelp.printHelp(issuedCommand);
      }
    });
    commandList.add(new Command("hint", "Displays a random hint of the game.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        gameState.printNextHint();
      }
    });
    commandList.add(new Command("items", "Lists the items in the character's inventory.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        gameState.getHero().printInventory();
      }
    });
    commandList.add(new Command("kill", "Attacks the target chosen by the player.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        turnResult.turnLength = gameState.getHero().attackTarget(issuedCommand);
      }
    });
    commandList.add(new Command("license", "Displays the game's license.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        Utils.printLicense();
      }
    });
    commandList.add(new Command("load", "Loads a saved game.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        GameState loadedGameState = Loader.loadGame(issuedCommand);
        if (loadedGameState != null) {
          setGameState(loadedGameState);
        }
      }
    });
    commandList.add(new Command("look", "Describes what the character can see.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        gameState.getHero().look(null);
      }
    });
    commandList.add(new Command("map", "Shows a map of your surroundings.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        World world = gameState.getWorld();
        ExplorationStatistics explorationStatistics = gameState.getStatistics().getExplorationStatistics();
        Point heroPosition = gameState.getHeroPosition();
        WorldMap map = new WorldMap(world, explorationStatistics, heroPosition);
        IO.writeString(map.toString());
      }
    });
    commandList.add(new Command("new", "Starts a new game.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        setGameState(Loader.newGame());
      }
    });
    commandList.add(new Command("pick", "Attempts to pick up an item from the current location.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        turnResult.turnLength = gameState.getHero().pickItem(issuedCommand);
      }
    });
    commandList.add(new Command("poem", "Prints a poem from the poem library.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        gameState.printPoem(issuedCommand);
      }
    });
    commandList.add(new Command("read", "Reads the specified item.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        turnResult.turnLength = gameState.getHero().readItem(issuedCommand);
      }
    });
    commandList.add(new Command("repair", "Attempts to cast Repair on the equipped item.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        turnResult.turnLength = gameState.getHero().castRepairOnEquippedItem();
      }
    });
    commandList.add(new Command("rest", "Rests until healing about three fifths of the character's health.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        turnResult.turnLength = gameState.getHero().rest();
      }
    });
    commandList.add(new Command("rotation", "Either displays the current skill rotation or sets up a new one.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        gameState.getHero().editRotation(issuedCommand);
      }
    });
    commandList.add(new Command("save", "Saves the game.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        Loader.saveGame(gameState, issuedCommand);
      }
    });
    commandList.add(new Command("saves", "Displays a table with all the save files.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        Loader.printFilesInSavesFolder();
      }
    });
    commandList.add(new Command("skills", "Displays a list with all skills known by the character.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        gameState.getHero().printSkills();
      }
    });
    commandList.add(new Command("sleep", "Sleeps until the sun rises. The character may dream during it.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        turnResult.turnLength = gameState.getHero().sleep();
      }
    });
    commandList.add(new Command("statistics", "Displays all available game statistics.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        gameState.getStatistics().printStatistics();
      }
    });
    commandList.add(new Command("status", "Displays the character's status.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        gameState.getHero().printAllStatus();
      }
    });
    commandList.add(new Command("system", "Displays information about the underlying system.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        SystemInfo.printSystemInfo();
      }
    });
    commandList.add(new Command("time", "Displays what the character knows about the current time and date.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        turnResult.turnLength = gameState.getHero().printDateAndTime();
      }
    });
    commandList.add(new Command("tutorial", "Displays the tutorial.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        IO.writeString(GameData.getTutorial());
      }
    });
    commandList.add(new Command("unequip", "Unequips the currently equipped item.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        turnResult.turnLength = gameState.getHero().unequipWeapon();
      }
    });
    commandList.add(new Command("wiki", "Searches the wiki for an article.") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        Wiki.search(issuedCommand);
      }
    });
  }

  private static void suggestTutorial() {
    IO.writeNewLine();
    IO.writeString("You may want to issue 'tutorial' to learn the basics.");
  }

  // This method enables other parts of the code to issue commands, is this bad design?
  // CommandData could be a component of Command that contained name and info but that did not provide execute().
  public static List<Command> getCommandList() {
    return commandList;
  }

  public static GameWindow getGameWindow() {
    return gameWindow;
  }

  public static GameState getGameState() {
    return gameState;
  }

  /**
   * Sets a new GameState to the static field.
   * This setter also invokes the {@code Hero.look()} method on the Hero of the specified GameState.
   *
   * @param state the new GameState (should not be {@code null})
   */
  private static void setGameState(GameState state) {
    gameState = state;
    Engine.refresh();
    IO.writeNewLine(); // Improves readability.
    gameState.getHero().look(null);
  }

  /**
   * Renders a turn based on the last IssuedCommand.
   *
   * @param issuedCommand the last IssuedCommand.
   */
  public static void renderTurn(IssuedCommand issuedCommand) {
    // Clears the text pane.
    getGameWindow().clearTextPane();
    processInput(issuedCommand);
    if (gameState.getHero().isDead()) {
      IO.writeString("You died.");
      // After the player's death, just prompt to load the default save file.
      setGameState(Loader.loadGame());
    } else {
      // Advance the campaign's world date.
      gameState.getWorld().rollDate(turnResult.turnLength);
      // Refresh the campaign state.
      Engine.refresh();
      // If the last turn changed the GameState, set the game as not saved.
      if (turnResult.gameStateChanged()) {
        gameState.setSaved(false);
      }
    }
    // Clear the results of the last turn.
    turnResult.clear();
  }

  /**
   * Processes the player input.
   *
   * @param issuedCommand the last IssuedCommand.
   */
  private static void processInput(IssuedCommand issuedCommand) {
    gameState.getCommandHistory().addCommand(issuedCommand);
    gameState.getStatistics().addCommand(issuedCommand);
    for (Command command : commandList) {
      if (command.name.equalsIgnoreCase(issuedCommand.getFirstToken())) {
        command.execute(issuedCommand);
        return;
      }
    }
    Messenger.printInvalidCommandMessage(issuedCommand.getFirstToken());
  }

  /**
   * Exits the game, prompting the user if the current state should be saved if it is not already saved.
   */
  public static void exit() {
    if (gameState != null) {
      if (!gameState.isSaved()) {
        Loader.saveGame(gameState);
      }
      DLogger.info("Exited with no problems.");
    }
    System.exit(0);
  }

}
