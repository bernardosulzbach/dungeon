package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.date.Duration;
import org.mafagafogigante.dungeon.game.Random;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.CircularList;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * An object that represents the weather of the world.
 */
public class Weather implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  // Eight hours.
  private static final Duration UPDATE_INTERVAL = new Duration(new Date(1, 1, 1, 1, 1, 1), new Date(1, 1, 1, 9, 1, 1));

  // The probability of a trend (weather getting lighter or heavier) being followed.
  private static final double TREND_FORCE = 0.6;

  private final CircularList<WeatherCondition> conditionHistory = new CircularList<>(2);
  private Date lastWeatherUpdate;

  /**
   * Constructs a new Weather starting at the specified date.
   */
  public Weather(@NotNull Date date) {
    this.lastWeatherUpdate = date;
    rollNewCondition(date);
  }

  public WeatherCondition getCurrentCondition(Date date) {
    refresh(date);
    return conditionHistory.get(0);
  }

  /**
   * Refreshes the current condition if enough time has passed. If not enough time has passed, a call is a no-op.
   */
  private void refresh(@NotNull Date date) {
    if (new Duration(lastWeatherUpdate, date).compareTo(UPDATE_INTERVAL) >= 0) {
      rollNewCondition(date);
    }
  }

  /**
   * Randomly selects a new WeatherCondition and adds it to the condition history.
   */
  private void rollNewCondition(@NotNull Date date) {
    if (conditionHistory.isEmpty()) {
      conditionHistory.add(Random.select(Arrays.asList(WeatherCondition.values())));
    } else {
      final WeatherCondition lastCondition = conditionHistory.get(0);
      if (conditionHistory.size() == 1) {
        final List<WeatherCondition> list = Arrays.asList(lastCondition.getLighter(), lastCondition.getHeavier());
        conditionHistory.add(Random.select(list));
      } else { // WeatherCondition history has at least two conditions.
        final WeatherCondition semiLastCondition = conditionHistory.get(1);
        // Try to force change in the same way so that extremes are more common.
        if (semiLastCondition.isLighterThan(lastCondition)) {
          conditionHistory.add(Random.roll(TREND_FORCE) ? lastCondition.getHeavier() : lastCondition.getLighter());
        } else if (semiLastCondition.isHeavierThan(lastCondition)) {
          conditionHistory.add(Random.roll(TREND_FORCE) ? lastCondition.getLighter() : lastCondition.getHeavier());
        } else { // The condition did not change, ensure change.
          // If an extreme condition follows the trend, it doesn't change, so conditions may not change.
          if (lastCondition.getLighter() == lastCondition) {
            conditionHistory.add(lastCondition.getHeavier());
          } else {
            conditionHistory.add(lastCondition.getLighter());
          }
        }
      }
    }
    lastWeatherUpdate = date;
  }

}
