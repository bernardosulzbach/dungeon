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

package org.dungeon.game;

/**
 * The constants that determine the time flow of the game.
 * <p/>
 * Created by Bernardo on 25/09/2014.
 */
public class TimeConstants {

  // How many seconds the player needs to cross a location.
  public static final int WALK_SUCCESS = 200;
  public static final int WALK_BLOCKED = 2;

  /**
   * The amount of seconds the Hero needs to rest or sleep to heal 10 percent of his health.
   */
  public static final int HEAL_TEN_PERCENT = 3600;

  public static final int BATTLE_TURN_DURATION = 30;

}
