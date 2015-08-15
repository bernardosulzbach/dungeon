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

package org.dungeon.entity.creatures;

import org.dungeon.game.Id;
import org.dungeon.game.Random;
import org.dungeon.util.Percentage;

import java.io.Serializable;

/**
 * This class represents an item drop law.
 */
class Drop implements Serializable {

  private final Id itemId;
  private final Percentage probability;

  public Drop(Id itemId, Percentage probability) {
    this.itemId = itemId;
    this.probability = probability;
  }

  public Id getItemId() {
    return itemId;
  }

  public boolean rollForDrop() {
    return Random.roll(probability);
  }

  @Override
  public String toString() {
    return "Drop{" +
        "itemId=" + itemId +
        ", probability=" + probability +
        '}';
  }

}
