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

package org.dungeon.debug;

import static org.dungeon.date.DungeonTimeUnit.DAY;
import static org.dungeon.date.DungeonTimeUnit.SECOND;

import org.dungeon.achievements.Achievement;
import org.dungeon.achievements.AchievementTracker;
import org.dungeon.commands.Command;
import org.dungeon.commands.CommandResult;
import org.dungeon.commands.IssuedCommand;
import org.dungeon.date.Date;
import org.dungeon.entity.creatures.Creature;
import org.dungeon.entity.creatures.CreatureFactory;
import org.dungeon.entity.items.CreatureInventory.SimulationResult;
import org.dungeon.entity.items.Item;
import org.dungeon.entity.items.ItemFactory;
import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.GameState;
import org.dungeon.game.ID;
import org.dungeon.game.Location;
import org.dungeon.game.LocationPreset;
import org.dungeon.game.PartOfDay;
import org.dungeon.game.Point;
import org.dungeon.io.IO;
import org.dungeon.map.WorldMap;
import org.dungeon.map.WorldMapWriter;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.CounterMap;
import org.dungeon.util.Matches;
import org.dungeon.util.Messenger;
import org.dungeon.util.Table;
import org.dungeon.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A set of debugging tools.
 */
public class DebugTools {

  private static final List<Command> commands = new ArrayList<Command>();
  private static boolean uninitialized = true;

  /**
   * Parses an IssuedCommand and executes the corresponding debugging Command if there is one.
   *
   * @param issuedCommand the last command issued by the player
   * @return a CommandResult representing the result of this debug command or null
   */
  public static CommandResult parseDebugCommand(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      if (uninitialized) {
        initialize();
      }
      for (Command command : commands) {
        if (issuedCommand.firstArgumentEquals(command.getDescription().getName())) {
          return command.execute(issuedCommand);
        }
      }
      IO.writeString("Command not recognized.");
    } else {
      Messenger.printMissingArgumentsMessage();
    }
    return null;
  }

  /**
   * Creates all debugging Commands.
   * <p/>
   * This method also sets {@code uninitialized} to false.
   */
  private static void initialize() {
    commands.add(new Command("achievements") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        printNotYetUnlockedAchievements();
        return null;
      }
    });
    commands.add(new Command("exploration") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        printExplorationStatistics();
        return null;
      }
    });
    commands.add(new Command("kills") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        printKills();
        return null;
      }
    });
    commands.add(new Command("location") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        printCurrentLocationInformation();
        return null;
      }
    });
    commands.add(new Command("map") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        WorldMapWriter.writeMap(WorldMap.makeDebugWorldMap());
        return null;
      }
    });
    commands.add(new Command("list") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        listAllArguments();
        return null;
      }
    });
    commands.add(new Command("give") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        if (issuedCommand.getTokenCount() >= 3) {
          boolean gaveItem = give(issuedCommand.getArguments()[1]);
          if (gaveItem) {
            return new DebugCommandResult(0, true);
          } else {
            return null;
          }
        } else {
          Messenger.printMissingArgumentsMessage();
          return null;
        }
      }
    });
    commands.add(new Command("saved") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        printIsSaved();
        return null;
      }
    });
    commands.add(new Command("spawn") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        if (spawn(issuedCommand)) {
          return new DebugCommandResult(0, true);
        } else {
          return null;
        }
      }
    });
    commands.add(new Command("tomorrow") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int timeWaiting = evaluateWaitedSeconds((int) DAY.as(SECOND));
        IO.writeString("A day has passed.");
        return new DebugCommandResult(timeWaiting, false);
      }
    });
    commands.add(new Command("time") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        printTime();
        return null;
      }
    });
    commands.add(new Command("wait") {
      @Override
      public CommandResult execute(IssuedCommand issuedCommand) {
        int timeWaiting = DebugTools.wait(issuedCommand);
        return new DebugCommandResult(timeWaiting, false);
      }
    });
    uninitialized = false;
  }

  private static void printKills() {
    CounterMap<CauseOfDeath> map = Game.getGameState().getStatistics().getBattleStatistics().getKillsByCauseOfDeath();
    if (map.isNotEmpty()) {
      Table table = new Table("Type", "Count");
      for (CauseOfDeath causeOfDeath : map.keySet()) {
        table.insertRow(causeOfDeath.toString(), String.valueOf(map.getCounter(causeOfDeath)));
      }
      table.print();
    } else {
      IO.writeString("You haven't killed anything yet. Go kill something!");
    }
  }

  private static void printNotYetUnlockedAchievements() {
    AchievementTracker tracker = Game.getGameState().getHero().getAchievementTracker();
    List<Achievement> achievementList = new ArrayList<Achievement>();
    for (Achievement achievement : GameData.ACHIEVEMENTS.values()) {
      if (!tracker.isUnlocked(achievement)) {
        achievementList.add(achievement);
      }
    }
    if (achievementList.isEmpty()) {
      IO.writeString("All achievements have been unlocked.");
    } else {
      Collections.sort(achievementList);
      for (Achievement achievement : achievementList) {
        IO.writeString(String.format("%s : %s", achievement.getName(), achievement.getInfo()));
      }
    }
  }

  private static void printExplorationStatistics() {
    ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
    Table table = new Table("Name", "Kills", "Visited so far", "Maximum number of visits");
    for (LocationPreset preset : GameData.getLocationPresetStore().getAllPresets()) {
      String name = preset.getName().getSingular();
      String kills = String.valueOf(explorationStatistics.getKillCount(preset.getID()));
      String VisitedSoFar = String.valueOf(explorationStatistics.getVisitedLocations(preset.getID()));
      String maximumNumberOfVisits = String.valueOf(explorationStatistics.getMaximumNumberOfVisits(preset.getID()));
      table.insertRow(name, kills, VisitedSoFar, maximumNumberOfVisits);
    }
    table.print();
  }

  /**
   * Prints a lot of information about the Location the Hero is in.
   */
  private static void printCurrentLocationInformation() {
    final int WIDTH = 20;  // The width of the row's "tag".
    GameState gameState = Game.getGameState();
    Point heroPosition = gameState.getHeroPosition();
    Location location = gameState.getWorld().getLocation(heroPosition);
    StringBuilder sb = new StringBuilder();
    sb.append(Utils.padString("Point:", WIDTH)).append(heroPosition.toString()).append('\n');
    sb.append(Utils.padString("Creatures (" + location.getCreatureCount() + "):", WIDTH)).append('\n');
    for (Creature creature : location.getCreatures()) {
      sb.append(Utils.padString("  " + creature.getName(), WIDTH));
      sb.append(creature.getVisibility().toPercentage()).append('\n');
    }
    if (!location.getItemList().isEmpty()) {
      sb.append(Utils.padString("Items (" + location.getItemList().size() + "):", WIDTH)).append('\n');
      for (Item item : location.getItemList()) {
        sb.append(Utils.padString("  " + item.getQualifiedName(), WIDTH));
        sb.append(item.getVisibility().toPercentage()).append('\n');
      }
    } else {
      sb.append("No items.\n");
    }
    sb.append(Utils.padString("Luminosity:", WIDTH)).append(location.getLuminosity()).append('\n');
    sb.append(Utils.padString("Permittivity:", WIDTH)).append(location.getLightPermittivity()).append('\n');
    sb.append(Utils.padString("Blocked Entrances:", WIDTH)).append(location.getBlockedEntrances()).append('\n');
    IO.writeString(sb.toString());
  }

  /**
   * Attempts to give an Item to the Hero. Returns whether or not this operation was successful.
   *
   * @param itemID the ID of the Item, as provided by the player
   * @return true if an Item was created, false otherwise
   */
  private static boolean give(String itemID) {
    Date date = Game.getGameState().getWorld().getWorldDate();
    Item item = ItemFactory.makeItem(new ID(itemID.toUpperCase()), date);
    if (item != null) {
      IO.writeString("Item successfully created.");
      if (Game.getGameState().getHero().getInventory().simulateItemAddition(item) == SimulationResult.SUCCESSFUL) {
        Game.getGameState().getHero().addItem(item);
      } else {
        Game.getGameState().getHeroLocation().addItem(item);
        IO.writeString("Item could not be added to your inventory. It was added to the current location instead.");
      }
      return true;
    } else {
      IO.writeString("Item could not be created.");
    }
    return false;
  }

  private static void listAllArguments() {
    StringBuilder builder = new StringBuilder();
    builder.append("Valid commands:");
    for (Command command : commands) {
      builder.append("\n ").append(command.getDescription().getName());
    }
    IO.writeString(builder.toString());
  }

  /**
   * Spawns the specified creatures in the Location the Hero is in.
   * <p/>
   * Note that spawning creatures with this method does NOT change the game state to unsaved.
   *
   * @return true if a creature was spawned, false otherwise
   */
  private static boolean spawn(IssuedCommand issuedCommand) {
    if (issuedCommand.getTokenCount() >= 3) {
      for (int i = 1; i < issuedCommand.getArguments().length; i++) {
        ID givenID = new ID(issuedCommand.getArguments()[i].toUpperCase());
        Creature clone = CreatureFactory.makeCreature(givenID);
        if (clone != null) {
          Game.getGameState().getHeroLocation().addCreature(clone);
          IO.writeString("Spawned a " + clone.getName() + ".");
          return true;
        } else {
          IO.writeString(givenID + " does not match any known creature.");
        }
      }
    } else {
      Messenger.printMissingArgumentsMessage();
    }
    return false;
  }

  private static void printIsSaved() {
    if (Game.getGameState().isSaved()) {
      IO.writeString("The game is saved.");
    } else {
      IO.writeString("This game state is not saved.");
    }
  }

  /**
   * Returns how many seconds the Hero waited. This method does not modify the GameState object.
   */
  private static int wait(IssuedCommand issuedCommand) {
    if (issuedCommand.getTokenCount() >= 3) {
      int seconds = 0;
      boolean gotSeconds = false;
      String argument = issuedCommand.getArguments()[1];
      Matches<PartOfDay> matches = Utils.findBestCompleteMatches(Arrays.asList(PartOfDay.values()), argument);
      if (matches.size() == 1) {
        seconds = PartOfDay.getSecondsToNext(Game.getGameState().getWorld().getWorldDate(), matches.getMatch(0));
        gotSeconds = true;
      } else if (matches.size() > 1) {
        Messenger.printAmbiguousSelectionMessage();
      } else {
        try {
          seconds = Integer.parseInt(argument);
          gotSeconds = true;
        } catch (NumberFormatException warn) {
          Messenger.printInvalidNumberFormatOrValue();
        }
      }
      if (gotSeconds) {
        return evaluateWaitedSeconds(seconds);
      }
    } else {
      Messenger.printMissingArgumentsMessage();
    }
    return 0;
  }

  private static int evaluateWaitedSeconds(int seconds) {
    if (seconds > 0) {
      IO.writeString("Waited for " + seconds + " seconds.");
      return seconds;
    } else {
      IO.writeString("The amount of seconds should be positive!");
      return 0;
    }
  }

  private static void printTime() {
    IO.writeString(Game.getGameState().getWorld().getWorldDate().toTimeString());
  }

}
