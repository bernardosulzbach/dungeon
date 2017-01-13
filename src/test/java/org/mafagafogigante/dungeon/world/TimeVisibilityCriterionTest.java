package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.entity.creatures.Observer;
import org.mafagafogigante.dungeon.game.Location;
import org.mafagafogigante.dungeon.game.World;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TimeVisibilityCriterionTest {

  private static final int HOURS_IN_DAY = 24;

  private Observer observer = Mockito.mock(Observer.class);
  private Location location = Mockito.mock(Location.class);
  private World world = Mockito.mock(World.class);

  /**
   * Initialize the Mockito stubs.
   */
  @Before
  public void initialize() {
    Mockito.when(observer.getObserverLocation()).thenReturn(location);
    Mockito.when(location.getWorld()).thenReturn(world);
  }

  @Test
  public void testIsMetByShouldReturnTrueWhenTheCriterionIsMet() throws Exception {
    TimeVisibilityCriterion criterion = new TimeVisibilityCriterion(6, 18);
    for (int i = 0; i < 12; i++) {
      Mockito.when(world.getWorldDate()).thenReturn(new Date(1, 1, 1, i + 6, 0, 0));
      Assert.assertTrue(criterion.isMetBy(observer));
    }
  }

  @Test
  public void testIsMetByShouldReturnFalseWhenTheCriterionIsUnmet() throws Exception {
    TimeVisibilityCriterion criterion = new TimeVisibilityCriterion(6, 18);
    for (int i = 0; i < 12; i++) {
      Mockito.when(world.getWorldDate()).thenReturn(new Date(1, 1, 1, (i + 18) % HOURS_IN_DAY, 0, 0));
      Assert.assertFalse(criterion.isMetBy(observer));
    }
  }

  @Test
  public void testIsMetByShouldReturnTrueWhenTheCriterionIsMetEvenForCriteriaThatCrossMidnight() throws Exception {
    for (int begin = 1; begin < HOURS_IN_DAY; begin++) {
      for (int end = 0; end < begin; end++) {
        TimeVisibilityCriterion criterion = new TimeVisibilityCriterion(begin, end);
        for (int hour = begin; hour != end; hour = (hour + 1) % HOURS_IN_DAY) {
          Date date = new Date(1, 1, 1, hour, 0, 0);
          Mockito.when(world.getWorldDate()).thenReturn(date);
          Assert.assertTrue(criterion.isMetBy(observer));
        }
      }
    }
  }

  @Test
  public void testIsMetByShouldReturnFalseWhenTheCriterionIsUnmetEvenForCriteriaThatCrossMidnight() throws Exception {
    for (int begin = 1; begin < HOURS_IN_DAY; begin++) {
      for (int end = 0; end < begin; end++) {
        TimeVisibilityCriterion criterion = new TimeVisibilityCriterion(begin, end);
        for (int hour = end; hour != begin; hour = (hour + 1) % HOURS_IN_DAY) {
          Date date = new Date(1, 1, 1, hour, 0, 0);
          Mockito.when(world.getWorldDate()).thenReturn(date);
          Assert.assertFalse(criterion.isMetBy(observer));
        }
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorDoesNotAllowEqualStartAndEnd() throws Exception {
    new TimeVisibilityCriterion(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorDoesNotAllowHoursEqualToTheMaximumHour() throws Exception {
    new TimeVisibilityCriterion(0, 24);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorDoesNotAllowHoursBiggerThanTheMaximumHour() throws Exception {
    new TimeVisibilityCriterion(0, 100);
  }

}
