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

package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.util.Percentage;

import java.io.Serializable;
import java.util.Collection;

/**
 * A luminosity value.
 */
public class Luminosity implements Serializable {

  public static final Luminosity ZERO = new Luminosity(new Percentage(0.0));

  private final Percentage value;

  public Luminosity(Percentage value) {
    this.value = value;
  }

  /**
   * Returns a Luminosity value equal to the resultant luminosity of a collection of entities.
   */
  public static Luminosity resultantLuminosity(Collection<Entity> entities) {
    double total = 0;
    for (Entity entity : entities) {
      total += entity.getLuminosity().toPercentage().toDouble();
    }
    return new Luminosity(new Percentage(Math.min(total, 1.0)));
  }

  public Percentage toPercentage() {
    return value;
  }

  @Override
  public String toString() {
    return "Luminosity of " + value;
  }

}
