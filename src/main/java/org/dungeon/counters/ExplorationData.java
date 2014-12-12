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

package org.dungeon.counters;

import java.io.Serializable;

/**
 * An ExplorationLog entry.
 * <p/>
 * Created by Bernardo Sulzbach on 03/11/14.
 */
class ExplorationData implements Serializable {

    private String locationID;
    private int visitCount;
    private int killCount;

    public ExplorationData(int visitCount, int killCount) {
        this.visitCount = visitCount;
        this.killCount = killCount;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public int addVisit() {
        return ++this.visitCount;
    }

    public int getKillCount() {
        return killCount;
    }

    public int addKill() {
        return ++this.killCount;
    }

}
