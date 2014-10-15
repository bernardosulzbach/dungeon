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

    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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
