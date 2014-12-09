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
package org.dungeon.achievements;

import org.dungeon.game.Game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * AchievementTracker that tracks the unlocked achievements.
 * <p/>
 * Created by Bernardo Sulzbach on 16/11/14.
 */
public class AchievementTracker implements Serializable {

    private final ArrayList<UnlockedAchievement> unlockedAchievements;

    public AchievementTracker() {
        this.unlockedAchievements = new ArrayList<UnlockedAchievement>();
    }

    public int getUnlockedCount() {
        return unlockedAchievements.size();
    }

    public void unlock(Achievement achievement) {
        unlockedAchievements.add(new UnlockedAchievement(achievement.getId(),
                Game.getGameState().getWorld().getWorldDate()));
    }

    /**
     * Return the UnlockedAchievement object that corresponds to a specific Achievement.
     *
     * @param achievement an Achievement object.
     * @return the UnlockedAchievement that corresponds to this Achievement.
     */
    public UnlockedAchievement getUnlockedAchievement(Achievement achievement) {
        String id = achievement.getId();
        for (UnlockedAchievement ua : unlockedAchievements) {
            if (ua.id.equals(id)) {
                return ua;
            }
        }
        return null;
    }

    /**
     * Convenience method that return if a given Achievement is unlocked in this AchievementTracker.
     * <p/>
     * Alternatively, the developer can compare the return of getUnlockedAchievement(Achievement) to null.
     *
     * @param achievement an Achievement object.
     * @return true if this Achievement is unlocked, false otherwise.
     */
    public boolean isUnlocked(Achievement achievement) {
        return getUnlockedAchievement(achievement) != null;
    }

}
