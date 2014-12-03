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
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * A sorted set of integers that can be expanded from both ends.
 * <p/>
 * Created by Bernardo Sulzbach on 01/12/14.
 */
public class ExpandableIntegerSet implements Serializable {

    // DBI stands for distance between integers.
    private final int MIN_DBI;
    // The difference between the minimum DBI and maximum DBI.
    private final int DIFF;

    // A sorted set of integers.
    private final TreeSet<Integer> set = new TreeSet<Integer>();

    /**
     * Make a new ExpandableIntegerSet.
     *
     * @param MIN_DBI the minimum distance between integers. Must be positive.
     * @param MAX_DBI the maximum distance between integers. Must be bigger than <code>MIN_DBI</code>.
     */
    public ExpandableIntegerSet(int MIN_DBI, int MAX_DBI) {
        if (MIN_DBI > 0 && MAX_DBI > MIN_DBI) {
            this.MIN_DBI = MIN_DBI;
            this.DIFF = MAX_DBI - MIN_DBI;
        } else {
            throw new IllegalArgumentException("illegal values for MIN_DBI or MAX_DBI");
        }
        initialize();
    }

    /**
     * Generate the first integer of the set. This method should not be invoked twice.
     */
    void initialize() {
        if (set.size() != 0) {
            throw new IllegalStateException("set already has an element.");
        } else {
            set.add(Engine.RANDOM.nextInt(MIN_DBI));
        }
    }

    /**
     * Expand the set of integers towards an integer a until there is an integer bigger than or equal to a.
     *
     * @return a list with all new integers.
     */
    List<Integer> expand(int a) {
        if (set.size() == 0) {
            throw new IllegalStateException("the set is empty.");
        }
        ArrayList<Integer> integerList = new ArrayList<Integer>();
        int integer = set.last();
        while (a >= integer) {
            integer += MIN_DBI + Engine.RANDOM.nextInt(DIFF);
            integerList.add(integer);
            set.add(integer);
        }
        integer = set.first();
        while (a <= integer) {
            integer -= MIN_DBI + Engine.RANDOM.nextInt(DIFF);
            integerList.add(integer);
            set.add(integer);
        }
        return integerList;
    }

    /**
     * @return true if <code>a</code> is in the set.
     */
    boolean contains(int a) {
        return set.contains(a);
    }

}
