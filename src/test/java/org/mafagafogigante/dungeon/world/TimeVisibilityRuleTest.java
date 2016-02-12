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