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

import org.dungeon.io.DLogger;

/**
 * Library class that serves as an abstract superclass to data classes that should only load from secondary storage
 * when, and if, their content is demanded.
 * <p/>
 * Created by Bernardo Sulzbach on 12/30/14.
 */
public abstract class Library {

  private boolean initialized = false;

  /**
   * Returns true if the data has already been loaded; false otherwise.
   */
  protected final boolean isInitialized() {
    return initialized;
  }

  /**
   * The method that should be invoked to call the load() method.
   * <p/>
   * If this method is called after data has been loaded, a warning is logged.
   */
  protected final void initialize() {
    if (initialized) {
      DLogger.warning("Tried to initialize an already initialized Library class.");
    } else {
      load();
      initialized = true;
    }
  }

  /**
   * Loads the data from secondary storage.
   * <p/>
   * This method is invoked by the initialize method.
   */
  abstract void load();

}
