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

/**
 * Command abstract class that defines a type for command objects.
 * <p/>
 * Make anonymous classes from this one to define commands.
 */
public abstract class Command {

  private final CommandDescription description;

  public Command(String name) {
    this(name, null);
  }

  public Command(String name, String info) {
    description = new CommandDescription(name, info);
  }

  public CommandDescription getDescription() {
    return description;
  }

  /**
   * Executes this Command, given an IssuedCommand object. Returns a CommandResult object or null.
   * If the duration of the execution is zero and if the game state is not changed, this method may return null.
   *
   * @return a CommandResult object or null
   */
  public abstract CommandResult execute(IssuedCommand issuedCommand);

}
