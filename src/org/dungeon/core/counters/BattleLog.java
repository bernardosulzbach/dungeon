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
import org.dungeon.core.creatures.enums.CreatureID;
import org.dungeon.core.creatures.enums.CreatureType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * BattleLog class whose objects can keep log of battles.
 * <p/>
 * Created by Bernardo on 20/09/2014.
 */
public class BattleLog implements Serializable {

    private List<BattleLogEntry> entries;

    public BattleLog() {
        entries = new ArrayList<BattleLogEntry>();
    }

    /**
     * Adds a new battle to the BattleLog object.
     */
    public void addBattle(Creature attacker, Creature defender, boolean attackerWon, int turns) {
        entries.add(new BattleLogEntry(attacker, defender, attackerWon, turns));
    }

    /**
     * Return the amount of battles that the attacker won.
     */
    public int getWonBattlesCount() {
        int counter = 0;
        for (BattleLogEntry entry : entries) {
            if (entry.attackerWon) {
                counter += 1;
            }
        }
        return counter;
    }

    /**
     * Returns the length of the longest battle in this battle log.
     */
    public int getLongestBattleLength() {
        int longest = 0;
        for (BattleLogEntry entry : entries) {
            if (entry.turns > longest) {
                longest = entry.turns;
            }
        }
        return longest;
    }

}

class BattleLogEntry implements Serializable {

    protected final CreatureID attackerID;
    protected final CreatureID defenderID;
    protected final CreatureType attackerType;
    protected final CreatureType defenderType;
    protected final boolean attackerWon;
    protected final int turns;

    public BattleLogEntry(Creature attacker, Creature defender, boolean attackerWon, int turns) {
        this.attackerID = attacker.getId();
        this.defenderID = defender.getId();
        this.attackerType = attacker.getType();
        this.defenderType = defender.getType();
        this.attackerWon = attackerWon;
        this.turns = turns;
    }
}