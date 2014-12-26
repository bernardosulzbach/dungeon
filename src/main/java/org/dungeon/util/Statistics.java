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

package org.dungeon.util;

import org.dungeon.game.IssuedCommand;
import org.dungeon.io.IO;

import java.io.Serializable;

/**
 * Statistics class that stores, processes and prints game statistics.
 * <p/>
 * Created by Bernardo on 07/12/2014.
 */
public class Statistics implements Serializable {

  private final CommandStatistics commandStats;

  public Statistics() {
    commandStats = new CommandStatistics();
  }

  /**
   * Add a command to the statistics.
   */
  public void addCommand(IssuedCommand issuedCommand) {
    commandStats.addCommand(issuedCommand);
  }

  /**
   * Print the statistics.
   */
  public void print() {
    int commandCount = commandStats.getCommandCount();
    int chars = commandStats.getChars();
    int words = commandStats.getWords();
    IO.writeKeyValueString("Commands issued", String.valueOf(commandCount));
    IO.writeKeyValueString("Characters entered", String.valueOf(chars));
    IO.writeKeyValueString("Average characters per command", String.format("%.2f", (double) chars / commandCount));
    IO.writeKeyValueString("Words entered", String.valueOf(words));
    IO.writeKeyValueString("Average words per command", String.format("%.2f", (double) words / commandCount));
  }

}
