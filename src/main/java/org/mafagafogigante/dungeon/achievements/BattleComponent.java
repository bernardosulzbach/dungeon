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

package org.mafagafogigante.dungeon.achievements;

import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.stats.BattleStatistics;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * The battle component of the achievements.
 */
final class BattleComponent {

  private final Collection<BattleStatisticsRequirement> requirements;

  BattleComponent(@NotNull Collection<BattleStatisticsRequirement> requirements) {
    this.requirements = requirements;
  }

  /**
   * Checks if this component of the Achievement is fulfilled or not.
   */
  public boolean isFulfilled() {
    BattleStatistics battleStatistics = Game.getGameState().getStatistics().getBattleStatistics();
    for (BattleStatisticsRequirement requirement : requirements) {
      if (!battleStatistics.satisfies(requirement)) {
        return false;
      }
    }
    return true;
  }

}
