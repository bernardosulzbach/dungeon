package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class World implements Serializable {

    private final Hero player;

    private final List<Location> locations;
    private final SpawnCounter spawnCounter;

    /**
     * @param startingLocation
     * @param campaignPlayer
     */
    public World(Location startingLocation, Hero campaignPlayer) {
        spawnCounter = new SpawnCounter();

        locations = new ArrayList<>();
        locations.add(startingLocation);

        player = campaignPlayer;
        addCreature(player, 0);
    }

    /**
     * Add a creature to a specific location.
     *
     * @param creature
     * @param locationIndex
     */
    public final void addCreature(Creature creature, int locationIndex) {
        if (-1 < locationIndex && locationIndex < locations.size()) {
            spawnCounter.incrementCounter(creature.getId());
            locations.get(locationIndex).addCreature(creature);
            creature.setLocation(locations.get(locationIndex));
        }
    }

    /**
     * Add an item to a specific location.
     *
     * @param item
     * @param locationIndex
     */
    public void addItem(Item item, int locationIndex) {
        if (-1 < locationIndex && locationIndex < locations.size()) {
            locations.get(locationIndex).addItem(item);
        }
    }

    public Hero getPlayer() {
        return player;
    }

    public Location getLocation(int locationIndex) {
        if (-1 < locationIndex && locationIndex < locations.size()) {
            return locations.get(locationIndex);
        } else {
            return null;
        }
    }

    /**
     * Prints all the spawn counters to the console.
     */
    public void printSpawnCounters() {
        spawnCounter.printCounters();
    }
}
