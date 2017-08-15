package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.Name;
import org.mafagafogigante.dungeon.util.Percentage;

/**
 * An interface that simplifies Entity instantiation.
 */
public interface Preset {

  Id getId();

  String getType();

  Name getName();

  Weight getWeight();

  Percentage getVisibility();

}
