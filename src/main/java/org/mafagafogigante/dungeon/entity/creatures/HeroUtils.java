package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.entity.Entity;
import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.game.BaseName;
import org.mafagafogigante.dungeon.game.Name;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.util.Matches;
import org.mafagafogigante.dungeon.util.Messenger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class HeroUtils {

  private HeroUtils() {
    throw new AssertionError();
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

  /**
   * Returns whether all Entities in a Collection have the same name or not.
   *
   * @param entities a {@code Collection} of Entities
   * @param ignored an Entity to be ignored, should be {@code null} if no Entity is to be ignored
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
   * Attempts to find items by their name in a specified Inventory.
   */
  public static List<Item> findItems(List<Item> items, String[] tokens) {
    Matches<Item> matches = Matches.findBestCompleteMatches(items, tokens);
    if (matches.size() == 0) {
      Writer.write("Item not found.");
    } else if (matches.isDisjoint()) {
      // Matches are disjoint, if there is more than one different name, the results are ambiguous.
      if (matches.size() == 1 || matches.getDifferentNames() == 1) {
        return Collections.singletonList(matches.getMatch(0));
      } else {
        Messenger.printAmbiguousSelectionMessage();
      }
    } else {
      return matches.toList();
    }
    return Collections.emptyList();
  }

  /**
   * Filters a List of Creatures, returning all that have a specified Tag.
   */
  static List<Creature> filterByTag(List<Creature> list, Creature.Tag tag) {
    List<Creature> visible = new ArrayList<>();
    for (Creature candidate : list) {
      if (candidate.hasTag(tag)) {
        visible.add(candidate);
      }
    }
    return visible;
  }

  static void writeNoLongerInInventoryMessage(Item item) {
    Writer.write(item.getQualifiedName() + " is no longer in the inventory.");
  }

  static void writeNoLongerInLocationMessage(Item item) {
    Writer.write(item.getQualifiedName() + " is no longer in this location.");
  }

}
