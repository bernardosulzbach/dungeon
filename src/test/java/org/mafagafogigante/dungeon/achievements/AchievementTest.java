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

package org.mafagafogigante.dungeon.achievements;

import org.mafagafogigante.dungeon.entity.creatures.Creature;
import org.mafagafogigante.dungeon.entity.creatures.CreaturePreset;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.PartOfDay;
import org.mafagafogigante.dungeon.game.Point;
import org.mafagafogigante.dungeon.stats.CauseOfDeath;
import org.mafagafogigante.dungeon.stats.Statistics;
import org.mafagafogigante.dungeon.util.CounterMap;

import org.junit.Assert;
import org.junit.Test;

public class AchievementTest {

  @Test
  public void testBattleAchievementShouldBeFulfilled() throws Exception {
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("TWO_COWS");
    achievementBuilder.setName("Two Cows");

    BattleStatisticsQuery cowQuery = new BattleStatisticsQuery();
    cowQuery.setId(new Id("COW"));

    achievementBuilder.addBattleStatisticsRequirement(new BattleStatisticsRequirement(cowQuery, 2));
    Achievement achievement = achievementBuilder.createAchievement();

    CreaturePreset cowPreset = new CreaturePreset();
    cowPreset.setId(new Id("COW"));
    cowPreset.setType("BEAST");
    cowPreset.setHealth(1);

    CauseOfDeath unarmedCauseOfDeath = CauseOfDeath.getUnarmedCauseOfDeath();

    Statistics statistics = new Statistics();
    Assert.assertFalse(achievement.isFulfilled(statistics));
    statistics.getBattleStatistics().addBattle(new Creature(cowPreset), unarmedCauseOfDeath, PartOfDay.DAWN);
    Assert.assertFalse(achievement.isFulfilled(statistics));
    statistics.getBattleStatistics().addBattle(new Creature(cowPreset), unarmedCauseOfDeath, PartOfDay.DAWN);
    Assert.assertTrue(achievement.isFulfilled(statistics));
  }

  @Test
  public void testVisitedLocationsBasedAchievementsShouldWorkAsExpected() throws Exception {
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("VISIT_TWO_DIFFERENT_DESERTS");
    achievementBuilder.setName("Visit Two Different Deserts");
    CounterMap<Id> visitedLocations = new CounterMap<>();
    visitedLocations.incrementCounter(new Id("DESERT"), 2);
    achievementBuilder.setVisitedLocations(visitedLocations);
    Achievement achievement = achievementBuilder.createAchievement();

    Statistics statistics = new Statistics();
    Assert.assertFalse(achievement.isFulfilled(statistics));
    statistics.getExplorationStatistics().addVisit(new Point(0, 0, 0), new Id("DESERT"));
    Assert.assertFalse(achievement.isFulfilled(statistics));
    statistics.getExplorationStatistics().addVisit(new Point(0, 0, 0), new Id("DESERT"));
    Assert.assertFalse(achievement.isFulfilled(statistics));
    statistics.getExplorationStatistics().addVisit(new Point(1, 1, 1), new Id("DESERT"));
    Assert.assertTrue(achievement.isFulfilled(statistics));
  }

  @Test
  public void testMaximumVisitsAchievementsShouldWorkAsExpected() throws Exception {
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("VISIT_ANY_DESERT_TWICE");
    achievementBuilder.setName("Visit Any Desert Twice");
    CounterMap<Id> maximumVisits = new CounterMap<>();
    maximumVisits.incrementCounter(new Id("DESERT"), 2);
    achievementBuilder.setMaximumNumberOfVisits(maximumVisits);
    Achievement achievement = achievementBuilder.createAchievement();

    Statistics statistics = new Statistics();
    Assert.assertFalse(achievement.isFulfilled(statistics));
    statistics.getExplorationStatistics().addVisit(new Point(0, 0, 0), new Id("DESERT"));
    Assert.assertFalse(achievement.isFulfilled(statistics));
    statistics.getExplorationStatistics().addVisit(new Point(1, 1, 1), new Id("DESERT"));
    Assert.assertFalse(achievement.isFulfilled(statistics));
    statistics.getExplorationStatistics().addVisit(new Point(0, 0, 0), new Id("DESERT"));
    Assert.assertTrue(achievement.isFulfilled(statistics));
  }

}
