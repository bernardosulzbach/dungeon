package org.dungeon.core.game;

/**
 * ItemFrequencyPair that wraps Pair in order to provide a simple object to track a creatureID and a frequency value.
 * <p/>
 * Created by Bernardo Sulzbach on 12/11/14.
 */
public class ItemFrequencyPair {

    private final Pair<String, Double> pair;

    public ItemFrequencyPair(String id, double frequency) {
        this.pair = new Pair<String, Double>(id, frequency);
    }

    public String getId() {
        return pair.a;
    }

    public double getFrequency() {
        return pair.b;
    }

}
