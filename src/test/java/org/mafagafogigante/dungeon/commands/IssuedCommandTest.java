package org.mafagafogigante.dungeon.commands;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class IssuedCommandTest {

  @Test(expected = IllegalArgumentException.class)
  public void constructorShouldThrowExceptionOnInputTooBig() throws Exception {
    new IssuedCommand(StringUtils.repeat('A', 65536));
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorShouldThrowExceptionOnInputWithoutTokens() throws Exception {
    new IssuedCommand(StringUtils.repeat(' ', 128));
  }

}
