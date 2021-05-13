package org.mafagafogigante.dungeon.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TableTest {

  @Test
  public void insertRowShouldWorkWithTheCorrectAmountOfArguments() throws Exception {
    Table table = new Table("A", "B");
    for (int i = 0; i < 100; i++) {
      table.insertRow("1", "2");
    }
  }

  @Test
  public void constructorShouldThrowAnExceptionIfThereAreNoArguments() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new Table();
    });
  }

  @Test
  public void constructorShouldThrowAnExceptionIfThereAreTooManyArguments() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new Table("A", "B", "C", "D", "E", "F", "G");
    });
  }

  @Test
  public void insertRowShouldThrowAnExceptionWithTooFewArguments() throws Exception {
    Table table = new Table("A", "B");
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      table.insertRow("1");
    });
  }

  @Test
  public void insertRowShouldThrowAnExceptionWithTooManyArguments() throws Exception {
    Table table = new Table("A", "B");
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      table.insertRow("1", "2", "3");
    });
  }

}
