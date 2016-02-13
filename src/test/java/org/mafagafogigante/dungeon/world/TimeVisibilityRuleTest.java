package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.game.World;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class TimeVisibilityRuleTest {

  @Test
  public void testIsRespectedShouldReturnTrueWhenTheRuleIsRespected() throws Exception {
    TimeVisibilityRule timeVisibilityRule = new TimeVisibilityRule(6, 18);
    World mockedWorld = Mockito.mock(World.class);
    for (int i = 0; i < 12; i++) {
      Mockito.when(mockedWorld.getWorldDate()).thenReturn(new Date(1, 1, 1, i + 6, 0, 0));
      Assert.assertTrue(timeVisibilityRule.isRespected(mockedWorld));
    }
  }

  @Test
  public void testIsRespectedShouldReturnFalseWhenTheRuleIsDisrespected() throws Exception {
    TimeVisibilityRule timeVisibilityRule = new TimeVisibilityRule(6, 18);
    World mockedWorld = Mockito.mock(World.class);
    for (int i = 0; i < 12; i++) {
      Mockito.when(mockedWorld.getWorldDate()).thenReturn(new Date(1, 1, 1, (i + 18) % 24, 0, 0));
      Assert.assertFalse(timeVisibilityRule.isRespected(mockedWorld));
    }
  }

  @Test
  public void testIsRespectedShouldReturnTrueWhenTheRuleIsRespectedEvenForRulesThatCrossMidnight() throws Exception {
    TimeVisibilityRule timeVisibilityRule = new TimeVisibilityRule(18, 6);
    World mockedWorld = Mockito.mock(World.class);
    for (int i = 0; i < 12; i++) {
      Mockito.when(mockedWorld.getWorldDate()).thenReturn(new Date(1, 1, 1, (i + 18) % 24, 0, 0));
      Assert.assertTrue(timeVisibilityRule.isRespected(mockedWorld));
    }
  }

  @Test
  public void testIsRespectedShouldReturnFalseWhenTheRuleIsDisrespectedEvenForRulesThatCrossMidnight()
      throws Exception {
    TimeVisibilityRule timeVisibilityRule = new TimeVisibilityRule(18, 6);
    World mockedWorld = Mockito.mock(World.class);
    for (int i = 0; i < 12; i++) {
      Mockito.when(mockedWorld.getWorldDate()).thenReturn(new Date(1, 1, 1, (i + 6) % 24, 0, 0));
      Assert.assertFalse(timeVisibilityRule.isRespected(mockedWorld));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorDoesNotAllowEqualStartAndEnd() throws Exception {
    new TimeVisibilityRule(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorDoesNotAllowHoursEqualToTheMaximumHour() throws Exception {
    new TimeVisibilityRule(0, 24);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorDoesNotAllowHoursBiggerThanTheMaximumHour() throws Exception {
    new TimeVisibilityRule(0, 100);
  }

}
