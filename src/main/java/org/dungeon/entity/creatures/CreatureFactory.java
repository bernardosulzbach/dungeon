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
import org.dungeon.entity.items.CreatureInventory.SimulationResult;
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
      giveItems(creature);
      return creature;
    } else {
      return null;
    }
  }

  /**
   * Creates the Hero.
   *
   * @param date the Date when the Items the Hero has were created
   * @return the Hero object
   */
  public static Hero makeHero(Date date) {
    Hero hero = new Hero(creaturePresetMap.get(Constants.HERO_ID));
    giveItems(hero, date);
    return hero;
  }

  /**
   * Gives a Creature all the Items defined in the corresponding CreaturePreset and equips its weapon, if there is one.
   * The Date of creation of the Items will be retrieved from the GameState stored in Game.
   * If that field is null or invalid, use the overloaded version of this method that requires a Date object.
   *
   * @param creature the Creature
   */
  private static void giveItems(Creature creature) {
    giveItems(creature, Game.getGameState().getWorld().getWorldDate());
  }

  /**
   * Gives a Creature all the Items defined in the corresponding CreaturePreset and equips its weapon, if there is one.
   *
   * @param creature the Creature
   * @param date     the Date when the Items this Creature has were created
   */
  private static void giveItems(Creature creature, Date date) {
    CreaturePreset preset = creaturePresetMap.get(creature.getID());
    for (ID itemID : preset.getItems()) {
      Item item = ItemFactory.makeItem(itemID, date);
      SimulationResult result = creature.getInventory().simulateItemAddition(item);
      if (result == SimulationResult.SUCCESSFUL) {
        creature.getInventory().addItem(item);
      } else {
        DLogger.warning("Could not add " + itemID + " to " + creature.getID() + ". Reason: " + result + ".");
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
        DLogger.warning(String.format("%s not found in the inventory of %s!", preset.getWeaponID(), creature.getID()));
      }
    }
  }

}
