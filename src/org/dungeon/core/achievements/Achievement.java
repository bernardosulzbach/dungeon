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
package org.dungeon.core.achievements;

import org.dungeon.core.creatures.Hero;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;
import org.dungeon.utils.StringUtils;

import java.io.Serializable;

/**
 * Achievement class that defines achievements.
 *
 * @author Bernardo Sulzbach
 */
public abstract class Achievement implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final String info;
    private final int experienceReward;
    private boolean unlocked;

    public Achievement(String name, String info, int experienceReward) {
        this.name = name;
        this.info = info;
        if (experienceReward < 0) {
            throw new IllegalArgumentException("experienceReward should be nonnegative.");
        }
        this.experienceReward = experienceReward;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    /**
     * Updates the state of the Achievement.
     *
     * @return true if the achievement was unlocked. False otherwise.
     */
    public abstract boolean update(Hero hero);

    /**
     * Outputs an achievement unlocked message with some information about the unlocked achievement.
     */
    public void printAchievementUnlocked() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.centerString(Constants.ACHIEVEMENT_UNLOCKED, '-')).append("\n");
        sb.append(Constants.MARGIN).append(getName()).append("\n");
        sb.append(Constants.MARGIN).append(getInfo()).append("\n");
        if (getExperienceReward() != 0) {
            sb.append(Constants.MARGIN).append(String.format("+ %d Experience Points", getExperienceReward()));
        }
        IO.writeString(sb.toString());
    }

    public String toOneLineString() {
        return name + " : " + info;
    }
}
