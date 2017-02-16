package org.mafagafogigante.dungeon.date;

import org.mafagafogigante.dungeon.util.NonNegativeInteger;
import org.mafagafogigante.dungeon.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A builder of time strings. A time string is a human-readable textual representation of a period, e.g.: 2 years and 6
 * months.
 */
class TimeStringBuilder {

  private final Map<DungeonTimeUnit, NonNegativeInteger> map;

  TimeStringBuilder() {
    map = new EnumMap<>(DungeonTimeUnit.class);
  }

  public void set(@NotNull DungeonTimeUnit unit, @NotNull Integer value) {
    map.put(unit, new NonNegativeInteger(value));
  }

  /**
   * Produces a String using only the specified most significant non-zero fields. If there is nothing to build the
   * String with, "Less than a second" is returned.
   *
   * @param fields how many fields to use
   * @return a time string
   */
  private String toString(final int fields) {
    List<String> strings = new ArrayList<>();
    // Enum maps are maintained in the natural order of their keys (the order of declaration of the enum constants).
    for (Entry<DungeonTimeUnit, NonNegativeInteger> entry : map.entrySet()) {
      if (strings.size() < fields) {
        int value = entry.getValue().toInteger();
        if (value > 0) {
          String valueString = value + " " + entry.getKey().toString().toLowerCase(Locale.ENGLISH);
          if (value > 1) {
            valueString += "s";
          }
          strings.add(valueString);
        }
      }
    }
    if (strings.isEmpty()) {
      return "less than a second";
    } else {
      Collections.reverse(strings);
      return Utils.enumerate(strings);
    }
  }

  @Override
  public String toString() {
    return toString(DungeonTimeUnit.values().length);
  }

}
