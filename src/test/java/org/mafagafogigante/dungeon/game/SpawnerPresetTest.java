package org.mafagafogigante.dungeon.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SpawnerPresetTest {

  @Test
  public void testSpawnerPresetConstructorThrowsIllegalArgumentExceptionWhenMinimumIsNegative() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new SpawnerPreset("ID", -1, 2, 1);
    });
  }

  @Test
  public void testSpawnerPresetConstructorThrowsIllegalArgumentExceptionWhenMinimumIsZero() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new SpawnerPreset("ID", 0, 2, 1);
    });
  }

  @Test
  public void testSpawnerPresetConstructorShouldWorkWhenMinimumIsLessThanMaximum() {
    new SpawnerPreset("ID", 1, 2, 1);
  }

  @Test
  public void testSpawnerPresetConstructorShouldWorkWhenMinimumIsEqualToMaximum() {
    new SpawnerPreset("ID", 2, 2, 1);
  }

  @Test
  public void testSpawnerPresetConstructorThrowsIllegalArgumentExceptionWhenMinimumIsGreaterThanMaximum() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new SpawnerPreset("ID", 3, 2, 1);
    });
  }

}
