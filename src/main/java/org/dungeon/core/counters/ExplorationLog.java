package org.dungeon.core.counters;

import org.dungeon.core.game.Point;

import java.io.Serializable;
import java.util.HashMap;

/**
 * ExplorationLog class that enables tracking visited locations.
 * <p/>
 * Created by Bernardo Sulzbach on 03/11/14.
 */
public class ExplorationLog implements Serializable {

    private final HashMap<Point, ExplorationData> entries;

    // The biggest number of visits to a same location.
    private int maximumVisits;

    // The biggest number of kills in a same location.
    private int maximumKills;

    public ExplorationLog() {
        this.entries = new HashMap<Point, ExplorationData>();
    }

    public int getMaximumVisits() {
        return maximumVisits;
    }

    public int getMaximumKills() {
        return maximumKills;
    }

    private int getVisitCount(Point point) {
        return entries.containsKey(point) ? entries.get(point).getVisitCount() : 0;
    }

    public void addVisit(Point point) {
        int visitsToPoint;
        if (entries.containsKey(point)) {
            visitsToPoint = entries.get(point).addVisit();
        } else {
            entries.put(point, new ExplorationData(1, 0));
            visitsToPoint = 1;
        }
        if (visitsToPoint > maximumVisits) {
            maximumVisits = visitsToPoint;
        }
    }

    private int getKillCount(Point point) {
        return entries.containsKey(point) ? entries.get(point).getKillCount() : 0;
    }

    public void addKill(Point point) {
        // No longer checks for a not existing point as the player needs to have visited (and thus created) a given
        // point before killing anything in it.
        int killsInPoint = entries.get(point).addKill();
        if (killsInPoint > maximumKills) {
            maximumKills = killsInPoint;
        }
    }

    /**
     * @return a String representation of all the content of the log.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        ExplorationData explorationData;
        for (Point point : entries.keySet()) {
            explorationData = entries.get(point);
            builder.append(point.toString());
            builder.append(" Visits: ").append(explorationData.getVisitCount());
            builder.append(" Kills: ").append(explorationData.getKillCount());
            builder.append("\n");
        }
        return builder.toString();
    }

}
