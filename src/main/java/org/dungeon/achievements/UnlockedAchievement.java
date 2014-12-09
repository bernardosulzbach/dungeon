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

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * UnlockedAchievement class that records the unlocking of an achievement.
 * <p/>
 * Created by Bernardo Sulzbach on 09/12/14.
 */
public final class UnlockedAchievement implements Serializable {

    public final String id;
    // DateTime is immutable, therefore it can be public.
    public final DateTime date;

    /**
     * Construct a new UnlockedAchievement.
     *
     * @param id   the id of the unlocked achievement.
     * @param date the date when the achievement was unlocked.
     */
    public UnlockedAchievement(String id, DateTime date) {
        this.id = id;
        this.date = date;
    }

}
