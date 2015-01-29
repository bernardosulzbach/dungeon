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

import org.dungeon.creatures.Creature;
import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.GameState;
import org.dungeon.game.ID;
import org.dungeon.game.IssuedCommand;
import org.dungeon.game.Location;
import org.dungeon.game.Point;
import org.dungeon.io.IO;
import org.dungeon.items.Item;
import org.dungeon.items.ItemBlueprint;
import org.dungeon.util.Utils;

import java.util.ArrayList;
import java.util.List;

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
        if (issuedCommand.firstArgumentEquals(command.getName())) {
          command.execute(issuedCommand);
          return;
        }
      }
      IO.writeString("Command not recognized.");
    } else {
      Utils.printMissingArgumentsMessage();
    }

  }

  /**
   * Creates all debugging Commands.
   * <p/>
   * This method also sets {@code uninitialized} to false.
   */
  private static void initialize() {
    commands.add(new Command("exploration") {
      @Override
      public void execute(IssuedCommand issuedCommand) {
        IO.writeString(Game.getGameState().getStatistics().getExplorationStatistics().toString());
      }
    });
    commands.add(new Command("tomorrow") {
      @Override
      void execute(IssuedCommand issuedCommand) {
        Game.getGameState().getWorld().rollDate(24 * 60 * 60);
        IO.writeString("A day has passed.");
      }
    });
    commands.add(new Command("location") {
      @Override
      void execute(IssuedCommand issuedCommand) {
        printCurrentLocationInformation();
      }
    });
    commands.add(new Command("saved") {
      @Override
      void execute(IssuedCommand issuedCommand) {
        printIsSaved();
      }
    });
    commands.add(new Command("list") {
      @Override
      void execute(IssuedCommand issuedCommand) {
        listAllArguments();
      }
    });
    commands.add(new Command("wait") {
      @Override
      void execute(IssuedCommand issuedCommand) {
        DebugTools.wait(issuedCommand);
      }
    });
    commands.add(new Command("time") {
      @Override
      void execute(IssuedCommand issuedCommand) {
        printTime();
      }
    });
    commands.add(new Command("give") {
      @Override
      void execute(IssuedCommand issuedCommand) {
        if (issuedCommand.getTokenCount() >= 3) {
          give(issuedCommand.getArguments()[1]);
        } else {
          Utils.printMissingArgumentsMessage();
        }
      }
    });
    // TODO: make a more generic command such as "spawn".
    commands.add(new Command("dummy") {
      @Override
      void execute(IssuedCommand issuedCommand) {
        spawnDummyInHeroLocation();
      }
    });
    uninitialized = false;
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
      builder.append("\n ").append(command.getName());
    }
    IO.writeString(builder.toString());
  }

  /**
   * Spawns a dummy in the Location the Hero is in.
   */
  private static void spawnDummyInHeroLocation() {
    Creature dummy = new Creature(GameData.getCreatureBlueprints().get(new ID("DUMMY")));
    Game.getGameState().getHeroLocation().addCreature(dummy);
    IO.writeString("Spawned a dummy.");
  }

  public static void printIsSaved() {
    if (Game.getGameState().isSaved()) {
      IO.writeString("The game is saved.");
    } else {
      IO.writeString("This game state is not saved.");
    }
  }

  public static void wait(IssuedCommand issuedCommand) {
    if (issuedCommand.getTokenCount() > 2) {
      try {
        int seconds = Integer.parseInt(issuedCommand.getArguments()[1]);
        Game.getGameState().getWorld().rollDate(seconds);
      } catch (NumberFormatException warn) {
        // TODO: maybe this and fibonacci should share a message for invalid number format.
        IO.writeString("Not a valid amount of seconds.");
      }
    } else {
      Utils.printMissingArgumentsMessage();
    }
  }

  public static void printTime() {
    IO.writeString(Game.getGameState().getWorld().getWorldDate().toTimeString());
  }

}
