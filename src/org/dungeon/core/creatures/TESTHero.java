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
package org.dungeon.core.creatures;

import org.dungeon.core.game.IO;
import org.dungeon.utils.Constants;
import org.dungeon.utils.StringUtils;

/**
 *
 * @author Bernardo Sulzbach
 */
public class TESTHero extends TESTCreature implements Levelling {

    private int experience;

    public TESTHero() {
    }

    @Override
    public int getExperience() {
        return experience;

    }

    private void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public int getRequiredExperience() {
        return (int) Math.pow(getLevel(), 2) * 100;
    }

    private void levelUp() {
        setLevel(getLevel() + 1);
        IO.writeString(StringUtils.centerString(Constants.LEVEL_UP, '-'));
        IO.writeString(getName() + " is now level " + getLevel() + ".");
    }

    @Override
    public void addExperience(int amount) {
        if (amount > 0) {
            setExperience(getExperience() + amount);
            if (getExperience() >= getRequiredExperience()) {
                levelUp();
            }
        } else {
            throw new IllegalArgumentException("Experience amount should be positive.");
        }
    }
}
