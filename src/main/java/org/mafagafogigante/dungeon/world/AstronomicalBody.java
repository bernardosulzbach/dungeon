package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.entity.creatures.Observer;

/**
 * An astronomical body that may be seen from a world.
 */
interface AstronomicalBody {

  boolean isVisible(Observer observer);

  String describeYourself();

}
