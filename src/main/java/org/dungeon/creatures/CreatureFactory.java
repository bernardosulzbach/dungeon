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

package org.dungeon.creatures;

import org.dungeon.date.Date;
import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.ID;
import org.dungeon.io.DLogger;
import org.dungeon.items.CreatureInventory.AdditionResult;
import org.dungeon.items.ItemFactory;
import org.dungeon.util.Constants;

public abstract class CreatureFactory {

  /**
   * Attempts to create a creature from the CreaturePreset specified by an ID. Returns null if no preset was found.
   * <p/>
   * Also adds the new creature to the statistics.
   */
  public static Creature makeCreature(ID id) {
    CreaturePreset preset = GameData.getCreaturePresets().get(id);
    if (preset != null) {
      Game.getGameState().getStatistics().getWorldStatistics().addSpawn(preset.getName().getSingular());
      Creature creature = new Creature(preset);
      giveItems(creature, id);
      return creature;
    } else {
      return null;
    }
  }

  public static Hero makeHero() {
    Hero hero = new Hero(GameData.getCreaturePresets().get(Constants.HERO_ID));
    giveItems(hero, Constants.HERO_ID);
    return hero;
  }

  private static void giveItems(Creature creature, ID id) {
    for (ID itemID : GameData.getCreaturePresets().get(id).getItems()) {
      Date date = Game.getGameState().getWorld().getWorldDate();
      AdditionResult result = creature.getInventory().addItem(ItemFactory.makeItem(itemID, date));
      if (result != AdditionResult.SUCCESSFUL) {
        DLogger.warning("Could not add " + itemID + " to " + id + "! Got " + result + ".");
      }
    }
  }

}
