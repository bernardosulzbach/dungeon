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

package org.dungeon.creatures;

import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.ID;

public abstract class CreatureFactory {

  /**
   * Attempts to create a creature from the model specified by an ID. Returns null if there is not such a model.
   * <p/>
   * Also adds the new creature to the statistics.
   */
  public static Creature makeCreature(ID id) {
    Creature model = GameData.getCreatureModels().get(id);
    if (model != null) {
      Game.getGameState().getStatistics().getWorldStatistics().addSpawn(model.getName());
      return new Creature(model);
    } else {
      return null;
    }
  }

}
