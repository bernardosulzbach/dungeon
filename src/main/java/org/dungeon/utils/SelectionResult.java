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
package org.dungeon.utils;

import org.dungeon.core.game.Selectable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Wraps a list of Selectable objects that match a given query. This class provides methods to get a match from the list
 * of matches, get the size of the list of matches and a method to retrieve the number of matches with a different
 * name.
 * <p/>
 * Created by Bernardo Sulzbach on 15/11/14.
 */
public class SelectionResult<T extends Selectable> {

    private final List<T> matches;

    private int differentNames;
    private boolean differentNamesUpToDate;

    public SelectionResult() {
        matches = new ArrayList<T>();
        differentNames = 0;
        differentNamesUpToDate = true;
    }

    public void addMatch(T match) {
        matches.add(match);
        differentNamesUpToDate = false;
    }

    public T getMatch(int index) {
        return matches.get(index);
    }

    /**
     * Returns true if there is a match with the given name, false otherwise.
     *
     * @param name the name used for comparison.
     * @return true if there is a match with the given name, false otherwise.
     */
    public boolean hasName(String name) {
        for (T match : matches) {
            if (match.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the number of matches in this SelectionResult.
     *
     * @return the number of matches in this SelectionResult.
     */
    public int size() {
        return matches.size();
    }

    // TODO: write tests for this class (mainly, for this method).

    /**
     * Returns how many different names the matches have. For instance, if the matches consist of two Entity objects
     * with identical names, this method will return 1.
     * <p/>
     * This method will calculate how many different names are in the list of matches or use the last calculated value,
     * if the matches list did not change since the last calculation. Therefore, after adding all matches and calling
     * this method once, subsequent method calls should be substantially faster.
     * <p/>
     * Adding more elements to the SelectionResult will make necessary a new iteration through the list before returning
     * the new amount of different names.
     * <p/>
     * This is more efficient than updating the counter after every addition as it allows many matches to be added with
     * little overhead, letting the different names calculation to the end.
     *
     * @return how many different names the matches have.
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
        HashSet<String> uniqueNames = new HashSet<String>();
        for (T match : matches) {
            uniqueNames.add(match.getName());
        }
        differentNames = uniqueNames.size();
        differentNamesUpToDate = true;
    }

}
