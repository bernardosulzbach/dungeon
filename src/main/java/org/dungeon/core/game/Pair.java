package org.dungeon.core.game;

/**
 * Pair base class that defines a generic immutable pair of elements.
 * <p/>
 * Created by Bernardo Sulzbach on 12/11/14.
 */
public class Pair<T, U> {

    public final T a;
    public final U b;

    public Pair(T a, U b) {
        this.a = a;
        this.b = b;
    }

}
