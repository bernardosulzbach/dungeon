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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The clock component.
 * <p/>
 * Created by Bernardo on 14/10/2014.
 */
public class ClockComponent implements Serializable {

    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Item master;

    /** Used to store the date the clock had when it was broken. */
    private Date lastTime;

    public ClockComponent() {
    }

    public ClockComponent(ClockComponent model) {
        this.lastTime = model.lastTime;
    }

    public void setMaster(Item master) {
        this.master = master;
    }

    public void setLastTime(Date lastTime) {
        // Create a new Date object so that this field is not affected by changes in the rest of the program.
        this.lastTime = new Date(lastTime.getTime());
    }

    /**
     * Provided a Date object, this method returns the
     */
    public String getTimeString(long time) {
        if (master.isBroken()) {
            if (lastTime == null) {
                return "The clock is pure junk.";
            } else {
                return "The clock is broken. It displays " + dateTimeFormat.format(lastTime) + ".";
            }
        } else {
            return "The clock displays " + dateTimeFormat.format(time) + ".";
        }
    }

}
