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

package org.dungeon.stats;

import org.dungeon.game.IssuedCommand;
import org.dungeon.io.IO;
import org.dungeon.util.CounterMap;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Statistics class that stores, processes and prints game statistics.
 * <p/>
 * Created by Bernardo on 07/12/2014.
 */
public final class Statistics implements Serializable {

  private final WorldStatistics worldStatistics = new WorldStatistics();
  private final ExplorationStatistics explorationStatistics = new ExplorationStatistics();
  private final BattleStatistics battleStatistics = new BattleStatistics();
  private final CommandStatistics commandStatistics = new CommandStatistics();

  /**
   * Returns the WorldStatistics object of this Statistics.
   *
   * @return a WorldStatistics object.
   */
  public WorldStatistics getWorldStatistics() {
    return worldStatistics;
  }

  public ExplorationStatistics getExplorationStatistics() {
    return explorationStatistics;
  }

  /**
   * Returns the BattleStatistics object of this Statistics.
   *
   * @return a BattleStatistics object.
   */
  public BattleStatistics getBattleStatistics() {
    return battleStatistics;
  }

  /**
   * Adds an issued command to the CommandStatistics.
   */
  public void addCommand(IssuedCommand issuedCommand) {
    commandStatistics.addCommand(issuedCommand);
  }

  /**
   * Prints the statistics.
   */
  public void printAllStatistics() {
    printCommandStatistics();
    printWorldStatistics();
  }

  /**
   * Prints the statistics tracked by CommandStatistics.
   */
  private void printCommandStatistics() {
    int commandCount = commandStatistics.getCommandCount();
    int chars = commandStatistics.getChars();
    int words = commandStatistics.getWords();
    IO.writeKeyValueString("Commands issued", String.valueOf(commandCount));
    IO.writeKeyValueString("Characters entered", String.valueOf(chars));
    IO.writeKeyValueString("Average characters per command", String.format("%.2f", (double) chars / commandCount));
    IO.writeKeyValueString("Words entered", String.valueOf(words));
    IO.writeKeyValueString("Average words per command", String.format("%.2f", (double) words / commandCount));
  }

  /**
   * Prints the statistics tracked by WorldStatistics.
   */
  private void printWorldStatistics() {
    IO.writeKeyValueString("Created Locations", String.valueOf(worldStatistics.getLocationCount()));
    IO.writeKeyValueString("Spawned Creatures", String.valueOf(worldStatistics.getCreatureCount()));
  }

  /**
   * Prints the spawn statistics tracked by WorldStatistics.
   */
  public void printSpawnStatistics() {
    CounterMap<String> spawnCounter = worldStatistics.getSpawnCounter();
    SortedSet<String> sortedSet = new TreeSet<String>(spawnCounter.keySet());
    for (String string : sortedSet) {
      IO.writeKeyValueString(string, String.valueOf(spawnCounter.getCounter(string)));
    }
  }

}
