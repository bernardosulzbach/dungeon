package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.entity.Entity;
import org.mafagafogigante.dungeon.game.Name;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * General utility class.
 */
public final class Utils {

  private Utils() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Enumerates the elements of a Collection in a human-readable way.
   *
   * <p>This method calls {@code toString()} on each object, so the result depends on what that method returns.
   *
   * @param collection a Collection
   * @return a String
   */
  public static String enumerate(@NotNull final Collection<?> collection) {
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<?> iterator = collection.iterator();
    for (int i = 0; i < collection.size(); i++) {
      stringBuilder.append(iterator.next().toString());
      if (i < collection.size() - 2) {
        stringBuilder.append(", ");
      } else if (i == collection.size() - 2) {
        if (collection.size() >= 3) {
          // A serial comma (only used when we have three or more items).
          stringBuilder.append(",");
        }
        stringBuilder.append(" and ");
      }
    }
    return stringBuilder.toString();
  }

  /**
   * Returns a String representation of the enumeration of all the Entities in a given List.
   */
  public static String enumerateEntities(final List<? extends Entity> listOfEntities) {
    CounterMap<Name> nameOccurrences = new CounterMap<>();
    for (Entity entity : listOfEntities) {
      nameOccurrences.incrementCounter(entity.getName());
    }
    ArrayList<String> quantifiedNames = new ArrayList<>();
    for (Name name : nameOccurrences.keySet()) {
      quantifiedNames.add(name.getQuantifiedName(nameOccurrences.getCounter(name)));
    }
    return enumerate(quantifiedNames);
  }

  /**
   * Given a duration in milliseconds, this method returns a human-readable period string.
   *
   * @param duration a duration in milliseconds, nonnegative
   * @return a String
   */
  public static String makePeriodString(long duration) {
    if (duration < 0) {
      throw new IllegalArgumentException("duration should be nonnegative.");
    }
    Period period = new Period(duration).normalizedStandard();
    period = withMostSignificantNonZeroFieldsOnly(period, 1);
    return PeriodFormat.wordBased(Locale.ENGLISH).print(period);
  }

  private static Period withMostSignificantNonZeroFieldsOnly(Period period, int fields) {
    int nonZeroFieldsFound = 0;
    for (DurationFieldType durationFieldType : period.getFieldTypes()) {
      if (period.get(durationFieldType) != 0) {
        if (nonZeroFieldsFound < fields) {
          nonZeroFieldsFound++;
        } else {
          period = period.withField(durationFieldType, 0);
        }
      }
    }
    return period;
  }

}
