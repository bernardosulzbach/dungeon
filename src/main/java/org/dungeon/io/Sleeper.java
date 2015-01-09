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

package org.dungeon.io;

/**
 * Sleeper class that handles Thread sleeping.
 * <p/>
 * This class should not be instantiated.
 * <p/>
 * Created by Bernardo Sulzbach on 09/01/15.
 */
final public class Sleeper {

  private Sleeper() {
  }

  /**
   * Sleeps for a specified amount of milliseconds.
   *
   * @param milliseconds how many milliseconds should this thread sleep for.
   */
  public static void sleep(int milliseconds) {
    if (milliseconds > 0) {
      try {
        Thread.sleep(milliseconds);
      } catch (InterruptedException logged) {
        DLogger.warning("Sleeper was interrupted!");
      }
    } else {
      DLogger.warning("Tried to sleep a nonpositive amount of milliseconds!");
    }
  }

}
