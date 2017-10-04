package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.io.Version;

import org.hamcrest.Matchers;
import org.hamcrest.core.IsNot;
import org.hamcrest.text.IsEmptyString;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class VersionTest {

  @Test
  public void versionStringShouldNotBeEmpty() {
    Assert.assertThat(Version.getCurrentVersion().toString(), new IsNot<>(new IsEmptyString()));
  }

  @Test
  public void versionOrderingShouldBeValid() {
    final int minimum = 1;
    final int maximum = 3;
    List<Version> versions = new ArrayList<>();
    for (int major = minimum; major <= maximum; major++) {
      for (int minor = minimum; minor <= maximum; minor++) {
        for (int patch = minimum; patch <= maximum; patch++) {
          versions.add(new Version(String.format("v%d.%d.%d", major, minor, patch)));
        }
      }
    }
    for (int i = 0; i < versions.size(); i++) {
      final Version left = versions.get(i);
      for (int j = 0; j < versions.size(); j++) {
        final Version right = versions.get(j);
        if (i < j) {
          Assert.assertThat(left, Matchers.lessThan(right));
        } else if (i == j) {
          Assert.assertThat(left, Matchers.equalTo(right));
        } else {
          Assert.assertThat(left, Matchers.greaterThan(right));
        }
      }
    }
  }

}
