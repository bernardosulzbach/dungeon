/*
 * Copyright (C) 2015 Bernardo Sulzbach
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

package org.dungeon.commands;

import org.dungeon.achievements.AchievementTrackerWriter;
import org.dungeon.debug.DebugTools;
import org.dungeon.game.Engine;
import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.GameState;
import org.dungeon.io.DungeonLogger;
import org.dungeon.io.Loader;
import org.dungeon.io.PoemWriter;
import org.dungeon.io.Writer;
import org.dungeon.map.WorldMap;
import org.dungeon.map.WorldMapWriter;
import org.dungeon.util.DungeonMath;
import org.dungeon.util.SystemInfo;
import org.dungeon.util.Utils;
import org.dungeon.util.library.Libraries;
import org.dungeon.wiki.WikiSearcher;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A collection of Commands.
 */
public final class CommandCollection {

  private static final CommandCollection defaultCommandCollection = new CommandCollection();

  static {
    // Respect the alphabetical ordering of the Command names.
    addCommandToDefault(new Command("achievements", "Displays the achievements the character already unlocked.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        AchievementTrackerWriter.parseCommand(issuedCommand);
      }
    });
    addCommandToDefault(new Command("age", "Displays the character's age.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().printAge();
      }
    });
    addCommandToDefault(new Command("cast", "Casts a spell.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().getSpellcaster().parseCast(issuedCommand);
      }
    });
    addCommandToDefault(new Command("commands", "Displays a list of valid commands.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        CommandHelp.printCommandList(issuedCommand);
      }
    });
    addCommandToDefault(new Command("debug", "Invokes a debugging command.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        DebugTools.parseDebugCommand(issuedCommand);
      }
    });
    addCommandToDefault(new Command("destroy", "Destroys an item on the ground.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().destroyItem(issuedCommand);
      }
    });
    addCommandToDefault(new Command("drop", "Drops the specified item.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().dropItem(issuedCommand);
      }
    });
    addCommandToDefault(new Command("eat", "Eats an item.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().eatItem(issuedCommand);
      }
    });
    addCommandToDefault(new Command("equip", "Equips the specified item.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().parseEquip(issuedCommand);
      }
    });
    addCommandToDefault(new Command("exit", "Exits the game.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.exit();
      }
    });
    addCommandToDefault(new Command("fibonacci", "Displays the specified term of the Fibonacci's sequence.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        DungeonMath.parseFibonacci(issuedCommand);
      }
    });
    addCommandToDefault(new Command("go", "Makes the character move in the specified direction.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Engine.parseHeroWalk(issuedCommand);
      }
    });
    addCommandToDefault(new Command("help", "Displays the help text for a specified command.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        CommandHelp.printHelp(issuedCommand);
      }
    });
    addCommandToDefault(new Command("hint", "Displays a random hint of the game.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Writer.writeString(Libraries.getHintLibrary().next());
      }
    });
    addCommandToDefault(new Command("items", "Lists the items in the character's inventory.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().printInventory();
      }
    });
    addCommandToDefault(new Command("kill", "Attacks the target chosen by the player.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().attackTarget(issuedCommand);
      }
    });
    addCommandToDefault(new Command("license", "Displays the game's license.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Utils.printLicense();
      }
    });
    addCommandToDefault(new Command("load", "Loads a saved game.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        GameState loadedGameState = Loader.loadGame(issuedCommand);
        if (loadedGameState != null) {
          Game.setGameState(loadedGameState);
        }
      }
    });
    addCommandToDefault(new Command("look", "Describes what the character can see.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().look(null);
      }
    });
    addCommandToDefault(new Command("map", "Shows a map of your surroundings.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        WorldMapWriter.writeMap(WorldMap.makeWorldMap());
      }
    });
    addCommandToDefault(new Command("milk", "Attempts to milk a creature.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().parseMilk(issuedCommand);
      }
    });
    addCommandToDefault(new Command("new", "Starts a new game.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.setGameState(null);
        Game.setGameState(Loader.newGame());
      }
    });
    addCommandToDefault(new Command("pick", "Attempts to pick up an item from the current location.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().pickItem(issuedCommand);
      }
    });
    addCommandToDefault(new Command("poem", "Prints a poem from the poem library.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        PoemWriter.parsePoemCommand(issuedCommand);
      }
    });
    addCommandToDefault(new Command("read", "Reads the specified item.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().readItem(issuedCommand);
      }
    });
    addCommandToDefault(new Command("rest", "Rests until healing about three fifths of the character's health.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().rest();
      }
    });
    addCommandToDefault(new Command("save", "Saves the game.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Loader.saveGame(Game.getGameState(), issuedCommand);
      }
    });
    addCommandToDefault(new Command("saves", "Displays a table with all the save files.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Loader.printFilesInSavesFolder();
      }
    });
    addCommandToDefault(new Command("sleep", "Sleeps until the sun rises. The character may dream during it.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().sleep();
      }
    });
    addCommandToDefault(new Command("spells", "Lists all the spells known by the character.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().writeSpellList();
      }
    });
    addCommandToDefault(new Command("statistics", "Displays all available game statistics.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getStatistics().printStatistics();
      }
    });
    addCommandToDefault(new Command("status", "Displays the character's status.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().printAllStatus();
      }
    });
    addCommandToDefault(new Command("system", "Displays information about the underlying system.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        SystemInfo.printSystemInfo();
      }
    });
    addCommandToDefault(new Command("time", "Displays what the character knows about the current time and date.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().readTime();
      }
    });
    addCommandToDefault(new Command("tutorial", "Displays the tutorial.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Writer.writeString(GameData.getTutorial());
      }
    });
    addCommandToDefault(new Command("unequip", "Unequips the currently equipped item.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Game.getGameState().getHero().unequipWeapon();
      }
    });
    addCommandToDefault(new Command("wiki", "Searches the wiki for an article.") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        WikiSearcher.search(issuedCommand);
      }
    });
  }

  private final List<Command> commands = new ArrayList<Command>();
  private final List<CommandDescription> commandDescriptions = new ArrayList<CommandDescription>();

  private CommandCollection() {
  }

  /**
   * Retrieves the default CommandCollection (the CommandCollection that contains all the normal Commands).
   *
   * @return the default CommandCollection
   */
  public static CommandCollection getDefaultCommandCollection() {
    return defaultCommandCollection;
  }

  /**
   * Convenience method to add a Command to the default CommandCollection.
   *
   * @param command a Command, not null
   */
  private static void addCommandToDefault(@NotNull Command command) {
    validateCommandAddition(command);
    defaultCommandCollection.addCommand(command);
  }

  private static void validateCommandAddition(@NotNull Command command) {
    if (!defaultCommandCollection.commands.isEmpty()) {
      Command lastCommand = defaultCommandCollection.commands.get(defaultCommandCollection.commands.size() - 1);
      String lastCommandName = lastCommand.getDescription().getName();
      String commandName = command.getDescription().getName();
      if (lastCommandName.compareTo(commandName) >= 0) {
        throw new IllegalArgumentException("commands are not alphabetically ordered.");
      }
    }
  }

  /**
   * Returns a Command object whose name is case insensitively equals to the first token of the provided IssuedCommand
   * or null if no such Command exists.
   *
   * @param issuedCommand an IssuedCommand object, not null
   * @return a Command object whose name is the equal to the first token of issuedCommand or null
   */
  public Command getCommand(IssuedCommand issuedCommand) {
    if (issuedCommand == null) {
      DungeonLogger.warning("Passed null IssuedCommand to CommandCollection.getCommand()!");
    } else {
      for (Command command : commands) {
        if (command.getDescription().getName().equalsIgnoreCase(issuedCommand.getFirstToken())) {
          return command;
        }
      }
    }
    return null;
  }

  /**
   * Adds a Command to this CommandCollection.
   *
   * @param command a Command object, not null
   */
  private void addCommand(Command command) {
    if (command == null) {
      DungeonLogger.warning("Passed null to CommandCollection.addCommand()!");
    } else if (commands.contains(command)) {
      DungeonLogger.warning("Attempted to add the same Command to a CommandCollection twice!");
    } else {
      commands.add(command);
      commandDescriptions.add(command.getDescription());
    }
  }

  /**
   * Returns an unmodifiable view of the List of CommandDescriptions.
   *
   * @return an unmodifiable List of CommandDescriptions
   */
  List<CommandDescription> getCommandDescriptions() {
    return Collections.unmodifiableList(commandDescriptions);
  }

  @Override
  public String toString() {
    return String.format("CommandCollection of size %d.", commands.size());
  }

}
