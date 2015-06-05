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
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.util.CounterMap;

/**
 * The battle component of the achievements.
 */
final class BattleComponent {

  private final int minimumBattleCount;
  private final int longestBattleLength;
  private final CounterMap<ID> killsByCreatureID;
  private final CounterMap<String> killsByCreatureType;
  private final CounterMap<CauseOfDeath> killsByCauseOfDeath;

  BattleComponent(int minimumBattleCount, int longestBattleLength, CounterMap<ID> killsByCreatureID,
      CounterMap<String> killsByCreatureType, CounterMap<CauseOfDeath> killsByCauseOfDeath) {
    this.minimumBattleCount = minimumBattleCount;
    this.longestBattleLength = longestBattleLength;
    this.killsByCreatureID = killsByCreatureID;
    this.killsByCreatureType = killsByCreatureType;
    this.killsByCauseOfDeath = killsByCauseOfDeath;
  }

  /**
   * Checks if this component of the Achievement is fulfilled or not.
   */
  public boolean isFulfilled() {
    BattleStatistics statistics = Game.getGameState().getStatistics().getBattleStatistics();
    return statistics.getBattleCount() >= minimumBattleCount
        && statistics.getLongestBattleLength() >= longestBattleLength
        && (killsByCreatureID == null || statistics.getKillsByCreatureID().fulfills(killsByCreatureID))
        && (killsByCreatureType == null || statistics.getKillsByCreatureType().fulfills(killsByCreatureType))
        && (killsByCauseOfDeath == null || statistics.getKillsByCauseOfDeath().fulfills(killsByCauseOfDeath));
  }

}
