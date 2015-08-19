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
import org.dungeon.achievements.AchievementStore;
import org.dungeon.achievements.AchievementTracker;
import org.dungeon.commands.Command;
import org.dungeon.commands.IssuedCommand;
import org.dungeon.date.Date;
import org.dungeon.entity.creatures.Creature;
import org.dungeon.entity.creatures.CreatureFactory;
import org.dungeon.entity.items.CreatureInventory.SimulationResult;
import org.dungeon.entity.items.Item;
import org.dungeon.entity.items.ItemFactory;
import org.dungeon.game.Engine;
import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.GameState;
import org.dungeon.game.Id;
import org.dungeon.game.Location;
import org.dungeon.game.LocationPreset;
import org.dungeon.game.PartOfDay;
import org.dungeon.game.Point;
import org.dungeon.io.Writer;
import org.dungeon.map.WorldMap;
import org.dungeon.map.WorldMapWriter;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.CounterMap;
import org.dungeon.util.Matches;
import org.dungeon.util.Messenger;
import org.dungeon.util.Table;
import org.dungeon.util.Utils;

import org.jetbrains.annotations.NotNull;

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
   */
  public static void parseDebugCommand(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      if (uninitialized) {
        initialize();
      }
      for (Command command : commands) {
        if (issuedCommand.firstArgumentEquals(command.getDescription().getName())) {
          command.execute(issuedCommand);
          return;
        }
      }
      Writer.writeString("Command not recognized.");
    } else {
      Messenger.printMissingArgumentsMessage();
    }
  }

  /**
   * Creates all debugging Commands.
   *
   * <p>This method also sets {@code uninitialized} to false.
   */
  private static void initialize() {
    commands.add(new Command("achievements") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        printNotYetUnlockedAchievements();
      }
    });
    commands.add(new Command("exploration") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        printExplorationStatistics();
      }
    });
    commands.add(new Command("kills") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        printKills();
      }
    });
    commands.add(new Command("location") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        printCurrentLocationInformation();
      }
    });
    commands.add(new Command("map") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        WorldMapWriter.writeMap(WorldMap.makeDebugWorldMap());
      }
    });
    commands.add(new Command("list") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        listAllArguments();
      }
    });
    commands.add(new Command("give") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        if (issuedCommand.getTokenCount() >= 3) {
          give(issuedCommand.getArguments()[1]);
        } else {
          Messenger.printMissingArgumentsMessage();
        }
      }
    });
    commands.add(new Command("saved") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        printIsSaved();
      }
    });
    commands.add(new Command("spawn") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        spawn(issuedCommand);
      }
    });
    commands.add(new Command("tomorrow") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Engine.rollDateAndRefresh((int) DAY.as(SECOND));
        Writer.writeString("A day has passed.");
      }
    });
    commands.add(new Command("time") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        Writer.writeString(Game.getGameState().getWorld().getWorldDate().toString());
      }
    });
    commands.add(new Command("wait") {
      @Override
      public void execute(@NotNull IssuedCommand issuedCommand) {
        DebugTools.wait(issuedCommand);
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
      Writer.write(table);
    } else {
      Writer.writeString("You haven't killed anything yet. Go kill something!");
    }
  }

  private static void printNotYetUnlockedAchievements() {
    AchievementTracker tracker = Game.getGameState().getHero().getAchievementTracker();
    List<Achievement> achievementList = new ArrayList<Achievement>();
    for (Achievement achievement : AchievementStore.getAchievements()) {
      if (!tracker.isUnlocked(achievement)) {
        achievementList.add(achievement);
      }
    }
    if (achievementList.isEmpty()) {
      Writer.writeString("All achievements have been unlocked.");
    } else {
      Collections.sort(achievementList);
      for (Achievement achievement : achievementList) {
        Writer.writeString(String.format("%s : %s", achievement.getName(), achievement.getInfo()));
      }
    }
  }

  private static void printExplorationStatistics() {
    ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
    Table table = new Table("Name", "Kills", "Visited so far", "Maximum number of visits");
    for (LocationPreset preset : GameData.getLocationPresetStore().getAllPresets()) {
      String name = preset.getName().getSingular();
      String kills = String.valueOf(explorationStatistics.getKillCount(preset.getId()));
      String VisitedSoFar = String.valueOf(explorationStatistics.getVisitedLocations(preset.getId()));
      String maximumNumberOfVisits = String.valueOf(explorationStatistics.getMaximumNumberOfVisits(preset.getId()));
      table.insertRow(name, kills, VisitedSoFar, maximumNumberOfVisits);
    }
    Writer.write(table);
  }

  /**
   * Prints a lot of information about the Location the Hero is in.
   */
  private static void printCurrentLocationInformation() {
    final int WIDTH = 40;  // The width of the row's "tag".
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
    sb.append(Utils.padString("Luminosity:", WIDTH)).append(location.getLuminosity().toPercentage()).append('\n');
    sb.append(Utils.padString("Permittivity:", WIDTH)).append(location.getLightPermittivity()).append('\n');
    sb.append(Utils.padString("Blocked Entrances:", WIDTH)).append(location.getBlockedEntrances()).append('\n');
    Writer.writeString(sb.toString());
  }

  /**
   * Attempts to give an Item to the Hero.
   *
   * @param itemId the Id of the Item, as provided by the player
   */
  private static void give(String itemId) {
    Date date = Game.getGameState().getWorld().getWorldDate();
    Item item = ItemFactory.makeItem(new Id(itemId.toUpperCase()), date);
    if (item != null) {
      Writer.writeString("Item successfully created.");
      if (Game.getGameState().getHero().getInventory().simulateItemAddition(item) == SimulationResult.SUCCESSFUL) {
        Game.getGameState().getHero().addItem(item);
      } else {
        Game.getGameState().getHeroLocation().addItem(item);
        Writer.writeString("Item could not be added to your inventory. It was added to the current location instead.");
      }
      Engine.refresh(); // Set the game state to unsaved after adding an item to the world.
    } else {
      Writer.writeString("Item could not be created.");
    }
  }

  private static void listAllArguments() {
    StringBuilder builder = new StringBuilder();
    builder.append("Valid commands:");
    for (Command command : commands) {
      builder.append("\n ").append(command.getDescription().getName());
    }
    Writer.writeString(builder.toString());
  }

  /**
   * Spawns the specified creatures in the Location the Hero is in.
   */
  private static void spawn(IssuedCommand issuedCommand) {
    if (issuedCommand.getTokenCount() >= 3) {
      for (int i = 1; i < issuedCommand.getArguments().length; i++) {
        Id givenId = new Id(issuedCommand.getArguments()[i].toUpperCase());
        Creature clone = CreatureFactory.makeCreature(givenId);
        if (clone != null) {
          Game.getGameState().getHeroLocation().addCreature(clone);
          Writer.writeString("Spawned a " + clone.getName() + ".");
          Engine.refresh(); // Set the game state to unsaved after adding a creature to the world.
        } else {
          Writer.writeString(givenId + " does not match any known creature.");
        }
      }
    } else {
      Messenger.printMissingArgumentsMessage();
    }
  }

  private static void printIsSaved() {
    if (Game.getGameState().isSaved()) {
      Writer.writeString("The game is saved.");
    } else {
      Writer.writeString("This game state is not saved.");
    }
  }

  private static void wait(IssuedCommand issuedCommand) {
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
        if (seconds > 0) {
          Engine.rollDateAndRefresh(seconds);
          Writer.writeString("Waited for " + seconds + " seconds.");
        } else {
          Writer.writeString("The amount of seconds should be positive!");
        }
      }
    } else {
      Messenger.printMissingArgumentsMessage();
    }
  }

}
