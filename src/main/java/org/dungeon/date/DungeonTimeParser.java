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

package org.dungeon.date;

import org.dungeon.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DungeonTimeParser {

  private DungeonTimeParser() {
    throw new AssertionError();
  }

  /**
   * Parses a period string.
   * <p/>
   * Such string should only contain pairs of positive numerical multipliers and time units separated by spaces,
   * "and", and commas.
   * <p/>
   * For example: "2 years, 5 months, 8 days, and 20 hours".
   *
   * @param string a period string, not null
   * @return a Period, not null
   */
  @NotNull
  public static Period parsePeriod(@NotNull String string) {
    List<String> tokens = cleanAndTokenize(string);
    if (tokens.size() < 2) {
      throw new IllegalArgumentException("string should provide at least one multiplier-unit pair.");
    }
    long milliseconds = 0;
    // Alternatively get a multiplier and a unit.
    Long multiplier = null;
    DungeonTimeUnit unit = null;
    for (String token : tokens) {
      if (multiplier == null) {
        try {
          multiplier = Long.parseLong(token);
          if (multiplier <= 0) {
            throw new InvalidMultiplierException("nonpositive multipliers are not allowed.");
          }
        } catch (NumberFormatException bad) {
          throw new InvalidMultiplierException(token + " is not a valid multiplier.");
        }
      } else {
        unit = parseDungeonTimeUnit(token);
      }
      if (unit != null) {
        milliseconds += multiplier * unit.milliseconds;
        multiplier = null;
        unit = null;
      }
    }
    return new Period(milliseconds);
  }

  @NotNull
  private static DungeonTimeUnit parseDungeonTimeUnit(final String token) {
    String cleanToken;
    if (token.endsWith("s")) {
      cleanToken = token.substring(0, token.length() - 1);
    } else {
      cleanToken = token;
    }
    try {
      return DungeonTimeUnit.valueOf(cleanToken.toUpperCase());
    } catch (IllegalArgumentException bad) {
      throw new InvalidUnitException(token + " is not a valid unit.");
    }
  }

  @NotNull
  private static List<String> cleanAndTokenize(String string) {
    string = string.replaceAll("and", "");
    string = string.replaceAll(",", "");
    return new ArrayList<String>(Arrays.asList(Utils.split(string)));
  }

  public static class InvalidMultiplierException extends IllegalArgumentException {
    public InvalidMultiplierException(@NotNull String s) {
      super(s);
    }
  }

  public static class InvalidUnitException extends IllegalArgumentException {
    public InvalidUnitException(@NotNull String s) {
      super(s);
    }
  }

}
