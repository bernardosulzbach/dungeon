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

import org.dungeon.creatures.Hero;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Utils;

import java.io.Serializable;

/**
 * Achievement class that defines achievements.
 */
public abstract class Achievement implements Serializable {

    final String id;
    private final String name;
    private final String info;

    Achievement(String id, String name, String info) {
        this.id = id;
        this.name = name;
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    /**
     * Updates the state of the Achievement.
     *
     * @return true if the achievement was unlocked. False otherwise.
     */
    public final boolean update(Hero hero) {
        if (!hero.getAchievementTracker().isUnlocked(this) && isFulfilled(hero)) {
            // All the requirements OK, unlock the achievement.
            printAchievementUnlocked();
            hero.getAchievementTracker().setUnlocked(this);
            return true;
        } else {
            return false;
        }

    }

    // Checks if the given Hero fulfills the requirements of this achievement.
    abstract boolean isFulfilled(Hero hero);

    /**
     * Outputs an achievement unlocked message with some information about the unlocked achievement.
     */
    void printAchievementUnlocked() {
        // Initial capacity:
        // First line:                    precisely 100 characters
        // Name, info and three newlines: about 60 characters (using 100 to prevent buffer overflow).
        // How many experience points:    25 characters for a experience reward of three digits.
        // Total:                         100 + 100 + 25 = 225
        StringBuilder sb = new StringBuilder(225);
        sb.append(Utils.centerString(Constants.ACHIEVEMENT_UNLOCKED, '-')).append("\n");
        sb.append(getName()).append("\n");
        sb.append(getInfo()).append("\n");
        // Keep the StringBuilder as, in the future, Honor, Karma or something else may be rewarded by the achievements.
        IO.writeString(sb.toString());
    }

}
