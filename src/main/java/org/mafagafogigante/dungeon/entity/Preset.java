package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.Name;

/**
 * An interface that simplifies Entity instantiation.
 */
public interface Preset {

  Id getId();

  String getType();

  Name getName();

  Weight getWeight();

  Visibility getVisibility();

}
