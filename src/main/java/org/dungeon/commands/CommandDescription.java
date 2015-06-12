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

package org.dungeon.commands;

/**
 * Simple wrapper for a name and info of a Command.
 */
public class CommandDescription {

  private final String name;
  private final String info;

  public CommandDescription(String name, String info) {
    if (name == null) {
      throw new IllegalArgumentException("Cannot create CommandDescription with null name.");
    }
    this.name = name;
    this.info = info;
  }

  public String getName() {
    return name;
  }

  public String getInfo() {
    return info;
  }

  @Override
  public String toString() {
    return getName() + " : " + getInfo();
  }

}
