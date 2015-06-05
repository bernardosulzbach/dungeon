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
import org.dungeon.util.Utils;

import java.util.Arrays;

/**
 * IssuedCommand class that wraps a String entered by the user and provides powerful query methods.
 * <p/>
 * An IssuedCommand is made up of at least one token (word) and is not case-sensitive.
 */
public final class IssuedCommand {

  private final String stringRepresentation;
  private final String[] tokens;

  public IssuedCommand(String source) {
    this.tokens = Utils.split(source);
    this.stringRepresentation = Utils.join(" ", tokens);
    if (tokens.length == 0) {
      throw new IllegalArgumentException("source must contain at least one token.");
    }
  }

  public String getStringRepresentation() {
    return stringRepresentation;
  }

  public String getFirstToken() {
    return tokens[0];
  }

  /**
   * @return true if there are at least two tokens, false otherwise.
   */
  public boolean hasArguments() {
    return tokens.length > 1;
  }

  public String getFirstArgument() {
    return tokens[1];
  }

  /**
   * Checks if the first argument of this IssuedCommand is case-insensitively equal to a given String.
   *
   * @param string the String used for comparison.
   * @return true if the Strings are case-insensitively equal, false otherwise.
   */
  public boolean firstArgumentEquals(String string) {
    if (hasArguments()) {
      return tokens[1].equalsIgnoreCase(string);
    } else {
      DLogger.warning("Called firstArgumentEquals on an IssuedCommand that does not have arguments.");
      return false;
    }
  }

  /**
   * @return an array with all tokens but the first.
   */
  public String[] getArguments() {
    return Arrays.copyOfRange(tokens, 1, tokens.length);
  }

  public int getTokenCount() {
    return tokens.length;
  }

}
