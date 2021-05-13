package org.mafagafogigante.dungeon.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SystemInformationTest {

  @Test
  public void testToColoredStringListShouldHaveTheExpectedFormat() throws Exception {
    SystemInformation systemInformation = new SystemInformation();
    String string = systemInformation.toString();
    String expectedRegexp = "User: .*\nTime: .*\nDate: .*\nJava: .*\nHeap: .*\nOS: .*";
    Assertions.assertTrue(string.matches(expectedRegexp),
            "SystemInformation string does not match the expected format");
  }

}
