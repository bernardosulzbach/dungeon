package org.dungeon.core.counters;

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
