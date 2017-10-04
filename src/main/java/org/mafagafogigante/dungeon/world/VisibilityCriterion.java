package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.entity.creatures.Observer;

/**
 * A criterion that describes whether or not something is visible.
 */
public interface VisibilityCriterion {

  /**
   * Evaluates whether or not this criterion is met by the specified observer.
   */
  boolean isMetBy(Observer observer);

}
