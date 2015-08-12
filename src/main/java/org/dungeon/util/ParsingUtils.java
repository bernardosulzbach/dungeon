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

package org.dungeon.util;

import java.util.Arrays;

/**
 * Parsing utilities.
 */
public final class ParsingUtils {

  private ParsingUtils() {
    throw new AssertionError();
  }

  /**
   * Splits an array of tokens into two smaller arrays. The first of which will contain all the tokens that appeared
   * before the first "on" occurrence and whose second array will have all tokens that appeared after this "on" token.
   */
  public static SplitResult splitOnOn(String[] tokens) {
    int separator = ArrayUtils.findFirstOccurrence(tokens, "on");
    if (separator == tokens.length) {
      return new SplitResult(tokens, new String[0]);
    } else {
      return new SplitResult(Arrays.copyOfRange(tokens, 0, separator),
          Arrays.copyOfRange(tokens, separator + 1, tokens.length));
    }
  }

  /**
   * The result of a split operation. Has two arrays - before and after - that contain the tokens before the split and
   * after the split, respectively.
   */
  public static class SplitResult {

    public final String[] before;
    public final String[] after;

    public SplitResult(String[] before, String[] after) {
      this.before = before;
      this.after = after;
    }

  }

}
