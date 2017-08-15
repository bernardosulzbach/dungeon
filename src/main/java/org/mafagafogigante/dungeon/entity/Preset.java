package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.game.BaseName;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.util.Percentage;

/**
 * An interface that simplifies Entity instantiation.
 */
public interface Preset {

  Id getId();

  String getType();

  BaseName getName();

  Weight getWeight();

  Percentage getVisibility();

}
