/*
 * Copyright (C) 2016 Bernardo Sulzbach
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

package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.date.DungeonTimeUnit;
import org.mafagafogigante.dungeon.date.Duration;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class WeatherTest {

  @Test
  public void testWeatherShouldPresentAllConditionsOverTheCourseOfAYear() {
    // Note that this test may fail because of probabilistic issues.
    // However, that would indicate an issue with conditions not varying enough, which would be a bug.
    Date date = new Date(10, 1, 1);
    final Date end = date.plus(1, DungeonTimeUnit.YEAR);
    Set<Condition> conditionSet = new HashSet<>();
    Weather weather = new Weather(date);
    while (date.compareTo(end) < 0) { // Check until a full year passes.
      conditionSet.add(weather.getCurrentCondition(date));
      date = date.plus(1, DungeonTimeUnit.HOUR); // Check the condition hourly.
    }
    Assert.assertThat(conditionSet, CoreMatchers.hasItems(Condition.values()));
  }

  @Test
  public void testWeatherConditionShouldChangeAtLeastOncePerMonth() {
    Date date = new Date(10, 1, 1);
    Date lastConditionChange = date;
    final Date end = date.plus(10, DungeonTimeUnit.YEAR); // Test over 10 years.
    final Duration oneMonth = new Duration(new Date(1, 1, 1), new Date(1, 2, 1));
    Weather weather = new Weather(date);
    Condition lastCondition = null;
    while (date.compareTo(end) < 0) { // Check until the end date.
      if (lastCondition != weather.getCurrentCondition(date)) {
        lastCondition = weather.getCurrentCondition(date);
        lastConditionChange = date;
      }
      Assert.assertTrue(new Duration(lastConditionChange, date).compareTo(oneMonth) < 0);
      date = date.plus(1, DungeonTimeUnit.HOUR); // Check the condition hourly.
    }
  }

}