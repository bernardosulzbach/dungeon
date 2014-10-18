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
package org.dungeon.core.counters;

import org.dungeon.core.creatures.Creature;
import org.dungeon.core.creatures.CreatureID;
import org.dungeon.core.creatures.CreatureType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * BattleLog class whose objects can keep log of battles.
 * <p/>
 * Created by Bernardo on 20/09/2014.
 */
public class BattleLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private int totalBattles;
    private int battlesWonByAttacker;
    private int longestBattleLength;

    private final List<BattleLogEntry> entries;

    public BattleLog() {
        entries = new ArrayList<BattleLogEntry>();
    }

    /**
     * Adds a new battle to the BattleLog object.
     */
    public void addBattle(Creature attacker, Creature defender, boolean attackerWon, int turns) {
        entries.add(new BattleLogEntry(attacker, defender, attackerWon, turns));
        //
        // After creating the fields totalBattles, battlesWonByAttacker and longestBattleLength, this method also needs
        // to set these values appropriately.
        //
        // Bernardo Sulzbach (mafagafogigante @ 21/09/2014): querying private int fields with their respective getters
        //   should have much better performance than unnecessarily iterating over all the elements of a list of
        //   BattleLogEntry objects just to get how many battles the attacker won.
        //
        setTotalBattles(getTotalBattles() + 1);
        if (attackerWon) {
            setBattlesWonByAttacker(getBattlesWonByAttacker() + 1);
        }
        if (turns > getLongestBattleLength()) {
            setLongestBattleLength(turns);
        }
    }

    public int getTotalBattles() {
        return totalBattles;
    }

    public void setTotalBattles(int totalBattles) {
        this.totalBattles = totalBattles;
    }

    /**
     * Return the amount of battles that the attacker won.
     */
    public int getBattlesWonByAttacker() {
        return battlesWonByAttacker;
    }

    public void setBattlesWonByAttacker(int battlesWonByAttacker) {
        this.battlesWonByAttacker = battlesWonByAttacker;
    }

    /**
     * Returns the length of the longest battle in this battle log.
     */
    public int getLongestBattleLength() {
        return longestBattleLength;
    }

    public void setLongestBattleLength(int longestBattleLength) {
        this.longestBattleLength = longestBattleLength;
    }

    /**
     * Return the kill count for a certain CreatureType.
     */
    public int getKills(CreatureType type) {
        int kills = 0;
        for (BattleLogEntry entry : entries) {
            if (entry.attackerWon && entry.defenderType == type) {
                kills++;
            }
        }
        return kills;
    }

    /**
     * Return the kill count for a certain CreatureID.
     */
    public int getKills(CreatureID id) {
        int kills = 0;
        for (BattleLogEntry entry : entries) {
            if (entry.attackerWon && entry.defenderID == id) {
                kills++;
            }
        }
        return kills;
    }

    /**
     * Return the kill count for a certain weapon identification string.
     */
    public int getKillsWithWeapon(String weaponID) {
        int kills = 0;
        for (BattleLogEntry entry : entries) {
            if (entry.attackerWon && entry.attackerWeapon.equals(weaponID)) {
                kills++;
            }
        }
        return kills;
    }

}
