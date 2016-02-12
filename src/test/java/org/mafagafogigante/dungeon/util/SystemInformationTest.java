package org.mafagafogigante.dungeon.util;

import org.junit.Assert;
import org.junit.Test;

public class SystemInformationTest {

  @Test
  public void testToColoredStringListShouldHaveTheExpectedFormat() throws Exception {
    SystemInformation systemInformation = new SystemInformation();
    String string = systemInformation.toString();
    String expectedRegexp = "Time: .*\nDate: .*\nUser: .*\n.*\n.*";
    Assert.assertTrue("SystemInformation string does not match the expected format", string.matches(expectedRegexp));
  }

}