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

import org.dungeon.achievements.Achievement;
import org.dungeon.achievements.AchievementTracker;
import org.dungeon.creatures.Creature;
import org.dungeon.creatures.CreatureBlueprint;
import org.dungeon.game.Command;
import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.GameState;
import org.dungeon.game.ID;
import org.dungeon.game.IssuedCommand;
import org.dungeon.game.Location;
import org.dungeon.game.LocationPreset;
import org.dungeon.game.Point;
import org.dungeon.io.IO;
import org.dungeon.items.Item;
import org.dungeon.items.ItemBlueprint;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.Messenger;
import org.dungeon.util.Table;
import org.dungeon.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * A set of debugging tools.
 * <p/>
 * Created by Bernardo Sulzbach on 03/11/14.
 */
public class DebugTools {

  private static List<Command> commands = new ArrayList<Command>();
  private static boolean uninitialized = true;

  /**
   * Parses an IssuedCommand and executes the corresponding debugging Command if there is one.
   *
   * @param issuedCommand the last command issued by the player.
   */
  public static void parseDebugCommand(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      if (uninitialized) {
        initialize();
      }
      for (Command command : commands) {
        if (issuedCommand.firstArgumentEquals(command.name)) {
          command.execute(issuedCommand);
          return;
        }
      }
      IO.writeString("Command not recognized.");
    } else {
      Messenger.printMissingArgumentsMessage();
    }

  }

  /**
   * Creates all debugging Commands.
   * <p/>
   * This method also sets {@code uninitialized} to false.
   */
  private static void initialize() {
    commands.add(new Command("achievements") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        printNotYetUnlockedAchievements();
      }
    });
    commands.add(new Command("exploration") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        printExplorationStatistics();
      }
    });
    commands.add(new Command("location") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        printCurrentLocationInformation();
      }
    });
    commands.add(new Command("list") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        listAllArguments();
      }
    });
    commands.add(new Command("give") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        if (issuedCommand.getTokenCount() >= 3) {
          give(issuedCommand.getArguments()[1]);
        } else {
          Messenger.printMissingArgumentsMessage();
        }
      }
    });
    commands.add(new Command("saved") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        printIsSaved();
      }
    });
    commands.add(new Command("spawn") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        spawn(issuedCommand);
      }
    });
    commands.add(new Command("tomorrow") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        Game.getGameState().getWorld().rollDate(24 * 60 * 60);
        IO.writeString("A day has passed.");
      }
    });
    commands.add(new Command("time") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        printTime();
      }
    });
    commands.add(new Command("wait") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        DebugTools.wait(issuedCommand);
      }
    });
    uninitialized = false;
  }

  private static void printNotYetUnlockedAchievements() {
    AchievementTracker tracker = Game.getGameState().getHero().getAchievementTracker();
    int notYetUnlockedCount = GameData.ACHIEVEMENTS.size() - tracker.getUnlockedCount();
    ArrayList<Achievement> notYetUnlockedAchievements = new ArrayList<Achievement>(notYetUnlockedCount);
    for (Achievement achievement : GameData.ACHIEVEMENTS.values()) {
      if (!tracker.isUnlocked(achievement)) {
        notYetUnlockedAchievements.add(achievement);
      }
    }
    if (notYetUnlockedAchievements.isEmpty()) {
      IO.writeString("All achievements have been unlocked.");
    } else {
      IO.writeAchievementList(notYetUnlockedAchievements);
    }
  }

  private static void printExplorationStatistics() {
    ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
    Table table = new Table("Name", "Kills", "Discovered so far", "Maximum number of visits");
    for (Entry<ID, LocationPreset> entry : GameData.getLocationPresets().entrySet()) {
      String name = entry.getValue().getName();
      String kills = String.valueOf(explorationStatistics.getKillCount(entry.getKey()));
      String discoveredSoFar = String.valueOf(explorationStatistics.getDiscoveredLocations(entry.getKey()));
      String maximumNumberOfVisits = String.valueOf(explorationStatistics.getMaximumNumberOfVisits(entry.getKey()));
      table.insertRow(name, kills, discoveredSoFar, maximumNumberOfVisits);
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
      sb.append("  ").append(creature.getName()).append('\n');
    }
    if (location.getItemCount() != 0) {
      sb.append(Utils.padString("Items (" + location.getItemCount() + "):", WIDTH)).append('\n');
      for (Item item : location.getItemList()) {
        sb.append("  ").append(item.getQualifiedName()).append('\n');
      }
    } else {
      sb.append("No items.\n");
    }
    sb.append(Utils.padString("Luminosity:", WIDTH)).append(location.getLuminosity()).append('\n');
    sb.append(Utils.padString("Permittivity:", WIDTH)).append(location.getLightPermittivity()).append('\n');
    IO.writeString(sb.toString());
  }

  private static void give(String itemID) {
    ItemBlueprint bp = GameData.getItemBlueprints().get(new ID(itemID.toUpperCase()));
    if (bp != null) {
      if (Game.getGameState().getHero().getInventory().addItem(new Item(bp))) {
        return;
      }
    }
    IO.writeString("Item could not be added to your inventory.");
  }

  private static void listAllArguments() {
    StringBuilder builder = new StringBuilder();
    builder.append("Valid commands:");
    for (Command command : commands) {
      builder.append("\n ").append(command.name);
    }
    IO.writeString(builder.toString());
  }

  /**
   * Spawns the specified creatures in the Location the Hero is in.
   * <p/>
   * Note that spawning creatures with this method does NOT change the game state to unsaved.
   */
  private static void spawn(IssuedCommand issuedCommand) {
    if (issuedCommand.getTokenCount() >= 3) {
      for (int i = 1; i < issuedCommand.getArguments().length; i++) {
        ID givenID = new ID(issuedCommand.getArguments()[i].toUpperCase());
        CreatureBlueprint blueprint = GameData.getCreatureBlueprints().get(givenID);
        if (blueprint != null) {
          Game.getGameState().getHeroLocation().addCreature(new Creature(blueprint));
          IO.writeString("Spawned a " + blueprint.getName() + ".");
        } else {
          IO.writeString(givenID + " does not match any CreatureBlueprint.");
        }
      }
    } else {
      Messenger.printMissingArgumentsMessage();
    }
  }

  public static void printIsSaved() {
    if (Game.getGameState().isSaved()) {
      IO.writeString("The game is saved.");
    } else {
      IO.writeString("This game state is not saved.");
    }
  }

  public static void wait(IssuedCommand issuedCommand) {
    if (issuedCommand.getTokenCount() >= 3) {
      try {
        int seconds = Integer.parseInt(issuedCommand.getArguments()[1]);
        Game.getGameState().getWorld().rollDate(seconds);
        IO.writeString("Waited for " + seconds + " seconds.");
      } catch (NumberFormatException warn) {
        Messenger.printInvalidNumberFormatOrValue();
      }
    } else {
      Messenger.printMissingArgumentsMessage();
    }
  }

  public static void printTime() {
    IO.writeString(Game.getGameState().getWorld().getWorldDate().toTimeString());
  }

}
