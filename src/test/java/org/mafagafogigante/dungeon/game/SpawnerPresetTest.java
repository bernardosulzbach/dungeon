package org.mafagafogigante.dungeon.game;

import org.junit.Test;

public class SpawnerPresetTest {

  @Test(expected = IllegalArgumentException.class)
  public void testSpawnerPresetConstructorThrowsIllegalArgumentExceptionWhenMinimumIsNegative() {
    new SpawnerPreset("ID", -1, 2, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSpawnerPresetConstructorThrowsIllegalArgumentExceptionWhenMinimumIsZero() {
    new SpawnerPreset("ID", 0, 2, 1);
  }

  @Test
  public void testSpawnerPresetConstructorShouldWorkWhenMinimumIsLessThanMaximum() {
    new SpawnerPreset("ID", 1, 2, 1);
  }

  @Test
  public void testSpawnerPresetConstructorShouldWorkWhenMinimumIsEqualToMaximum() {
    new SpawnerPreset("ID", 2, 2, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSpawnerPresetConstructorThrowsIllegalArgumentExceptionWhenMinimumIsGreaterThanMaximum() {
    new SpawnerPreset("ID", 3, 2, 1);
  }

}
