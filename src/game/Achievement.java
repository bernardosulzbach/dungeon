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
package game;

import java.io.Serializable;
import utils.Constants;
import utils.StringUtils;

/**
 * A draft of what the achievement class will look like.
 *
 * Hero will have an Achievement list (or something similar).
 *
 * @author Bernardo Sulzbach
 */
public class Achievement implements Serializable {

    private final BattleCounter requirements;
    private final String name;
    private final String info;
    private boolean unlocked;

    public Achievement(String name, String info, BattleCounter requirements) {
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
     *
     * If all the requirements are met, the achievement is unlocked and its name and info are displayed to to player.
     */
    public void update(BattleCounter battleCounter) {
        if (!unlocked) {
            for (CreatureID id : requirements.getCounters().keySet()) {
                Integer battleCount = battleCounter.getCounters().get(id);
                if (battleCount != null) {
                    if (battleCount >= requirements.getCounters().get(id)) {
                        printAchievementUnlocked();
                        setUnlocked(true);
                    }
                }
            }

        }
    }

    private void printAchievementUnlocked() {
        StringBuilder achievementMessageBuilder = new StringBuilder();
        achievementMessageBuilder.append(StringUtils.centerString(Constants.ACHIEVEMENT_UNLOCKED, '-')).append("\n");
        achievementMessageBuilder.append(StringUtils.centerString(getName())).append("\n");
        achievementMessageBuilder.append(StringUtils.centerString(getInfo())).append("\n");
        IO.writeString(achievementMessageBuilder.toString());
    }
}
