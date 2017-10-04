package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.date.DungeonTimeUnit;
import org.mafagafogigante.dungeon.date.Duration;
import org.mafagafogigante.dungeon.entity.Luminosity;
import org.mafagafogigante.dungeon.util.Percentage;
import org.mafagafogigante.dungeon.util.Selectable;

import org.jetbrains.annotations.NotNull;

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
  private final Luminosity luminosity;

  private int startingHour;

  /**
   * Creates a new PartOfDay.
   *
   * @param name the name of this PartOfDay
   * @param luminosity the luminosity of this PartOfDay, a double between 0 and 1
   * @param startingHour the starting hour, a nonnegative integer smaller than 24
   */
  PartOfDay(String name, double luminosity, int startingHour) {
    this.name = NameFactory.newInstance(name);
    this.luminosity = new Luminosity(new Percentage(luminosity));
    setStartingHour(startingHour);
  }

  /**
   * Returns the PartOfDay constant corresponding to a given time.
   *
   * @param date a Date object.
   * @return a PartOfDay constant.
   */
  @NotNull
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
    throw new AssertionError(); // Execution should never reach this line.
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
    return (int) new Duration(cur, day).getSeconds();
  }

  public Luminosity getLuminosity() {
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
