/*
 * Copyright (C) 2014 Bernardo Sulzbach
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

package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.util.Percentage;

import java.io.Serializable;

/**
 * The weapon component of some items.
 */
public class WeaponComponent implements Serializable {

  private final int damage;
  private final Percentage hitRate;
  private final int integrityDecrementOnHit;

  /**
   * Constructs a new WeaponComponent.
   */
  public WeaponComponent(int damage, Percentage hitRate, int integrityDecrementOnHit) {
    this.damage = damage;
    this.hitRate = hitRate;
    this.integrityDecrementOnHit = integrityDecrementOnHit;
  }

  public int getDamage() {
    return damage;
  }

  public Percentage getHitRate() {
    return hitRate;
  }

  public int getIntegrityDecrementOnHit() {
    return integrityDecrementOnHit;
  }

}