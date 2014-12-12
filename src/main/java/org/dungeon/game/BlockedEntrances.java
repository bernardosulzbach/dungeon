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

package org.dungeon.game;

import java.io.Serializable;
import java.util.HashSet;

/**
 * BlockedEntrances class that defines a allows blocking the entrances of a Locations.
 * <p/>
 * Created by Bernardo Sulzbach on 02/12/14.
 */
public class BlockedEntrances implements Serializable {

    private HashSet<Direction> blockedEntrances;

    public BlockedEntrances() {
    }

    /**
     * Copy constructor.
     *
     * @param source the object to be copied.
     */
    public BlockedEntrances(BlockedEntrances source) {
        if (source.blockedEntrances != null) {
            blockedEntrances = new HashSet<Direction>(source.blockedEntrances);
        }
    }

    public void block(Direction direction) {
        if (blockedEntrances == null) {
            blockedEntrances = new HashSet<Direction>();
        }
        blockedEntrances.add(direction);
    }

    public boolean isBlocked(Direction direction) {
        return blockedEntrances != null && blockedEntrances.contains(direction);
    }

}
