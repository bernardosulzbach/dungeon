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

package org.mafagafogigante.dungeon.stats;

import org.mafagafogigante.dungeon.commands.IssuedCommand;

import java.io.Serializable;

/**
 * CommandStatistics class that is a component of Statistics.
 */
final class CommandStatistics implements Serializable {

  private int commands;
  private int chars;
  private int words;

  /**
   * Adds an issued command to the statistics.
   *
   * @param issuedCommand the command to be added.
   */
  public void addCommand(IssuedCommand issuedCommand) {
    commands++;
    words += issuedCommand.getTokens().length;
    for (char c : issuedCommand.getStringRepresentation().toCharArray()) {
      if (!Character.isWhitespace(c)) {
        chars++;
      }
    }
  }

  /**
   * Returns how many commands the user has issued so far.
   *
   * @return the command count
   */
  public int getCommandCount() {
    return commands;
  }

  /**
   * Returns how many printable characters the user has entered so far.
   *
   * @return the character count
   */
  public int getChars() {
    return chars;
  }

  /**
   * Returns how many words the user has entered so far.
   *
   * @return the word count
   */
  public int getWords() {
    return words;
  }

}
