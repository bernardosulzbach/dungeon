package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.entity.creatures.Observer;
import org.mafagafogigante.dungeon.game.Location;
import org.mafagafogigante.dungeon.game.World;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TimeVisibilityCriterionTest {

  private static final int HOURS_IN_DAY = 24;

  private final Observer observer = Mockito.mock(Observer.class);
  private final Location location = Mockito.mock(Location.class);
  private final World world = Mockito.mock(World.class);

  /**
   * Initialize the Mockito stubs.
   */
  @BeforeEach
  public void initialize() {
    Mockito.when(observer.getObserverLocation()).thenReturn(location);
    Mockito.when(location.getWorld()).thenReturn(world);
  }

  @Test
  public void testIsMetByShouldReturnTrueWhenTheCriterionIsMet() throws Exception {
    TimeVisibilityCriterion criterion = new TimeVisibilityCriterion(6, 18);
    for (int i = 0; i < 12; i++) {
      Mockito.when(world.getWorldDate()).thenReturn(new Date(1, 1, 1, i + 6, 0, 0));
      Assertions.assertTrue(criterion.isMetBy(observer));
    }
  }

  @Test
  public void testIsMetByShouldReturnFalseWhenTheCriterionIsUnmet() throws Exception {
    TimeVisibilityCriterion criterion = new TimeVisibilityCriterion(6, 18);
    for (int i = 0; i < 12; i++) {
      Mockito.when(world.getWorldDate()).thenReturn(new Date(1, 1, 1, (i + 18) % HOURS_IN_DAY, 0, 0));
      Assertions.assertFalse(criterion.isMetBy(observer));
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
          Assertions.assertTrue(criterion.isMetBy(observer));
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
          Assertions.assertFalse(criterion.isMetBy(observer));
        }
      }
    }
  }

  @Test
  public void testConstructorDoesNotAllowEqualStartAndEnd() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new TimeVisibilityCriterion(0, 0);
    });
  }

  @Test
  public void testConstructorDoesNotAllowHoursEqualToTheMaximumHour() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new TimeVisibilityCriterion(0, 24);
    });
  }

  @Test
  public void testConstructorDoesNotAllowHoursBiggerThanTheMaximumHour() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new TimeVisibilityCriterion(0, 100);
    });
  }

}
