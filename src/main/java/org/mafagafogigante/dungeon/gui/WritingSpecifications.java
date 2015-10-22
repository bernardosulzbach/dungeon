/*
 * Copyright (C) 2015 Bernardo Sulzbach
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

package org.mafagafogigante.dungeon.gui;

import org.mafagafogigante.dungeon.util.NonNegativeInteger;

/**
 * A series of specifications for a text pane write.
 */
public class WritingSpecifications {

  private final boolean scrollDown;
  private final NonNegativeInteger wait;

  /**
   * Constructs a new WritingSpecifications object.
   *
   * @param scrollDown if the pane should scroll down
   * @param wait how many milliseconds the application should wait before returning, nonnegative
   */
  public WritingSpecifications(boolean scrollDown, int wait) {
    this.scrollDown = scrollDown;
    this.wait = new NonNegativeInteger(wait);
  }

  public boolean shouldScrollDown() {
    return scrollDown;
  }

  public boolean shouldWait() {
    return getWait() != 0;
  }

  public int getWait() {
    return wait.toInteger();
  }

  @Override
  public String toString() {
    return "WritingSpecifications{" +
        "scrollDown=" + scrollDown +
        '}';
  }

}
