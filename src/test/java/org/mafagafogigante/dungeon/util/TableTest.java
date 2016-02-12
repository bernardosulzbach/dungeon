package org.mafagafogigante.dungeon.util;

import org.junit.Assert;
import org.junit.Test;

public class TableTest {

  @Test
  public void insertRowShouldWorkWithTheCorrectAmountOfArguments() throws Exception {
    Table table = new Table("A", "B");
    try {
      for (int i = 0; i < 100; i++) {
        table.insertRow("1", "2");
      }
    } catch (Exception unexpected) {
      Assert.fail();
    }
  }

  @Test
  public void constructorShouldThrowAnExceptionIfThereAreNoArguments() throws Exception {
    try {
      new Table();
      Assert.fail("expected an IllegalArgumentException.");
    } catch (IllegalArgumentException expected) {
    }
  }

  @Test
  public void constructorShouldThrowAnExceptionIfThereAreTooManyArguments() throws Exception {
    try {
      new Table("A", "B", "C", "D", "E", "F", "G");
      Assert.fail("expected an IllegalArgumentException.");
    } catch (IllegalArgumentException expected) {
    }
  }

  @Test
  public void insertRowShouldThrowAnExceptionWithTooFewArguments() throws Exception {
    Table table = new Table("A", "B");
    try {
      table.insertRow("1");
      Assert.fail("expected an IllegalArgumentException.");
    } catch (IllegalArgumentException expected) {
    }
  }

  @Test
  public void insertRowShouldThrowAnExceptionWithTooManyArguments() throws Exception {
    Table table = new Table("A", "B");
    try {
      table.insertRow("1", "2", "3");
      Assert.fail("expected an IllegalArgumentException.");
    } catch (IllegalArgumentException expected) {
    }
  }

}
