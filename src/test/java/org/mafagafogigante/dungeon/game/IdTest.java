package org.mafagafogigante.dungeon.game;

import org.junit.Assert;
import org.junit.Test;

public class IdTest {

  @Test
  public void testConstructor() throws Exception {
    try {
      new Id(null);
      Assert.fail("expected an IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
    }
    try {
      new Id("");
      Assert.fail("expected an IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
    }
    try {
      new Id(" ");
      Assert.fail("expected an IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
    }
    new Id("403_ACCESS_FORBIDDEN");
    new Id("404_NOT_FOUND");
    new Id("VALID_ID");
    new Id("YET_ANOTHER_VALID_ID");
    new Id("ID_WITH_NUMBER_255");
  }

}