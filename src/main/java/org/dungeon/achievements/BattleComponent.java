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

package org.dungeon.achievements;

import org.dungeon.game.Game;
import org.dungeon.game.ID;
import org.dungeon.stats.BattleStatistics;
import org.dungeon.util.CounterMap;

/**
 * The battle component of the achievements.
 * <p/>
 * Created by Bernardo on 07/12/2014.
 */
final class BattleComponent {

  final CounterMap<String> killsByCreatureType = new CounterMap<String>();
  final CounterMap<ID> killsByCreatureID = new CounterMap<ID>();
  final CounterMap<ID> killsByWeapon = new CounterMap<ID>();
  int battleCount;
  int longestBattleLength;

  public boolean isFulfilled() {
    BattleStatistics statistics = Game.getGameState().getStatistics().getBattleStatistics();
    if (statistics.getBattleCount() < battleCount) {
      return false;
    }
    if (statistics.getLongestBattleLength() < longestBattleLength) {
      return false;
    }
    if (!statistics.getKillsByCreatureID().fulfills(killsByCreatureID)) {
      return false;
    }
    if (!statistics.getKillsByCreatureType().fulfills(killsByCreatureType)) {
      return false;
    }
    if (!statistics.getKillsByWeapon().fulfills(killsByWeapon)) {
      return false;
    }
    return true;
  }

}
