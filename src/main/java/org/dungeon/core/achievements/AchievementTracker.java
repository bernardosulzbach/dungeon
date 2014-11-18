package org.dungeon.core.achievements;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * AchievementTracker that tracks the unlocked achievements.
 * <p/>
 * Created by Bernardo Sulzbach on 16/11/14.
 */
public class AchievementTracker implements Serializable {

    private final ArrayList<String> unlockedAchievements;

    public AchievementTracker() {
        this.unlockedAchievements = new ArrayList<String>();
    }

    public int getUnlockedCount() {
        return unlockedAchievements.size();
    }

    public void setUnlocked(Achievement achievement) {
        unlockedAchievements.add(achievement.getId());
    }

    public boolean isUnlocked(Achievement achievement) {
        return unlockedAchievements.contains(achievement.getId());
    }

}
