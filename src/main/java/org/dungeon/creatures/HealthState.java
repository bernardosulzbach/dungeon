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

package org.dungeon.creatures;

/**
 * HealthState enum that defines the six stages of healthiness.
 * <p/>
 * Created by Bernardo on 06/02/2015.
 */
public enum HealthState {

  UNINJURED("Uninjured"),
  BARELY_INJURED("Barely Injured"),
  INJURED("Injured"),
  BADLY_INJURED("Badly Injured"),
  NEAR_DEATH("Near Death"),
  DEAD("Dead");

  private final String stringRepresentation;

  HealthState(String stringRepresentation) {
    this.stringRepresentation = stringRepresentation;
  }

  public static HealthState getHealthState(int curHealth, int maxHealth) {
    double fraction = curHealth / (double) maxHealth;
    return values()[(int) ((values().length - 1) * (1 - fraction))];
  }

  @Override
  public String toString() {
    return stringRepresentation;
  }

}
