package org.dungeon.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A thin wrapper for a list of matches. Return type of selectFromList.
 * <p/>
 * Created by Bernardo Sulzbach on 15/11/14.
 */
public class SelectionResult<T> {

    private final List<T> matches;

    public SelectionResult() {
        matches = new ArrayList<T>();
    }

    public void addMatch(T match) {
        matches.add(match);
    }

    public T getMatch(int index) {
        return matches.get(index);
    }

    public int size() {
        return matches.size();
    }

}
