package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.io.Version;

import org.hamcrest.core.IsNot;
import org.hamcrest.text.IsEmptyString;
import org.junit.Assert;
import org.junit.Test;

public class VersionTest {

  @Test
  public void versionStringShouldNotBeEmpty() {
    Assert.assertThat(new Version().toString(), new IsNot<>(new IsEmptyString()));
  }

}
