package org.mafagafogigante.dungeon.entity.creatures;

import java.io.Serializable;

public abstract class Effect implements Serializable {

  public abstract void affect(Creature creature);

}
