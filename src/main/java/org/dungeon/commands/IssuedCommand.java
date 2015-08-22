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

import org.dungeon.util.Utils;

import org.jetbrains.annotations.NotNull;

/**
 * IssuedCommand class that processes a command entered by the player and provides useful query methods.
 *
 * <p>IssuedCommands are case-unsensitive and granted to have at least one word (the command).
 */
public final class IssuedCommand {

  private final String stringRepresentation;
  private final String[] tokens;

  /**
   * Creates a new IssuedCommand from a string.
   *
   * @param source a string with at least one character that is not whitespace.
   */
  public IssuedCommand(@NotNull String source) {
    tokens = Utils.split(source);
    if (tokens.length == 0) {
      throw new IllegalArgumentException("invalid source, no tokens obtained.");
    }
    this.stringRepresentation = Utils.join(" ", tokens);
  }

  public String getStringRepresentation() {
    return stringRepresentation;
  }

  public String[] getTokens() {
    return tokens;
  }

  @Override
  public String toString() {
    return getStringRepresentation();
  }

}
