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

package org.dungeon.entity.creatures;

import org.dungeon.date.Date;
import org.dungeon.entity.items.CreatureInventory.AdditionResult;
import org.dungeon.entity.items.Item;
import org.dungeon.entity.items.ItemFactory;
import org.dungeon.game.Game;
import org.dungeon.game.ID;
import org.dungeon.io.DLogger;
import org.dungeon.util.Constants;

import java.util.Map;

public abstract class CreatureFactory {

  private static Map<ID, CreaturePreset> creaturePresetMap;

  public static void setCreaturePresetMap(Map<ID, CreaturePreset> creaturePresetMap) {
    if (CreatureFactory.creaturePresetMap == null) {
      CreatureFactory.creaturePresetMap = creaturePresetMap;
    } else {
      throw new AssertionError("Tried to set the CreaturePreset Map a second time!");
    }
  }

  /**
   * Attempts to create a creature from the CreaturePreset specified by an ID. Returns null if no preset was found.
   * <p/>
   * Also adds the new creature to the statistics.
   */
  public static Creature makeCreature(ID id) {
    CreaturePreset preset = creaturePresetMap.get(id);
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
    Hero hero = new Hero(creaturePresetMap.get(Constants.HERO_ID));
    giveItems(hero, Constants.HERO_ID);
    return hero;
  }

  /**
   * Gives a Creature all the Items defined in the corresponding CreaturePreset and equips its weapon, if there is one.
   */
  private static void giveItems(Creature creature, ID id) {
    CreaturePreset preset = creaturePresetMap.get(id);
    for (ID itemID : preset.getItems()) {
      Date date = Game.getGameState().getWorld().getWorldDate();
      AdditionResult result = creature.getInventory().addItem(ItemFactory.makeItem(itemID, date));
      if (result != AdditionResult.SUCCESSFUL) {
        DLogger.warning("Could not add " + itemID + " to " + id + "! Got " + result + ".");
      }
    }
    equipWeapon(creature, preset);
  }

  private static void equipWeapon(Creature creature, CreaturePreset preset) {
    if (preset.getWeaponID() != null) {
      // Get the weapon from the creature's inventory.
      for (Item item : creature.getInventory().getItems()) {
        if (item.getID().equals(preset.getWeaponID())) {
          creature.setWeapon(item);
          break;
        }
      }
      if (!creature.hasWeapon()) { // Did not found a suitable Item in the inventory.
        DLogger.warning(String.format("%s not found in the inventory of %s!", preset.getWeaponID(), preset.getID()));
      }
    }
  }

}
