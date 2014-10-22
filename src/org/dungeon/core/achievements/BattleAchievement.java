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

/**
 * Class BattleAchievement that defines a general-purpose battle-related achievements.
 * <p/>
 * Created by Bernardo Sulzbach on 20/09/2014.
 */
public class BattleAchievement extends Achievement {

    private static final long serialVersionUID = 1L;

    private final int battleCount;
    private final int longestBattleLength;
    private final CounterMap<String> idKills;
    private final CounterMap<String> typeKills;
    private final CounterMap<String> weaponKills;

    public BattleAchievement(String name, String info, int experienceReward, int battleCount, int longestBattleLength,
            CounterMap<String> idKills, CounterMap<String> typeKills, CounterMap<String> weaponIdKills) {

        super(name, info, experienceReward);
        this.battleCount = battleCount;
        this.longestBattleLength = longestBattleLength;
        this.idKills = idKills;
        this.typeKills = typeKills;
        this.weaponKills = weaponIdKills;
    }

    @Override
    public boolean update(Hero hero) {
        if (!isUnlocked()) {
            BattleLog log = hero.getBattleLog();
            if (log.getLongestBattleLength() >= longestBattleLength) {
                if (log.getBattlesWonByAttacker() >= battleCount) {
                    if (idKills != null) {
                        for (String id : idKills.keySet()) {
                            if (log.getKillsByID(id) < idKills.getCounter(id)) {
                                return false;
                            }
                        }
                    }
                    if (typeKills != null) {
                        for (String type : typeKills.keySet()) {
                            if (log.getKillsByType(type) < typeKills.getCounter(type)) {
                                return false;
                            }
                        }
                    }
                    if (weaponKills != null) {
                        for (String weapon : weaponKills.keySet()) {
                            if (log.getKillsWithWeapon(weapon) < weaponKills.getCounter(weapon)) {
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
