package game;

import java.io.Serializable;
import java.util.HashMap;

/**
 * SpawnCounter class that is used by a World object to count its spawns.
 */
class SpawnCounter implements Serializable {

    private static final String EMPTY_SPAWN_COUNTER = "The spawn counter is empty.";
    private HashMap<CreatureID, Integer> counters;

    public SpawnCounter() {
        counters = new HashMap<>();
    }

    public void incrementCounter(CreatureID id) {
        if (counters.containsKey(id)) {
            counters.put(id, counters.get(id) + 1);
        } else {
            counters.put(id, 1);
        }
    }

    /**
     * Send all the counters to the output.
     */
    public void printCounters() {
        if (counters.isEmpty()) {
            IO.writeString(EMPTY_SPAWN_COUNTER);
        } else {
            StringBuilder sb = new StringBuilder();
            for (CreatureID id : counters.keySet()) {
                sb.append(String.format("  %-20s%10d\n", id, counters.get(id)));
            }
            // Remove the last newline character.
            sb.setLength(sb.length() - 1);
            IO.writeString(sb.toString());
        }
    }
}
