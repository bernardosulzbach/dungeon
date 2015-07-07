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

import org.dungeon.entity.Entity;
import org.dungeon.entity.items.Item;
import org.dungeon.game.Name;
import org.dungeon.io.IO;
import org.dungeon.util.CounterMap;
import org.dungeon.util.Matches;
import org.dungeon.util.Messenger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class HeroUtils {

  private HeroUtils() {
    throw new AssertionError();
  }

  /**
   * Returns whether all Entities in a Collection have the same name or not.
   *
   * @param entities a {@code Collection} of Entities
   * @param ignored  an Entity to be ignored, should be {@code null} if no Entity is to be ignored
   * @return a boolean indicating if all Entities in the collection have the same name
   */
  static boolean checkIfAllEntitiesHaveTheSameName(Collection<? extends Entity> entities, Entity ignored) {
    Name lastSeenName = null;
    for (Entity entity : entities) {
      if (ignored == null || entity != ignored) {
        if (lastSeenName == null) {
          lastSeenName = entity.getName();
        } else {
          if (!entity.getName().equals(lastSeenName)) { // Got an Entity with a different name.
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * Attempts to find an item by its name in a specified Inventory.
   *
   * @return an Item object if there is a match. null otherwise.
   */
  static Item findItem(List<Item> items, String[] tokens) {
    Matches<Item> matches = org.dungeon.util.Utils.findBestCompleteMatches(items, tokens);
    if (matches.size() == 0) {
      IO.writeString("Item not found.");
    } else if (matches.size() == 1 || matches.getDifferentNames() == 1) {
      return matches.getMatch(0);
    } else {
      Messenger.printAmbiguousSelectionMessage();
    }
    return null;
  }

  /**
   * Filters a List of Creatures, returning all that have a specified Tag.
   */
  static List<Creature> filterByTag(List<Creature> list, Creature.Tag tag) {
    List<Creature> visible = new ArrayList<Creature>();
    for (Creature candidate : list) {
      if (candidate.hasTag(tag)) {
        visible.add(candidate);
      }
    }
    return visible;
  }

  /**
   * Returns a String representation of the enumeration of all the Entities in a given List.
   */
  static String enumerateEntities(final List<? extends Entity> listOfEntities) {
    CounterMap<Name> nameOccurrences = new CounterMap<Name>();
    for (Entity entity : listOfEntities) {
      nameOccurrences.incrementCounter(entity.getName());
    }
    ArrayList<String> quantifiedNames = new ArrayList<String>();
    for (Name name : nameOccurrences.keySet()) {
      quantifiedNames.add(name.getQuantifiedName(nameOccurrences.getCounter(name)));
    }
    return org.dungeon.util.Utils.enumerate(quantifiedNames);
  }

  /**
   * Returns whether all Entities in a Collection have the same name or not.
   *
   * @param entities a {@code Collection} of Entities
   * @return a boolean indicating if all Entities in the collection have the same name
   */
  static boolean checkIfAllEntitiesHaveTheSameName(Collection<? extends Entity> entities) {
    return checkIfAllEntitiesHaveTheSameName(entities, null);
  }

  static void writeNoLongerInInventoryMessage(Item item) {
    IO.writeString(item.getQualifiedName() + " is no longer in the inventory.");
  }

  public static void writeNoLongerInLocationMessage(Item item) {
    IO.writeString(item.getQualifiedName() + " is no longer in this location.");
  }

}
