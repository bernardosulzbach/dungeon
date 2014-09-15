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

import org.dungeon.core.game.CreatureID;

/**
 * CreatureCounter class.
 *
 * @author Bernardo Sulzbach
 */
public class CreatureCounter {

    private final CreatureID id;
    private int value;

    public CreatureCounter(CreatureID id) {
        this.id = id;
        this.value = 0;
    }

    public CreatureCounter(CreatureID id, int value) {
        this.id = id;
        this.value = value;
    }

    public CreatureID getId() {
        return id;
    }

    public int getValue() {
        return value;
    }
    
    public void incrementValue() {
        value++;
    }

}
