package org.mafagafogigante.dungeon.io;

import org.junit.Assert;
import org.junit.Test;

public class ConverterTest {

  @Test
  public void bytesToHuman() throws Exception {
    Assert.assertEquals("1 B", Converter.bytesToHuman(1));
    Assert.assertEquals("2 B", Converter.bytesToHuman(2));
    Assert.assertEquals("1023 B", Converter.bytesToHuman((1L << 10) - 1));
    Assert.assertEquals("1.0 KiB", Converter.bytesToHuman(1L << 10));
    Assert.assertEquals("1024.0 KiB", Converter.bytesToHuman((1L << 20) - 1));
    Assert.assertEquals("1.0 MiB", Converter.bytesToHuman(1L << 20));
    Assert.assertEquals("1024.0 MiB", Converter.bytesToHuman((1L << 30) - 1));
    Assert.assertEquals("1.0 GiB", Converter.bytesToHuman(1L << 30));
    Assert.assertEquals("1024.0 GiB", Converter.bytesToHuman((1L << 40) - 1));
  }

}
