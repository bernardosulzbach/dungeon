/*
 * Copyright (C) 2015 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dungeon.io;

class IOUtils {

  /**
   * Converts a given number of bytes to a human readable format.
   *
   * @return a String
   */
  public static String bytesToHuman(long bytes) {
    if (bytes < 1024) {
      return bytes + " B";
    }
    // 2 ^ 10 (1 kB) has (63 - 10) = 53 leading zeros.
    // 2 ^ 20 (1 MB) has (63 - 20) = 43 leading zeros.
    // And so forth.
    // Bits used to represent the number of bytes = number of bits available - number of leading zeros.
    int bitsUsed = 63 - Long.numberOfLeadingZeros(bytes);
    // (1L << (bitsUsed - bitsUsed % 10)) shifts the one (in binary) to the left by a multiple of 10.
    // This is a fast way to get the power of 1024 by which we must divide the number of bytes.
    double significand = (double) bytes / (1L << (bitsUsed - bitsUsed % 10));
    // By dividing the number of bits used by 10, get the prefix that should be used.
    // Subtract one as Strings are zero indexed.
    char prefix = "kMGTPE".charAt(bitsUsed / 10 - 1);
    return String.format("%.1f %sB", significand, prefix);
  }

}
