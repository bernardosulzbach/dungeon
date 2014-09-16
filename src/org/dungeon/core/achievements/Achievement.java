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

import java.io.Serializable;

import org.dungeon.core.counters.CreatureCounter;
import org.dungeon.core.creatures.CreatureID;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;
import org.dungeon.utils.StringUtils;

/**
 * A draft of what the achievement class will look like.
 * <p/>
 * Hero will have an Achievement list (or something similar).
 *
 * @author Bernardo Sulzbach
 */
public class Achievement implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final CreatureCounter requirements;
    private final String name;
    private final String info;
    private boolean unlocked;

    public Achievement(String name, String info, CreatureID id, int amount) {
        this.name = name;
        this.info = info;
        this.requirements = new CreatureCounter(id, amount);
    }

    public Achievement(String name, String info, CreatureCounter requirements) {
        this.name = name;
        this.info = info;
        this.requirements = requirements;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    /**
     * Updates the state of the Achievement using another counter.
     * <p/>
     * If all the requirements are met, the achievement is unlocked and its name
     * and info are displayed to to player.
     *
     * @return true if the achievement was unlocked. False otherwise.
     */
    public boolean update(CreatureCounter campaignCounters) {
        if (!unlocked) {
            for (CreatureID requirement : requirements.getKeySet()) {
                if (campaignCounters.getCreatureCount(requirement) < requirements.getCreatureCount(requirement)) {
                    // The campaign counter does not match the requirement.
                    return false;
                }
            }
            // All the requirements OK.
            printAchievementUnlocked();
            setUnlocked(true);
            return true;
        }
        return false;
    }

    private void printAchievementUnlocked() {
        StringBuilder achievementMessageBuilder = new StringBuilder();
        achievementMessageBuilder.append(StringUtils.centerString(Constants.ACHIEVEMENT_UNLOCKED, '-')).append("\n");
        achievementMessageBuilder.append(StringUtils.centerString(getName())).append("\n");
        achievementMessageBuilder.append(StringUtils.centerString(getInfo())).append("\n");
        IO.writeString(achievementMessageBuilder.toString());
    }

    public String toOneLineString() {
        return name + " : " + info;
    }
}
