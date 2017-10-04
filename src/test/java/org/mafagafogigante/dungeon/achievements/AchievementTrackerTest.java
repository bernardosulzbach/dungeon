package org.mafagafogigante.dungeon.achievements;

import org.mafagafogigante.dungeon.achievements.comparators.UnlockedAchievementComparators;
import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.date.DungeonTimeUnit;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.Point;
import org.mafagafogigante.dungeon.stats.Statistics;
import org.mafagafogigante.dungeon.util.CounterMap;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AchievementTrackerTest {

  @Test
  public void testGetUnlockedCountShouldReturnZeroInNewAchievementTracker() throws Exception {
    AchievementTracker achievementTracker = new AchievementTracker(new Statistics());
    Assert.assertEquals(0, achievementTracker.getUnlockedCount());
  }

  @Test
  public void testGetUnlockedCountShouldBeUpdatedAsExpected() throws Exception {
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("ACHIEVEMENT");
    List<Achievement> achievements = Collections.singletonList(achievementBuilder.createAchievement());
    AchievementStore achievementStore = AchievementStoreFactory.makeAchievementStore(achievements);
    AchievementTracker achievementTracker = new AchievementTracker(new Statistics());
    Assert.assertEquals(0, achievementTracker.getUnlockedCount());
    achievementTracker.update(achievementStore, new Date(1, 1, 1));
    Assert.assertEquals(1, achievementTracker.getUnlockedCount());
  }

  @Test
  public void testGetUnlockedAchievementsReturnsTheExpectedUnlockedAchievements() throws Exception {
    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("ACHIEVEMENT");
    final String name = "Name";
    final String info = "Info";
    achievementBuilder.setName(name);
    achievementBuilder.setInfo(info);
    List<Achievement> achievements = Collections.singletonList(achievementBuilder.createAchievement());
    AchievementStore achievementStore = AchievementStoreFactory.makeAchievementStore(achievements);

    AchievementTracker tracker = new AchievementTracker(new Statistics());
    final Date date = new Date(1, 1, 1);
    tracker.update(achievementStore, date);

    Comparator<UnlockedAchievement> defaultComparator = UnlockedAchievementComparators.getDefaultComparator();
    List<UnlockedAchievement> unlocked = tracker.getUnlockedAchievements(defaultComparator);
    for (UnlockedAchievement unlockedAchievement : unlocked) {
      Assert.assertEquals(name, unlockedAchievement.getName());
      Assert.assertEquals(info, unlockedAchievement.getInfo());
      Assert.assertEquals(date, unlockedAchievement.getDate());
    }
  }

  @Test
  public void testHasNotBeenUnlocked() throws Exception {
    final Statistics statistics = new Statistics();
    final AchievementTracker tracker = new AchievementTracker(statistics);

    AchievementBuilder achievementBuilder = new AchievementBuilder();
    achievementBuilder.setId("ACHIEVEMENT");
    final String name = "Name";
    final String info = "Info";
    achievementBuilder.setName(name);
    achievementBuilder.setInfo(info);
    achievementBuilder.setText("Text");
    CounterMap<Id> counterMap = new CounterMap<>();
    counterMap.incrementCounter(new Id("PLACE"));
    achievementBuilder.setVisitedLocations(counterMap);
    Achievement achievement = achievementBuilder.createAchievement();
    List<Achievement> achievements = Collections.singletonList(achievement);
    AchievementStore achievementStore = AchievementStoreFactory.makeAchievementStore(achievements);
    final Date date = new Date(1, 1, 1);
    tracker.update(achievementStore, date);

    Assert.assertTrue(tracker.hasNotBeenUnlocked(achievement));

    statistics.getExplorationStatistics().addVisit(new Point(0, 0, 0), new Id("PLACE"));

    Assert.assertTrue(tracker.hasNotBeenUnlocked(achievement));

    tracker.update(achievementStore, date.plus(1, DungeonTimeUnit.SECOND));
    Assert.assertFalse(tracker.hasNotBeenUnlocked(achievement));
  }

}
