package org.dungeon.core.achievements;

import org.dungeon.core.counters.ExplorationLog;
import org.dungeon.core.creatures.Hero;

/**
 * A sketch of exploration achievements. Currently, it only supports kill and visit counting.
 * <p/>
 * Created by Bernardo Sulzbach on 03/11/14.
 */
public class ExplorationAchievement extends Achievement {

    private int killCount;
    private int visitCount;

    public ExplorationAchievement(String id,
                                  String name,
                                  String info,
                                  int visitCount,
                                  int killCount) {
        super(id, name, info);
        this.killCount = killCount;
        this.visitCount = visitCount;
    }

    @Override
    public boolean isFulfilled(Hero hero) {
        ExplorationLog explorationLog = hero.getExplorationLog();
        return killCount <= explorationLog.getMaximumKills() && visitCount <= explorationLog.getMaximumVisits();
    }

}
