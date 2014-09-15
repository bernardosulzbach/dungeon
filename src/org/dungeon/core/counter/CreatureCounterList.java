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

import java.util.ArrayList;
import java.util.List;
import org.dungeon.core.game.CreatureID;

/**
 *
 * @author Bernardo Sulzbach
 */
public class CreatureCounterList {
    private final List<CreatureCounter> counters;

    public CreatureCounterList() {
        this.counters = new ArrayList<>();
    }
    
    public void addCounter(CreatureCounter counter) {
        counters.add(counter);
    }
    
    public int getCount(CreatureID id) {
        for (CreatureCounter counter : counters) {
            if (counter.getId() == id) {
                return counter.getValue();
            }
        }
        return 0;
    }
    
    public void incrementCounter(CreatureID id) {
        for (CreatureCounter counter : counters) {
            if (counter.getId() == id) {
                counter.incrementValue();
                return;
            }
        }
        // The counter for this CreatureID does not exist. Create it.
        counters.add(new CreatureCounter(id, 1));
    }
    
}
