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

package org.dungeon.util;

import org.dungeon.io.DLogger;

import java.io.Serializable;

/**
 * A class that represents a percentage value.
 * <p/>
 * Created by Bernardo Sulzbach on 26/12/14.
 */
public class Percentage implements Serializable {

  private static final double ONE = 1.0;
  private static final double ZERO = 0.0;

  private final double value;

  public Percentage(double percentage) {
    if (percentage < ZERO) {
      value = ZERO;
      DLogger.warning("Tried to use " + percentage + " as a percentage. Used " + ZERO + " instead.");
    } else if (percentage > ONE) {
      value = ONE;
      DLogger.warning("Tried to use " + percentage + " as a percentage. Used " + ONE + " instead.");
    } else {
      value = percentage;
    }
  }

  public double toDouble() {
    return value;
  }

  @Override
  public String toString() {
    return String.format("%.2f%%", value * 100);
  }

}
