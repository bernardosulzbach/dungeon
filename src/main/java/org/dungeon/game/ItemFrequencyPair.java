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

package org.dungeon.game;

/**
 * ItemFrequencyPair that wraps Pair in order to provide a simple object to track a creatureID and a frequency value.
 * <p/>
 * Created by Bernardo Sulzbach on 12/11/14.
 */
class ItemFrequencyPair {

  private final Pair<ID, Double> pair;

  public ItemFrequencyPair(ID id, double frequency) {
    this.pair = new Pair<ID, Double>(id, frequency);
  }

  public ID getId() {
    return pair.a;
  }

  public double getFrequency() {
    return pair.b;
  }

}
