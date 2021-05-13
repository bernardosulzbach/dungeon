package org.mafagafogigante.dungeon.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IdTest {

  @Test
  public void constructorShouldThrowExceptionOnNull() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new Id(null);
    });
  }

  @Test
  public void constructorShouldThrowExceptionOnEmptyString() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new Id("");
    });
  }

  @Test
  public void constructorShouldThrowExceptionOnStringWithSpaces() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new Id(" ");
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new Id("A ");
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new Id(" A");
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new Id("A A");
    });
  }

  @Test
  public void constructorShouldWorkForValidStrings() throws Exception {
    new Id("403_ACCESS_FORBIDDEN");
    new Id("404_NOT_FOUND");
    new Id("VALID_ID");
    new Id("ID_WITH_NUMBER_255");
  }

}
