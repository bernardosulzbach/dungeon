package org.mafagafogigante.dungeon.date;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class DungeonTimeParser {

  private DungeonTimeParser() {
    throw new AssertionError();
  }

  /**
   * Parses a duration string.
   *
   * <p>Such string should only contain pairs of positive numerical multipliers and time units separated by spaces,
   * "and", and commas.
   *
   * <p>For example: "2 years, 5 months, 8 days, and 20 hours".
   */
  @NotNull
  public static Duration parseDuration(@NotNull String string) {
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
    return new Duration(milliseconds);
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
      return DungeonTimeUnit.valueOf(cleanToken.toUpperCase(Locale.ENGLISH));
    } catch (IllegalArgumentException bad) {
      throw new InvalidUnitException(token + " is not a valid unit.");
    }
  }

  @NotNull
  private static List<String> cleanAndTokenize(String string) {
    string = string.replaceAll("and", "");
    string = string.replaceAll(",", "");
    return new ArrayList<>(Arrays.asList(StringUtils.split(string)));
  }

  public static class InvalidMultiplierException extends IllegalArgumentException {
    InvalidMultiplierException(@NotNull String string) {
      super(string);
    }
  }

  public static class InvalidUnitException extends IllegalArgumentException {
    InvalidUnitException(@NotNull String string) {
      super(string);
    }
  }

}
