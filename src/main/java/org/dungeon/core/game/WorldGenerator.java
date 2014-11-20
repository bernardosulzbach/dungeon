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
package org.dungeon.core.game;

import org.dungeon.io.IO;

import java.io.Serializable;

/**
 * The world generator. This class should be instantiated by a World object.
 * <p/>
 * Created by Bernardo Sulzbach on 14/10/14.
 */
class WorldGenerator implements Serializable {

    private final World world;

    private int chunkSide;
    private int generatedLocations;
    private final int MIN_CHUNK_SIDE = 1;
    private final int MAX_CHUNK_SIDE = 50;

    /**
     * Instantiates a new World generator. This should be called by the constructor of a World object.
     */
    public WorldGenerator(World world) {
        this(world, 5);
    }

    private WorldGenerator(World world, int chunkSide) {
        this.world = world;
        this.chunkSide = chunkSide;
    }

    public int getChunkSide() {
        return chunkSide;
    }

    // Returns the new chunk side.
    public int setChunkSide(int chunkSide) {
        if (chunkSide < MIN_CHUNK_SIDE) {
            return this.chunkSide = MIN_CHUNK_SIDE;
        } else if (chunkSide > MAX_CHUNK_SIDE) {
            return this.chunkSide = MAX_CHUNK_SIDE;
        } else {
            return this.chunkSide = chunkSide;
        }
    }

    private Location createRandomLocation() {
        return new Location(GameData.LOCATION_PRESETS[Game.RANDOM.nextInt(GameData.LOCATION_PRESETS.length)], world);
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
                    generatedLocations++;
                }
            }
        }
    }

    /**
     * Outputs some basic statistics about this WorldGenerator.
     */
    public void printStatistics() {
        IO.writeKeyValueString("Chunk side", String.valueOf(chunkSide));
        IO.writeKeyValueString("Chunk size", String.valueOf(chunkSide * chunkSide));
        IO.writeKeyValueString("Locations", String.valueOf(generatedLocations));
    }

}
