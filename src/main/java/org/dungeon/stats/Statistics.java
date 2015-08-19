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

import org.dungeon.commands.IssuedCommand;
import org.dungeon.io.Writer;
import org.dungeon.util.Table;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Statistics class that stores, processes and prints game statistics.
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
  public void printStatistics() {
    Table statistics = new Table("Property", "Value");
    insertCommandStatistics(statistics);
    statistics.insertSeparator();
    insertWorldStatistics(statistics);
    Writer.write(statistics);
  }

  private void insertCommandStatistics(Table statistics) {
    int commandCount = commandStatistics.getCommandCount();
    int chars = commandStatistics.getChars();
    int words = commandStatistics.getWords();
    statistics.insertRow("Commands issued", String.valueOf(commandCount));
    statistics.insertRow("Characters entered", String.valueOf(chars));
    statistics.insertRow("Average characters per command", String.format("%.2f", (double) chars / commandCount));
    statistics.insertRow("Words entered", String.valueOf(words));
    statistics.insertRow("Average words per command", String.format("%.2f", (double) words / commandCount));
  }

  private void insertWorldStatistics(Table statistics) {
    statistics.insertRow("Created Locations", String.valueOf(worldStatistics.getLocationCount()));
    SortedSet<String> locationNames = new TreeSet<String>(worldStatistics.getLocationCounter().keySet());
    for (String name : locationNames) {
      statistics.insertRow("  " + name, String.valueOf(worldStatistics.getLocationCounter().getCounter(name)));
    }
    statistics.insertSeparator();
    statistics.insertRow("Spawned Creatures", String.valueOf(worldStatistics.getSpawnCount()));
    SortedSet<String> spawnNames = new TreeSet<String>(worldStatistics.getSpawnCounter().keySet());
    for (String name : spawnNames) {
      statistics.insertRow("  " + name, String.valueOf(worldStatistics.getSpawnCounter().getCounter(name)));
    }
  }

}
