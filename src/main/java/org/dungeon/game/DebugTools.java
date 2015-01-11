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

import org.dungeon.creatures.Creature;
import org.dungeon.io.IO;
import org.dungeon.io.Loader;
import org.dungeon.items.Item;
import org.dungeon.items.ItemBlueprint;
import org.dungeon.util.Utils;

import java.awt.Color;

/**
 * A set of debugging tools.
 * <p/>
 * Created by Bernardo Sulzbach on 03/11/14.
 */
class DebugTools {

  private static final String[] args = {"exploration", "tomorrow", "holidays", "saves", "location", "generator",
      "saved", "list", "time", "give", "dummy"};

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
    IO.writeString("Arguments are: ");
    for (String arg : args) {
      IO.writeString("\n  " + arg);
    }
  }

  /**
   * Spawns a dummy in the Location the Hero is in.
   */
  private static void spawnDummyInHeroLocation() {
    Creature dummy = new Creature(GameData.getCreatureBlueprints().get(new ID("DUMMY")));
    Game.getGameState().getHeroLocation().addCreature(dummy);
    IO.writeString("Spawned a dummy.");
  }

  private static void printIsSaved() {
    if (Game.getGameState().isSaved()) {
      IO.writeString("The game is saved.");
    } else {
      IO.writeString("This game state is not saved.");
    }
  }

  private static void printTime() {
    IO.writeString(Game.getGameState().getWorld().getWorldDate().toTimeString());
  }

  static void parseDebugCommand(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      if (issuedCommand.firstArgumentEquals(args[0])) {
        IO.writeString(Game.getGameState().getHero().getExplorationLog().toString());
      } else if (issuedCommand.firstArgumentEquals(args[1])) {
        Game.getGameState().getWorld().rollDate(24 * 60 * 60);
        if (Engine.RANDOM.nextBoolean()) {
          IO.writeString("A day has passed.", Color.ORANGE);
        } else {
          IO.writeString("You are one day closer to your ending.", Color.ORANGE);
        }
      } else if (issuedCommand.firstArgumentEquals(args[3])) {
        Loader.printFilesInSavesFolder();
      } else if (issuedCommand.firstArgumentEquals(args[4])) {
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
      } else if (issuedCommand.firstArgumentEquals(args[5])) {
        Game.getGameState().getWorld().getGenerator().printStatistics();
      } else if (issuedCommand.firstArgumentEquals(args[6])) {
        printIsSaved();
      } else if (issuedCommand.firstArgumentEquals(args[7])) {
        listAllArguments();
      } else if (issuedCommand.firstArgumentEquals(args[8])) {
        printTime();
      } else if (issuedCommand.firstArgumentEquals(args[9])) {
        if (issuedCommand.getTokenCount() >= 3) {
          give(issuedCommand.getArguments()[1]);
        } else {
          Utils.printMissingArgumentsMessage();
        }
      } else if (issuedCommand.firstArgumentEquals(args[10])) {
        spawnDummyInHeroLocation();
      } else {
        IO.writeString("Command not recognized.");
      }
    } else {
      Utils.printMissingArgumentsMessage();
    }
  }

}
