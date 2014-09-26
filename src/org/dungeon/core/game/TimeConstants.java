package org.dungeon.core.game;

/**
 * The constants that determine the time flow of the game.
 * <p/>
 * Created by Bernardo on 25/09/2014.
 */
public class TimeConstants {

    // How many seconds the player needs to cross a location.
    public static final int WALK_SUCCESS = 200;
    // How many seconds the player wastes when he walks into a blocked path.
    public static final int WALK_BLOCKED = 5;
    // How many seconds the player would need to rest in order to heal from zero up to his maximum health.
    public static final int REST_COMPLETE = 36000; // 36000 seconds == 10 hours
}
