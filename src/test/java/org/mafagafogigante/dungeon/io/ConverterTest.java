package org.mafagafogigante.dungeon.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConverterTest {

  @Test
  public void bytesToHuman() throws Exception {
    Assertions.assertEquals("1 B", Converter.bytesToHuman(1));
    Assertions.assertEquals("2 B", Converter.bytesToHuman(2));
    Assertions.assertEquals("1023 B", Converter.bytesToHuman((1L << 10) - 1));
    Assertions.assertEquals("1.0 KiB", Converter.bytesToHuman(1L << 10));
    Assertions.assertEquals("1024.0 KiB", Converter.bytesToHuman((1L << 20) - 1));
    Assertions.assertEquals("1.0 MiB", Converter.bytesToHuman(1L << 20));
    Assertions.assertEquals("1024.0 MiB", Converter.bytesToHuman((1L << 30) - 1));
    Assertions.assertEquals("1.0 GiB", Converter.bytesToHuman(1L << 30));
    Assertions.assertEquals("1024.0 GiB", Converter.bytesToHuman((1L << 40) - 1));
  }

}
