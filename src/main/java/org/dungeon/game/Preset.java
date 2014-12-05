package org.dungeon.game;

/**
 * Abstract Preset class that defines common methods for all presets.
 *
 * Created by Bernardo Sulzbach on 03/12/14.
 */
public abstract class Preset {

    private boolean finished;

    void lock() {
        finished = true;
    }

    boolean isLocked() {
        return finished;
    }

}
