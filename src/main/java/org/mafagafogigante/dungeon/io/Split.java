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

package org.mafagafogigante.dungeon.io;

import java.util.Collections;
import java.util.List;

/**
 * The result of a split operation. Has two lists - before and after - that contain the tokens before the split and
 * after the split, respectively.
 */
public final class Split {

  private final List<String> before;
  private final List<String> after;

  private Split(List<String> before, List<String> after) {
    this.before = before;
    this.after = after;
  }

  /**
   * Splits a List of tokens into two lists. The first of which will contain all the tokens that appeared before the
   * first "on" occurrence and whose second list will have all tokens that appeared after this "on" token.
   */
  public static Split splitOnOn(List<String> tokens) {
    int separator = tokens.indexOf("on");
    if (separator != -1) {
      return new Split(tokens.subList(0, separator), tokens.subList(separator + 1, tokens.size()));
    } else {
      return new Split(tokens, Collections.<String>emptyList());
    }
  }

  public List<String> getBefore() {
    return before;
  }

  public List<String> getAfter() {
    return after;
  }

}
