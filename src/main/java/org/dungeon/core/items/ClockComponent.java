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
package org.dungeon.core.items;

import org.dungeon.core.game.Game;
import org.dungeon.utils.Constants;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * The clock component.
 * <p/>
 * Created by Bernardo on 14/10/2014.
 */
public class ClockComponent implements Serializable {

    private Item master;

    /**
     * Used to store the date the clock had when it was broken.
     */
    private DateTime lastTime;

    public void setMaster(Item master) {
        this.master = master;
    }

    public void setLastTime(DateTime lastTime) {
        // Create a new Date object so that this field is not affected by changes in the rest of the program.
        this.lastTime = lastTime;
    }

    /**
     * Returns a string that represents a clock reading.
     */
    public String getTimeString() {
        if (master.isBroken()) {
            if (lastTime == null) {
                if (Game.RANDOM.nextBoolean()) {
                    return "The clock is pure junk.";
                } else {
                    return "The clock is completely smashed.";
                }
            } else {
                return "The clock is broken. Still, it displays " + Constants.TIME_FORMAT.print(lastTime) + ".";
            }
        } else {
            String timeString = Constants.TIME_FORMAT.print(Game.getGameState().getWorld().getWorldDate());
            return "The clock displays " + timeString + ".";
        }
    }

}
