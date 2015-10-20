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

package org.dungeon.entity;

import java.io.Serializable;

/**
 * A source of Luminosity.
 */
public class LightSource implements Serializable {

  private final Luminosity luminosity;
  private boolean enabled = true;

  public LightSource(Luminosity luminosity) {
    this.luminosity = luminosity;
  }

  /**
   * Enables this source until disable is called.
   */
  public void enable() {
    this.enabled = true;
  }

  /**
   * Disables this source until enable is called.
   */
  public void disable() {
    this.enabled = false;
  }

  /**
   * Returns the luminosity of this light source.
   */
  public Luminosity getLuminosity() {
    if (enabled) {
      return luminosity;
    } else {
      return Luminosity.ZERO;
    }
  }

  @Override
  public String toString() {
    return (enabled ? "Enabled" : "Disabled") + " LightSource of luminosity of " + luminosity.toPercentage();
  }

}
