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

package org.dungeon.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The world generator. This class should be instantiated by a World object.
 * <p/>
 * Created by Bernardo Sulzbach on 14/10/14.
 */
public class WorldGenerator implements Serializable {

  private static final int MIN_CHUNK_SIDE = 1;
  private static final int DEF_CHUNK_SIDE = 5;
  private static final int MAX_CHUNK_SIDE = 50;
  private static final int MIN_DIST_RIVER = 6;
  private static final int MAX_DIST_RIVER = 11;
  private final World world;
  private final RiverGenerator riverGenerator;
  private int chunkSide;

  /**
   * Instantiates a new World generator. This should be called by the constructor of a World object.
   */
  public WorldGenerator(World world) {
    this(world, DEF_CHUNK_SIDE);
  }

  private WorldGenerator(World world, int chunkSide) {
    this.world = world;
    this.chunkSide = chunkSide;
    riverGenerator = new RiverGenerator(MIN_DIST_RIVER, MAX_DIST_RIVER);
  }

  public int getChunkSide() {
    return chunkSide;
  }

  /**
   * Set a new value for the chunk side.
   *
   * @param chunkSide the new value for the chunk side.
   * @return the value actually set, it may be different from the provided chunk side.
   */
  public int setChunkSide(int chunkSide) {
    if (chunkSide < MIN_CHUNK_SIDE) {
      return this.chunkSide = MIN_CHUNK_SIDE;
    } else if (chunkSide > MAX_CHUNK_SIDE) {
      return this.chunkSide = MAX_CHUNK_SIDE;
    } else {
      return this.chunkSide = chunkSide;
    }
  }

  /**
   * Retrieves a random LocationPreset whose type is "Land".
   *
   * @return a LocationPreset
   */
  private LocationPreset getRandomLandLocationPreset() {
    // After getting something better than random locations, we will have the desired Location ID here.
    List<LocationPreset> locationPresets = new ArrayList<LocationPreset>(GameData.getLocationPresets().values());
    LocationPreset selectedPreset;
    do {
      selectedPreset = locationPresets.get(Engine.RANDOM.nextInt(locationPresets.size()));
    } while (!"Land".equals(selectedPreset.getType()));
    return selectedPreset;
  }

  private Location createRiverLocation() {
    return new Location(GameData.getLocationPresets().get(new ID("RIVER")), world);
  }

  private Location createBridgeLocation() {
    return new Location(GameData.getLocationPresets().get(new ID("BRIDGE")), world);
  }

  public void expand(Point p) {
    riverGenerator.expand(p, chunkSide);
    Point currentPoint;
    LocationPreset currentLocationPreset = null;
    int remainingLocationsOfCurrentPreset = 0;
    int pX = p.getX();
    int pY = p.getY();
    // Get the closest smaller chunkSide multiple of x and y.
    // For instance, if chunkSide == 5, x == -2 and y == 1, then it makes x_start == -5 and y_start == 0.
    int x_start = pX < 0 ? chunkSide * (((pX + 1) / chunkSide) - 1) : chunkSide * (pX / chunkSide);
    int y_start = pY < 0 ? chunkSide * (((pY + 1) / chunkSide) - 1) : chunkSide * (pY / chunkSide);
    for (int x = x_start; x < x_start + chunkSide; x++) {
      for (int y = y_start; y < y_start + chunkSide; y++) {
        currentPoint = new Point(x, y);
        if (!world.hasLocation(currentPoint)) {
          if (riverGenerator.isRiver(currentPoint)) {
            world.addLocation(createRiverLocation(), currentPoint);
          } else if (riverGenerator.isBridge(currentPoint)) {
            world.addLocation(createBridgeLocation(), currentPoint);
          } else {
            if (currentLocationPreset == null || remainingLocationsOfCurrentPreset == 0) {
              currentLocationPreset = getRandomLandLocationPreset();
              remainingLocationsOfCurrentPreset = currentLocationPreset.getBlobSize();
            }
            world.addLocation(new Location(currentLocationPreset, world), currentPoint);
            remainingLocationsOfCurrentPreset--;
          }
        }
      }
    }
  }

}
