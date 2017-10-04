package org.mafagafogigante.dungeon.entity;

import java.util.ArrayList;
import java.util.List;

public class Damage {

  private final List<DamageAmount> amounts = new ArrayList<>();

  public List<DamageAmount> getAmounts() {
    return amounts;
  }

}
