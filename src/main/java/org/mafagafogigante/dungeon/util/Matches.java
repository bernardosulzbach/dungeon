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

package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.game.Name;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * A collection of Selectable objects that match a given query.
 */
public class Matches<T extends Selectable> {

  private final List<T> matches;

  private int differentNames;
  private boolean differentNamesUpToDate;

  private Matches() {
    matches = new ArrayList<>();
    differentNames = 0;
    differentNamesUpToDate = true;
  }

  /**
   * Converts a Collection to Matches.
   */
  public static <T extends Selectable> Matches<T> fromCollection(Collection<T> collection) {
    Matches<T> newInstance = new Matches<>();
    for (T t : collection) {
      newInstance.addMatch(t);
    }
    return newInstance;
  }

  void addMatch(T match) {
    matches.add(match);
    differentNamesUpToDate = false;
  }

  public T getMatch(int index) {
    return matches.get(index);
  }

  public List<T> toList() {
    return new ArrayList<>(matches);
  }

  /**
   * Returns true if there is a match with the given name, false otherwise.
   *
   * @param name the name used for comparison
   * @return true if there is a match with the given name, false otherwise
   */
  public boolean hasMatchWithName(Name name) {
    for (T match : matches) {
      if (match.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the number of matches.
   */
  public int size() {
    return matches.size();
  }

  /**
   * Returns how many different names the matches have. For instance, if the matches consist of two Entity objects with
   * identical names, this method will return 1.
   *
   * <p>This method will calculate how many different names are in the list of matches or use the last calculated value,
   * if the matches list did not change since the last calculation. Therefore, after adding all matches and calling this
   * method once, subsequent method calls should be substantially faster.
   */
  public int getDifferentNames() {
    if (!differentNamesUpToDate) {
      updateDifferentNamesCount();
    }
    return differentNames;
  }

  /**
   * Updates the differentNames variable after iterating over the list of matches.
   */
  private void updateDifferentNamesCount() {
    HashSet<Name> uniqueNames = new HashSet<>();
    for (T match : matches) {
      uniqueNames.add(match.getName());
    }
    differentNames = uniqueNames.size();
    differentNamesUpToDate = true;
  }

}
