package org.dungeon.core.game;

import java.io.Serializable;

/**
 * The world generator. This class should be instantiated by a World object.
 *
 * Created by mafagafogigante on 14/10/14.
 */
public class WorldGenerator implements Serializable {


    private final World world;
    private final int chunkSide;
    private final LocationPreset[] locationPresets;

    /**
     * Instantiates a new World generator. This should be called by the constructor of a World object.
     */
    public WorldGenerator(World world, LocationPreset[] locationPresets) {
        this(world, 5, locationPresets);
    }

    public WorldGenerator(World world, int chunkSide, LocationPreset[] locationPresets) {
        this.world = world;
        this.chunkSide = chunkSide;
        this.locationPresets = locationPresets;
    }

    private Location createRandomLocation() {
        // Get a random preset.
        LocationPreset preset = locationPresets[Game.RANDOM.nextInt(locationPresets.length)];
        return new Location(preset);
    }

    public void expand(Point p) {
        Point current_point;
        // Get the closest smaller chunkSide multiple of x and y.
        // For instance, if chunkSide == 5, x == -2 and y == 1, then it makes x_start == -5 and y_start == 0.
        int pX = p.getX();
        int pY = p.getY();
        int x_start = pX < 0 ? chunkSide * (((pX + 1) / chunkSide) - 1) : chunkSide * (pX / chunkSide);
        int y_start = pY < 0 ? chunkSide * (((pY + 1) / chunkSide) - 1) : chunkSide * (pY / chunkSide);
        for (int x = x_start; x < x_start + chunkSide; x++) {
            for (int y = y_start; y < y_start + chunkSide; y++) {
                current_point = new Point(x, y);
                if (!world.hasLocation(current_point)) {
                    // TODO: come up with something better than random locations.
                    world.addLocation(createRandomLocation(), current_point);
                }
            }
        }
    }

}
