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

import org.dungeon.date.Date;
import org.dungeon.date.DungeonTimeUnit;
import org.dungeon.date.Period;
import org.dungeon.util.Percentage;
import org.dungeon.util.Selectable;

/**
 * Enumerated type of the parts of the day.
 */
public enum PartOfDay implements Selectable {

  // Keep this array sorted by the startingHour in ascending order. See getCorrespondingConstant to understand why.
  NIGHT("Night", 0.4, 1),
  DAWN("Dawn", 0.6, 5),
  MORNING("Morning", 0.8, 7),
  NOON("Noon", 1.0, 11),
  AFTERNOON("Afternoon", 0.8, 13),
  DUSK("Dusk", 0.6, 17),
  EVENING("Evening", 0.4, 19),
  MIDNIGHT("Midnight", 0.2, 23);

  private final Name name;

  // How bright is the sun at this part of the day?
  // Should be bigger than or equal to 0 and smaller than or equal to 1.
  private final Percentage luminosity;

  private int startingHour;

  /**
   * Creates a new PartOfDay.
   *
   * @param name         the name of this PartOfDay
   * @param luminosity   the luminosity of this PartOfDay, a double between 0 and 1
   * @param startingHour the starting hour, a nonnegative integer smaller than 24
   */
  PartOfDay(String name, double luminosity, int startingHour) {
    this.name = Name.newInstance(name);
    this.luminosity = new Percentage(luminosity);
    setStartingHour(startingHour);
  }

  /**
   * Returns the PartOfDay constant corresponding to a given time.
   *
   * @param date a Date object.
   * @return a PartOfDay constant.
   */
  public static PartOfDay getCorrespondingConstant(Date date) {
    long hour = date.getHour();
    // MIDNIGHT starts at 23, therefore 0 does not satisfy the comparison and the following statement is necessary.
    if (hour == 0) {
      // It is also possible to add 24 to hour if it is zero, making it bigger than 23, but this is simpler.
      return MIDNIGHT;
    }
    PartOfDay[] podArray = values();
    // Note that the array is sorted in ascending startingHour order, therefore we iterate backwards.
    for (int i = podArray.length - 1; i >= 0; i--) {
      if (podArray[i].getStartingHour() <= hour) {
        return podArray[i];
      }
    }
    return null;
  }

  /**
   * @param cur the current time.
   * @param pod a part of the day.
   * @return the number of seconds between the current time and the start of the part of the day.
   */
  public static int getSecondsToNext(Date cur, PartOfDay pod) {
    // The day on which the next part of day will happen.
    Date day = cur.getHour() < pod.getStartingHour() ? cur : cur.plus(1, DungeonTimeUnit.DAY);
    day = new Date(day.getYear(), day.getMonth(), day.getDay(), pod.getStartingHour(), 0, 0);
    return (int) new Period(cur, day).getSeconds();
  }

  public Percentage getLuminosity() {
    return luminosity;
  }

  int getStartingHour() {
    return startingHour;
  }

  void setStartingHour(int startingHour) {
    if (startingHour < 0 || startingHour > 23) {
      throw new IllegalArgumentException("startingHour must be in the range [0, 23]");
    }
    this.startingHour = startingHour;
  }

  @Override
  public Name getName() {
    // Whenever the player sees a PartOfDay, he or she sees the return value of toString().
    // Therefore it makes sense to delegate the name of a PartOfDay to the toString() method.
    return name;
  }

  @Override
  public String toString() {
    return name.toString();
  }

}
