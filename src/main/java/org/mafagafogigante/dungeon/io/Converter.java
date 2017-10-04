package org.mafagafogigante.dungeon.io;

/**
 * Uninstantiable Converter class that defines methods for IO related data conversion.
 */
public class Converter {

  private Converter() {
    throw new AssertionError();
  }

  /**
   * Converts a given number of bytes to a human readable format.
   *
   * @return a String
   */
  public static String bytesToHuman(long bytes) {
    if (bytes < 1024) {
      return bytes + " B";
    }
    // 2 ^ 10 (1 KiB) has (63 - 10) = 53 leading zeros.
    // 2 ^ 20 (1 MiB) has (63 - 20) = 43 leading zeros.
    // And so forth.
    // Bits used to represent the number of bytes = number of bits available - number of leading zeros.
    int bitsUsed = 63 - Long.numberOfLeadingZeros(bytes);
    // (1L << (bitsUsed - bitsUsed % 10)) shifts the one (in binary) to the left by a multiple of 10.
    // This is a fast way to get the power of 1024 by which we must divide the number of bytes.
    double significand = (double) bytes / (1L << (bitsUsed - bitsUsed % 10));
    // By dividing the number of bits used by 10, get the prefix that should be used.
    // Subtract one as Strings are zero indexed.
    char prefix = "KMGTPE".charAt(bitsUsed / 10 - 1);
    return String.format("%.1f %siB", significand, prefix);
  }

}
