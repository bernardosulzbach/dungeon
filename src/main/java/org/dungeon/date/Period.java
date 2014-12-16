package org.dungeon.date;

/**
 * Period class to calculate, store and print date differences.
 *
 * Created by Bernardo Sulzbach on 16/12/14.
 */
public class Period {

  private static final String LESS_THAN_A_DAY = "Less than a day";

  // Use a Date as it already provides convenient getters.
  private final Date difference;

  /**
   * Constructs a period from a specified start date to an end date.
   * @param start the start of the period.
   * @param end the end of the period.
   */
  public Period(Date start, Date end) {
    difference = new Date(end.getTime() - start.getTime());
  }

  public long getSeconds() {
    return difference.getTime() / 1000;
  }

  @Override
  public String toString() {
    long years = difference.getYear();
    long months = difference.getMonth();
    long days = difference.getDay();
    StringBuilder builder = new StringBuilder();
    if (years != 0) {
      if (years == 1) {
        builder.append(years).append(" year");
      } else {
        builder.append(years).append(" years");
      }
    }
    if (months != 0) {
      if (builder.length() != 0) {
        if (days == 0) {
          builder.append(" and ");
        } else {
          builder.append(", ");
        }
      }
      if (months == 1) {
        builder.append(months).append(" month");
      } else {
        builder.append(months).append(" months");
      }
    }
    if (days != 0) {
      if (builder.length() != 0) {
        builder.append(" and ");
      }
      if (days == 1) {
        builder.append(days).append(" day");
      } else {
        builder.append(days).append(" days");
      }
    }
    if (builder.length() == 0) {
      builder.append(LESS_THAN_A_DAY);
    }
    return builder.toString();
  }

}
