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

import org.dungeon.commands.Command;
import org.dungeon.commands.CommandDescription;
import org.dungeon.commands.CommandHelp;
import org.dungeon.commands.CommandResult;
import org.dungeon.commands.IssuedCommand;
import org.dungeon.commands.SimpleCommandResult;
import org.dungeon.debug.DebugTools;
import org.dungeon.gui.GameWindow;
import org.dungeon.io.DLogger;
import org.dungeon.io.IO;
import org.dungeon.io.Loader;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.Math;
import org.dungeon.util.Messenger;
import org.dungeon.util.SystemInfo;
import org.dungeon.util.Utils;
import org.dungeon.wiki.Wiki;

import java.util.ArrayList;
import java.util.List;

public class Game {

  private static GameWindow gameWindow;
  private static GameState gameState;
  private static List<Command> commandList;
  private static List<CommandDescription> descriptions = new ArrayList<CommandDescription>();
  private static CommandResult lastCommandResult;

  public static void main(String[] args) {
    GameData.loadGameData();
    initializeCommands();
    gameWindow = new GameWindow();
    setGameState(loadAGameStateOrCreateANewOne());
  }

  /**
   * Loads a saved GameState or creates a new one.
   * If a new GameState is created and the saves folder is empty, the tutorial is suggested.
   */
  private static GameState loadAGameStateOrCreateANewOne() {
    GameState gameState = Loader.loadGame();
    if (gameState == null) {
      gameState = Loader.newGame();
      // Note that loadedGameState may be null even if a save exists (if the player declined to load it).
      // So check for any save in the folder.
      if (!Loader.checkForAnySave()) { // Suggest the tutorial only if no saved game exists.
        suggestTutorial();
      }
    }
    return gameState;
  }

  /**
   * Creates the anonymous subclasses of Command and add them to the List of Commands.
   * Also adds all the CommandDescriptions to the proper List.
   */
  private static void initializeCommands() {
    // When referring to the playable character, "character" should be used instead of "you" or "player".
    // Also, note that the gender is unspecified.
    commandList = new ArrayList<Command>();
    // Commands, alphabetically sorted using the name as key.
    commandList.add(new Command("achievements", "Displays the achievements the character already unlocked.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        gameState.printUnlockedAchievements();
        return null;
      }
    });
    commandList.add(new Command("age", "Displays the character's age.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        gameState.getHero().printAge();
        return null;
      }
    });
    commandList.add(new Command("commands", "Displays a list of valid commands.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        CommandHelp.printCommandList(issuedCommand);
        return null;
      }
    });
    commandList.add(new Command("debug", "Invokes a debugging command.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        return DebugTools.parseDebugCommand(issuedCommand);
      }
    });
    commandList.add(new Command("destroy", "Destroys an item on the ground.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int duration = gameState.getHero().destroyItem(issuedCommand);
        return new SimpleCommandResult(duration);
      }
    });
    commandList.add(new Command("drop", "Drops the specified item.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int duration = gameState.getHero().dropItem(issuedCommand);
        return new SimpleCommandResult(duration);
      }
    });
    commandList.add(new Command("eat", "Eats an item.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int duration = gameState.getHero().eatItem(issuedCommand);
        return new SimpleCommandResult(duration);
      }
    });
    commandList.add(new Command("equip", "Equips the specified item.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int duration = gameState.getHero().parseEquip(issuedCommand);
        return new SimpleCommandResult(duration);
      }
    });
    commandList.add(new Command("exit", "Exits the game.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        Game.exit();
        return null;
      }
    });
    commandList.add(new Command("fibonacci", "Displays the specified term of the Fibonacci's sequence.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        Math.parseFibonacci(issuedCommand);
        return null;
      }
    });
    commandList.add(new Command("go", "Makes the character move in the specified direction.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int duration = Engine.parseHeroWalk(issuedCommand);
        return new SimpleCommandResult(duration);
      }
    });
    commandList.add(new Command("help", "Displays the help text for a specified command.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        CommandHelp.printHelp(issuedCommand);
        return null;
      }
    });
    commandList.add(new Command("hint", "Displays a random hint of the game.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        gameState.printNextHint();
        return null;
      }
    });
    commandList.add(new Command("items", "Lists the items in the character's inventory.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        gameState.getHero().printInventory();
        return null;
      }
    });
    commandList.add(new Command("kill", "Attacks the target chosen by the player.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int duration = gameState.getHero().attackTarget(issuedCommand);
        return new SimpleCommandResult(duration);
      }
    });
    commandList.add(new Command("license", "Displays the game's license.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        Utils.printLicense();
        return null;
      }
    });
    commandList.add(new Command("load", "Loads a saved game.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        GameState loadedGameState = Loader.loadGame(issuedCommand);
        if (loadedGameState != null) {
          setGameState(loadedGameState);
        }
        return null;
      }
    });
    commandList.add(new Command("look", "Describes what the character can see.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        gameState.getHero().look(null);
        return null;
      }
    });
    commandList.add(new Command("map", "Shows a map of your surroundings.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        World world = gameState.getWorld();
        ExplorationStatistics explorationStatistics = gameState.getStatistics().getExplorationStatistics();
        Point heroPosition = gameState.getHeroPosition();
        WorldMap map = new WorldMap(world, explorationStatistics, heroPosition);
        IO.writeString(map.toString());
        return null;
      }
    });
    commandList.add(new Command("milk", "Attempts to milk a creature.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int duration = gameState.getHero().parseMilk(issuedCommand);
        return new SimpleCommandResult(duration);
      }
    });
    commandList.add(new Command("new", "Starts a new game.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        setGameState(Loader.newGame());
        return null;
      }
    });
    commandList.add(new Command("pick", "Attempts to pick up an item from the current location.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int duration = gameState.getHero().pickItem(issuedCommand);
        return new SimpleCommandResult(duration);
      }
    });
    commandList.add(new Command("poem", "Prints a poem from the poem library.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        gameState.printPoem(issuedCommand);
        return null;
      }
    });
    commandList.add(new Command("read", "Reads the specified item.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int duration = gameState.getHero().readItem(issuedCommand);
        return new SimpleCommandResult(duration);
      }
    });
    commandList.add(new Command("repair", "Attempts to cast Repair on the equipped item.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int duration = gameState.getHero().castRepairOnEquippedItem();
        return new SimpleCommandResult(duration);
      }
    });
    commandList.add(new Command("rest", "Rests until healing about three fifths of the character's health.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int duration = gameState.getHero().rest();
        return new SimpleCommandResult(duration);
      }
    });
    commandList.add(new Command("rotation", "Either displays the current skill rotation or sets up a new one.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        gameState.getHero().editRotation(issuedCommand);
        return null;
      }
    });
    commandList.add(new Command("save", "Saves the game.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        Loader.saveGame(gameState, issuedCommand);
        return null;
      }
    });
    commandList.add(new Command("saves", "Displays a table with all the save files.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        Loader.printFilesInSavesFolder();
        return null;
      }
    });
    commandList.add(new Command("skills", "Displays a list with all skills known by the character.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        gameState.getHero().printSkills();
        return null;
      }
    });
    commandList.add(new Command("sleep", "Sleeps until the sun rises. The character may dream during it.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int duration = gameState.getHero().sleep();
        return new SimpleCommandResult(duration);
      }
    });
    commandList.add(new Command("statistics", "Displays all available game statistics.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        gameState.getStatistics().printStatistics();
        return null;
      }
    });
    commandList.add(new Command("status", "Displays the character's status.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        gameState.getHero().printAllStatus();
        return null;
      }
    });
    commandList.add(new Command("system", "Displays information about the underlying system.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        SystemInfo.printSystemInfo();
        return null;
      }
    });
    commandList.add(new Command("time", "Displays what the character knows about the current time and date.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int duration = gameState.getHero().printDateAndTime();
        return new SimpleCommandResult(duration);
      }
    });
    commandList.add(new Command("tutorial", "Displays the tutorial.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        IO.writeString(GameData.getTutorial());
        return null;
      }
    });
    commandList.add(new Command("unequip", "Unequips the currently equipped item.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int duration = gameState.getHero().unequipWeapon();
        return new SimpleCommandResult(duration);
      }
    });
    commandList.add(new Command("wiki", "Searches the wiki for an article.") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        Wiki.search(issuedCommand);
        return null;
      }
    });
    // Put the CommandDescriptions in their List.
    for (Command command : commandList) {
      descriptions.add(command.getDescription());
    }
  }

  private static void suggestTutorial() {
    IO.writeNewLine();
    IO.writeString("You may want to issue 'tutorial' to learn the basics.");
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
   * <p/>
   * There is no need to call Engine.refresh() after invoking this method.
   *
   * @param state the new GameState (should not be {@code null})
   */
  private static void setGameState(GameState state) {
    gameState = state;
    Engine.refresh();
    IO.writeNewLine(); // Improves readability.
    gameState.getHero().look(null);
  }

  public static List<CommandDescription> getCommandDescriptions() {
    return descriptions;
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
      setGameState(loadAGameStateOrCreateANewOne());
    } else {
      // Advance the campaign's world date.
      if (lastCommandResult != null) {
        if (lastCommandResult.getDuration() > 0) {
          gameState.getWorld().rollDate(lastCommandResult.getDuration());
        }
        // If the last turn changed the GameState, set the game as not saved.
        if (lastCommandResult.evaluateIfGameStateChanged()) {
          gameState.setSaved(false);
        }
      }
      // Refresh the campaign state.
      Engine.refresh();
    }
  }

  /**
   * Processes the player's input. Adds the IssuedCommand to the CommandHistory and to the CommandStatistics. Finally,
   * this method finds and executes the corresponding Command object or prints a message if there is not such Command.
   *
   * @param issuedCommand the last IssuedCommand.
   */
  private static void processInput(IssuedCommand issuedCommand) {
    gameState.getCommandHistory().addCommand(issuedCommand);
    gameState.getStatistics().addCommand(issuedCommand);
    for (Command command : commandList) {
      if (command.getDescription().getName().equalsIgnoreCase(issuedCommand.getFirstToken())) {
        lastCommandResult = command.execute(issuedCommand);
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
