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
import org.dungeon.io.WriteStyle;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class World implements Serializable {

    private static final long serialVersionUID = 1L;

    private final CounterMap<CreatureID> spawnCounter;

    private final HashMap<Point, Location> locations;

    private final Date worldDate;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public World() {
        spawnCounter = new CounterMap<CreatureID>();
        locations = new HashMap<Point, Location>();
        worldDate = new Date();
        initializeCalendar();
    }

    private void initializeCalendar() {
        Calendar calendar = Calendar.getInstance();
        // Set the calendar to the starting game date.
        calendar.set(1985, Calendar.JUNE, 1, 6, 0, 0);
        worldDate.setTime(calendar.getTimeInMillis());
    }

    public Date getWorldDate() {
        return worldDate;
    }

    public void addLocation(Location locationObject, Point coordinates) {
        locations.put(coordinates, locationObject);
        locationObject.setWorld(this);
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
     * Returns the PartOfDay constant that represents the current part of the day.
     */
    // TODO: consider moving this method to PartOfDay
    public PartOfDay getDayPart() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(worldDate);
        int hour = calendar.get(Calendar.HOUR);
        if (1 <= hour && hour <= 4) {
            return PartOfDay.NIGHT;
        } else if (hour == 5 || hour == 6) {
            return PartOfDay.DAWN;
        } else if (7 <= hour && hour <= 10) {
            return PartOfDay.MORNING;
        } else if (hour == 11 || hour == 12) {
            return PartOfDay.NOON;
        } else if (13 <= hour && hour <= 16) {
            return PartOfDay.AFTERNOON;
        } else if (hour == 17 || hour == 18) {
            return PartOfDay.DUSK;
        } else if (19 <= hour || hour <= 22) {
            return PartOfDay.EVENING;
        } else {
            return PartOfDay.MIDNIGHT;
        }
    }

    /**
     * Prints all the spawn counters.
     */
    public void printSpawnCounters() {
        StringBuilder sb = new StringBuilder();
        for (CreatureID id : spawnCounter.keySet()) {
            sb.append(String.format("%-20s%10d\n", id, spawnCounter.getCounter(id)));
        }
        IO.writeString(sb.toString(), WriteStyle.MARGIN);
    }

    /**
     * Prints the current date and time of the world.
     */
    public void printDateAndTime() {
        IO.writeString(dateFormat.format(worldDate.getTime()) + " " + "(" + getDayPart() + ")", WriteStyle.MARGIN);
    }

    /**
     * Rolls the world date a given amount of seconds forward.
     */
    public void rollDate(int seconds) {
        int milliseconds = 1000 * seconds;
        worldDate.setTime(worldDate.getTime() + milliseconds);
    }

}
