/*
 * Copyright (C) 2014 Bernardo Sulzbach
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

import org.dungeon.io.DLogger;

/**
 * ResourceLine class that represents a line of a resource file.
 * <p/>
 * Created by Bernardo Sulzbach on 12/28/14.
 */
final class ResourceLine {

  private static final char LINE_BREAK = '\\';
  private static final String[] COMMENT_ESCAPES = {"//", "#"};

  private final String text;
  private final boolean valid;

  /**
   * A String that represents the returned text. Works as a cache for multiple toString calls.
   */
  private String returnText;

  /**
   * Constructs a ResourceLine based on a String read from a resource file.
   *
   * @param line the line of text.
   */
  public ResourceLine(String line) {
    if (line == null) {
      DLogger.warning("Tried to create a ResourceLine with a null String.");
      this.text = null;
      valid = false;
    } else {
      this.text = line.trim();
      valid = !this.text.isEmpty();
    }
  }

  /**
   * Predicate method that evaluates if this ResourceLine is a valid line.
   *
   * @return true if the line is valid. False otherwise.
   */
  boolean isValid() {
    return valid;
  }

  /**
   * Predicate method that evaluates if this ResourceLine is continued.
   *
   * @return true if the line ends with a line break character. False otherwise.
   */
  boolean isContinued() {
    return valid && text.charAt(text.length() - 1) == LINE_BREAK;
  }

  /**
   * Predicate method that evaluates if this ResourceLine is a comment.
   *
   * @return true if the line starts with a comment escape sequence. False otherwise.
   */
  boolean isComment() {
    if (valid) {
      for (String escape : COMMENT_ESCAPES) {
        if (text.startsWith(escape)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  /**
   * Returns a line of text without the line break character (if there is one) and without any trailing whitespaces.
   *
   * If this ResourceLine is a comment, {@code null} is returned.
   */
  public String toString() {
    if (isComment()) {
      return null;
    }
    if (returnText == null) {
      makeReturnText();
    }
    return returnText;
  }

  private void makeReturnText() {
    returnText = text;
    if (isContinued()) {
      returnText = returnText.substring(0, returnText.length() - 1);
    }
    // Just loop if the String is not empty and the last character is a whitespace.
    if (!returnText.isEmpty() && Character.isWhitespace(returnText.charAt(returnText.length() - 1))) {
      int indexOfFirstTrailingWhitespace = returnText.length() - 1;
      // Loop until the index becomes 0 or starts to point to a character that is not a whitespace.
      while (indexOfFirstTrailingWhitespace > 0) {
        if (Character.isWhitespace(returnText.charAt(indexOfFirstTrailingWhitespace - 1))) {
          indexOfFirstTrailingWhitespace--;
        } else {
          break;
        }
      }
      returnText = returnText.substring(0, indexOfFirstTrailingWhitespace);
    }

  }

}
