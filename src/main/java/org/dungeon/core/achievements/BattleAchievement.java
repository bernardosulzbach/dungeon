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

import org.dungeon.core.counters.BattleStatistics;
import org.dungeon.core.counters.CounterMap;
import org.dungeon.core.creatures.Hero;

/**
 * Class BattleAchievement that defines a general-purpose battle-related achievements.
 * <p/>
 * Created by Bernardo Sulzbach on 20/09/2014.
 */
public class BattleAchievement extends Achievement {

    private final int battleCount;
    private final int longestBattleLength;
    private final CounterMap<String> killsByCreatureId;
    private final CounterMap<String> killsByCreatureType;
    private final CounterMap<String> killsByWeapon;

    public BattleAchievement(String id,
                             String name,
                             String info,
                             int experienceReward,
                             int battleCount,
                             int longestBattleLength,
                             CounterMap<String> killsByCreatureId,
                             CounterMap<String> killsByCreatureType,
                             CounterMap<String> weaponIdKills) {
        super(id, name, info, experienceReward);
        this.battleCount = battleCount;
        this.longestBattleLength = longestBattleLength;
        this.killsByCreatureId = killsByCreatureId;
        this.killsByCreatureType = killsByCreatureType;
        this.killsByWeapon = weaponIdKills;
    }

    @Override
    public boolean isFulfilled(Hero hero) {
        BattleStatistics stats = hero.getBattleStatistics();
        if (stats.getBattleCount() < battleCount) {
            return false;
        }
        if (stats.getLongestBattleLength() < longestBattleLength) {
            return false;
        }
        if (killsByCreatureId != null && !stats.getKillsByCreatureId().fulfills(killsByCreatureId)) {
            return false;
        }
        if (killsByCreatureType != null && !stats.getKillsByCreatureType().fulfills(killsByCreatureType)) {
            return false;
        }
        if (killsByWeapon != null && !stats.getKillsByWeapon().fulfills(killsByWeapon)) {
            return false;
        }
        return true;
    }

}
