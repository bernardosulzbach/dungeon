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

import org.dungeon.io.DungeonLogger;
import org.dungeon.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * IssuedCommand class that processes a command entered by the player and provides useful query methods.
 *
 * <p>IssuedCommands are case-unsensitive and granted to have at least one word (the command).
 */
public final class IssuedCommand {

  private final String stringRepresentation;
  private final String command;
  private final List<String> arguments = new ArrayList<String>();

  /**
   * Creates a new IssuedCommand from a string.
   *
   * @param source a string with at least one character that is not whitespace.
   */
  public IssuedCommand(@NotNull String source) {
    String[] tokens = Utils.split(source);
    if (tokens.length == 0) {
      throw new IllegalArgumentException("invalid source, no tokens obtained.");
    }
    this.command = tokens[0];
    this.arguments.addAll(Arrays.asList(tokens).subList(1, tokens.length));
    this.stringRepresentation = Utils.join(" ", tokens);
  }

  public String getFirstToken() {
    return command;
  }

  /**
   * Returns true if there is one or more arguments.
   */
  public boolean hasArguments() {
    return !arguments.isEmpty();
  }

  /**
   * Returns an array with all arguments.
   */
  public String[] getArguments() {
    return arguments.toArray(new String[arguments.size()]);
  }

  public String getFirstArgument() {
    return arguments.get(0);
  }

  /**
   * Checks if the first argument of this IssuedCommand is case-insensitively equal to a given String.
   *
   * @param string the String used for comparison
   * @return true if the Strings are case-insensitively equal, false otherwise
   */
  public boolean firstArgumentEquals(String string) {
    if (hasArguments()) {
      return getFirstArgument().equalsIgnoreCase(string);
    } else {
      DungeonLogger.warning("Called firstArgumentEquals on an IssuedCommand that does not have arguments.");
      return false;
    }
  }

  public int getTokenCount() {
    return 1 + arguments.size();
  }

  public String getStringRepresentation() {
    return stringRepresentation;
  }

  @Override
  public String toString() {
    return getStringRepresentation();
  }

}
