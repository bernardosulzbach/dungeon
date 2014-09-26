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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * Counter class that maps a generic key to an integer and provides methods manipulate this integer.
 *
 * @author Bernardo Sulzbach
 */
public class CounterMap<K> implements Serializable {

    private static final long serialVersionUID = 1L;

    // The encapsulated HashMap
    private final HashMap<K, Integer> map;

    // Constructors
    public CounterMap() {
        map = new HashMap<K, Integer>();
    }

    public CounterMap(K key, Integer initialCount) {
        this();
        map.put(key, initialCount);
    }

    public CounterMap(HashMap<K, Integer> map) {
        this.map = map;
    }

    /**
     * Returns a Set view of the keys contained in this map.
     */
    public Set<K> keySet() {
        return map.keySet();
    }

    /**
     * Increments the count of a given key in the CounterMap by 1.
     * <p/>
     * If the key does not exist, it will be created an assigned a value of 1.
     *
     * @return the final value of the counter.
     */
    public int incrementCounter(K key) {
        return incrementCounter(key, 1);
    }

    /**
     * Increments the count of a given key in the CounterMap by a given amount.
     * <p/>
     * If the key does not exist, it will be created an assigned the added value.
     *
     * @return the final value of the counter.
     */
    public int incrementCounter(K key, Integer amount) {
        Integer counter = map.get(key);
        if (counter == null) {
            counter = amount;
        } else {
            counter = counter + amount;
        }
        map.put(key, counter);
        return counter;
    }

    /**
     * Retrieves the counter mapped to a certain key. If no counter is mapped to the provided key, 0 will be returned.
     */
    public int getCounter(K key) {
        Integer counter = map.get(key);
        if (counter == null) {
            return 0;
        } else {
            return counter;
        }
    }

}
