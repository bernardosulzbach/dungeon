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

package org.dungeon.core.achievements;

import org.dungeon.core.counters.BattleLog;
import org.dungeon.core.counters.CounterMap;
import org.dungeon.core.creatures.Hero;
import org.dungeon.core.creatures.enums.CreatureID;
import org.dungeon.core.creatures.enums.CreatureType;

/**
 * Class BattleAchievement that defines a general-purpose battle-related achievement.
 * Created by Bernardo on 20/09/2014.
 */
public class BattleAchievement extends Achievement {

    private final int battleCount;
    private final int longestBattleLength;
    private final CounterMap<CreatureID> idKills;
    private final CounterMap<CreatureType> typeKills;

    public BattleAchievement(String name,
                             String info,
                             int experienceReward,
                             int battleCount,
                             int longestBattleLength,
                             CounterMap<CreatureID> idKills,
                             CounterMap<CreatureType> typeKills) {

        super(name, info, experienceReward);
        this.battleCount = battleCount;
        this.longestBattleLength = longestBattleLength;
        this.idKills = idKills;
        this.typeKills = typeKills;
    }

    @Override
    public boolean update(Hero hero) {
        if (!isUnlocked()) {
            BattleLog log = hero.getBattleLog();
            if (log.getLongestBattleLength() >= longestBattleLength) {
                if (log.getBattlesWonByAttacker() >= battleCount) {
                    if (idKills != null) {
                        for (CreatureID id : idKills.keySet()) {
                            if (log.getKills(id) < idKills.getCounter(id)) {
                                return false;
                            }
                        }
                    }
                    if (typeKills != null) {
                        for (CreatureType type : typeKills.keySet()) {
                            if (log.getKills(type) < typeKills.getCounter(type)) {
                                return false;
                            }
                        }
                    }
                    // All the requirements OK, unlock the achievement.
                    printAchievementUnlocked();
                    setUnlocked(true);
                    return true;
                }
            }
        }
        return false;
    }
}
