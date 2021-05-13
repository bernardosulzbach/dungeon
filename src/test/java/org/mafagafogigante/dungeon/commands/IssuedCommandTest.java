package org.mafagafogigante.dungeon.commands;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssuedCommandTest {

  @Test
  public void constructorShouldThrowExceptionOnInputTooBig() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new IssuedCommand(StringUtils.repeat('A', 65536));
    });
  }

  @Test
  public void constructorShouldThrowExceptionOnInputWithoutTokens() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new IssuedCommand(StringUtils.repeat(' ', 128));
    });
  }

}
