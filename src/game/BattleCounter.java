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
package game;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Bernardo Sulzbach
 */
public class BattleCounter implements Serializable {

    private final HashMap<CreatureID, Integer> counters;

    public BattleCounter() {
        counters = new HashMap<>();
    }

    public HashMap<CreatureID, Integer> getCounters() {
        return counters;
    }

    public void incrementCounter(CreatureID id) {
        if (counters.containsKey(id)) {
            counters.put(id, counters.get(id) + 1);
        } else {
            counters.put(id, 1);
        }
    }

    public void setCounter(CreatureID id, int value) {
        counters.put(id, value);
    }

    public int getCounter(CreatureID id) {
        Integer counter = counters.get(id);
        if (counter == null) {
            return 0;
        } else {
            return counter;
        }
    }
}
