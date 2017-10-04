package org.mafagafogigante.dungeon.game;

import org.junit.Test;

public class IdTest {

  @Test(expected = IllegalArgumentException.class)
  public void constructorShouldThrowExceptionOnNull() throws Exception {
    new Id(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorShouldThrowExceptionOnEmptyString() throws Exception {
    new Id("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorShouldThrowExceptionOnStringWithSpaces() throws Exception {
    new Id(" ");
  }

  @Test
  public void constructorShouldWorkForValidStrings() throws Exception {
    new Id("403_ACCESS_FORBIDDEN");
    new Id("404_NOT_FOUND");
    new Id("VALID_ID");
    new Id("ID_WITH_NUMBER_255");
  }

}
