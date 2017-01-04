package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * A simple wrapper for a Percentage object that represents how visible an Entity is.
 */
public class Visibility implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final Percentage value;

  public Visibility(@NotNull Percentage value) {
    this.value = value;
  }

  /**
   * Evaluates if an Entity with this Visibility should be visible under the specified luminosity.
   *
   * @param luminosity a Percentage, not null
   * @return true if an Entity with this Visibility is visible, false otherwise
   */
  public boolean visibleUnder(Luminosity luminosity) {
    return Double.compare(luminosity.toPercentage().toDouble(), 1 - value.toDouble()) >= 0;
  }

  public Percentage toPercentage() {
    return value;
  }

  @Override
  public String toString() {
    return "Visibility of " + value;
  }

}
