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
