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

import org.dungeon.core.counters.CounterMap;
import org.dungeon.core.creatures.Creature;
import org.dungeon.core.creatures.enums.CreatureID;
import org.dungeon.core.items.Item;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class World implements Serializable {

    private static final long serialVersionUID = 1L;

    private final HashMap<Point, Location> locations;
    private final CounterMap<CreatureID> spawnCounter;

    private Date worldDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public World() {
        spawnCounter = new CounterMap<CreatureID>();
        locations = new HashMap<Point, Location>();
        initializeCalendar();
    }

    private void initializeCalendar() {
        Calendar calendar = Calendar.getInstance();
        // Set the calendar to the starting game date.
        calendar.set(1985, Calendar.JUNE, 1, 6, 0, 0);
        worldDate = new Date();
        worldDate.setTime(calendar.getTimeInMillis());
    }


    public void addLocation(Location locationObject, Point coordinates) {
        locations.put(coordinates, locationObject);
    }

    /**
     * Add a creature to a specific location.
     */
    public void addCreature(Creature creature, Point coordinates) {
        locations.get(coordinates).addCreature(creature);
        creature.setLocation(getLocation(coordinates));
        spawnCounter.incrementCounter(creature.getId());
    }

    public void addCreatureArray(Creature[] creatures, Point coordinates) {
        locations.get(coordinates).addCreatureArray(creatures);
        for (Creature creature : creatures) {
            creature.setLocation(getLocation(coordinates));
        }
        spawnCounter.incrementCounter(creatures[0].getId(), creatures.length);
    }

    /**
     * Add an item to a specific location.
     */
    public void addItem(Item item, Point locationPoint) {
        locations.get(locationPoint).addItem(item);
    }

    /**
     * Move a Creature from origin to destination.
     */
    public void moveCreature(Creature creature, Point origin, Point destination) {
        if (locations.get(origin).hasCreature(creature)) {
            locations.get(origin).removeCreature(creature);
            locations.get(destination).addCreature(creature);
            System.out.printf("You arrive at %s.\n", locations.get(destination).getName());
        } else {
            throw new IllegalArgumentException("Creature is not in the origin.");
        }
    }

    public boolean hasLocation(Point point) {
        return locations.containsKey(point);
    }

    public Location getLocation(Point point) {
        return locations.get(point);
    }

    /**
     * Returns a string corresponding to the current part of the day.
     */
    public String getDayPartString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(worldDate);
        int hour = calendar.get(Calendar.HOUR);
        if (1 <= hour && hour <= 4) {
            return "Night";
        } else if (hour == 5 || hour == 6) {
            return "Dawn";
        } else if (7 <= hour && hour <= 10) {
            return "Morning";
        } else if (hour == 11 || hour == 12) {
            return "Noon";
        } else if (13 <= hour && hour <= 16) {
            return "Afternoon";
        } else if (hour == 17 || hour == 18) {
            return "Dusk";
        } else if (19 <= hour || hour <= 22) {
            return "Evening";
        } else {
            return "Midnight";
        }
    }

    /**
     * Prints all the spawn counters.
     */
    public void printSpawnCounters() {
        StringBuilder sb = new StringBuilder();
        for (CreatureID id : spawnCounter.keySet()) {
            sb.append(Constants.MARGIN).append(String.format("%-20s%10d\n", id, spawnCounter.getCounter(id)));
        }
        IO.writeString(sb.toString());
    }

    /**
     * Prints the current date and time of the world.
     */
    public void printDateAndTime() {
        IO.writeString(dateFormat.format(worldDate.getTime()) + " " + "(" + getDayPartString() + ")");
    }

    /**
     * Rolls the world date a given amount of minutes forward.
     */
    public void rollDate(int minutes) {
        int milliseconds = 1000 * 60 * minutes;
        worldDate.setTime(worldDate.getTime() + milliseconds);
    }

}
