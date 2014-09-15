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
package org.dungeon.core.counter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import org.dungeon.core.game.CreatureID;

/**
 * CreatureCounter class.
 *
 * @author Bernardo Sulzbach
 */
public class CreatureCounter implements Serializable {

    private final HashMap<CreatureID, Integer> map;

    public CreatureCounter() {
        this.map = new HashMap<>();
    }

    public CreatureCounter(CreatureID id, int amount) {
        this.map = new HashMap<>();
        this.map.put(id, amount);
    }

    public CreatureCounter(HashMap<CreatureID, Integer> map) {
        this.map = map;
    }

    public Set<CreatureID> getKeySet() {
        return map.keySet();
    }

    public int getCreatureCount(CreatureID id) {
        if (map.containsKey(id)) {
            return map.get(id);
        }
        return 0;
    }

    public void incrementCreatureCount(CreatureID id) {
        incrementCreatureCount(id, 1);
    }

    public void incrementCreatureCount(CreatureID id, int amount) {
        if (map.containsKey(id)) {
            map.put(id, map.get(id) + amount);
        } else {
            map.put(id, amount);
        }
    }
}
