package org.mafagafogigante.dungeon.util;

import org.junit.Test;

public class TableTest {

  @Test
  public void insertRowShouldWorkWithTheCorrectAmountOfArguments() throws Exception {
    Table table = new Table("A", "B");
    for (int i = 0; i < 100; i++) {
      table.insertRow("1", "2");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorShouldThrowAnExceptionIfThereAreNoArguments() throws Exception {
    new Table();
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorShouldThrowAnExceptionIfThereAreTooManyArguments() throws Exception {
    new Table("A", "B", "C", "D", "E", "F", "G");
  }

  @Test(expected = IllegalArgumentException.class)
  public void insertRowShouldThrowAnExceptionWithTooFewArguments() throws Exception {
    Table table = new Table("A", "B");
    table.insertRow("1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void insertRowShouldThrowAnExceptionWithTooManyArguments() throws Exception {
    Table table = new Table("A", "B");
    table.insertRow("1", "2", "3");
  }

}
