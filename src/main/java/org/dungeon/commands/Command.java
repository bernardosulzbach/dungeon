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

package org.dungeon.commands;

import org.jetbrains.annotations.NotNull;

/**
 * Command abstract class that defines a type for command objects.
 *
 * <p>Make anonymous classes from this one to define commands.
 */
public abstract class Command {

  private final CommandDescription description;

  /**
   * Creates a new Command object with the provided name and info.
   *
   * @param name a String for name, lowercase
   * @param info a String for info
   */
  public Command(@NotNull String name, @NotNull String info) {
    description = new CommandDescription(name, info);
  }

  public CommandDescription getDescription() {
    return description;
  }

  /**
   * Indicates whether a Command is "equal to" this one.
   *
   * <p>Two Commands are considered to be equal when their names are equal.
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Command command = (Command) object;
    return description.getName().equals(command.description.getName());
  }

  @Override
  public int hashCode() {
    return description.getName().hashCode();
  }

  /**
   * Executes this Command, given an IssuedCommand object.
   */
  public abstract void execute(@NotNull String[] arguments);

  @Override
  public String toString() {
    return description.toString();
  }

}
