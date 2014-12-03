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
package org.dungeon.core.game;

import java.io.Serializable;

/**
 * River class that implements a river (a line of water in the World).
 * <p/>
 * Created by Bernardo Sulzbach on 01/12/14.
 */
public class River implements Serializable {

    private final ExpandableIntegerSet bridges;

    /**
     * Make a river.
     *
     * @param MIN_DBB the minimum distance between bridges. Must be positive.
     * @param MAX_DBB the maximum distance between bridges. Must be bigger than <code>MIN_DBB</code>.
     */
    River(int MIN_DBB, int MAX_DBB) {
        bridges = new ExpandableIntegerSet(MIN_DBB, MAX_DBB);
    }

    // Expand the set of bridges towards a value of y until there is a bridge at y or after y.
    private void expand(int y) {
        bridges.expand(y);
    }

    boolean isBridge(int y) {
        expand(y);
        return bridges.contains(y);
    }

}
