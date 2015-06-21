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

import org.dungeon.game.LocationPreset.Type;

import java.io.Serializable;

/**
 * The world generator. This class should be instantiated by a World object.
 */
class WorldGenerator implements Serializable {

  private static final int CHUNK_SIDE = 5;
  private static final int MIN_DIST_RIVER = 6;
  private static final int MAX_DIST_RIVER = 11;
  private final World world;
  private final RiverGenerator riverGenerator;
  private final int chunkSide;

  WorldGenerator(World world) {
    this.world = world;
    this.chunkSide = WorldGenerator.CHUNK_SIDE;
    riverGenerator = new RiverGenerator(MIN_DIST_RIVER, MAX_DIST_RIVER);
  }

  /**
   * Retrieves a random LocationPreset whose type is "Land".
   *
   * @return a LocationPreset
   */
  private static LocationPreset getRandomLandLocationPreset() {
    return Random.select(GameData.getLocationPresetStore().getLocationPresetsByType(Type.LAND));
  }

  private Location createRandomRiverLocation() {
    return new Location(Random.select(GameData.getLocationPresetStore().getLocationPresetsByType(Type.RIVER)), world);
  }

  private Location createRandomBridgeLocation() {
    return new Location(Random.select(GameData.getLocationPresetStore().getLocationPresetsByType(Type.BRIDGE)), world);
  }

  public void expand(Point p) {
    riverGenerator.expand(p, chunkSide);
    Point currentPoint;
    LocationPreset currentLocationPreset = null;
    int remainingLocationsOfCurrentPreset = 0;
    int pX = p.getX();
    int pY = p.getY();
    // Get the closest smaller chunkSide multiple of x and y.
    // For instance, if chunkSide == 5, x == -2 and y == 1, then it makes xStart == -5 and yStart == 0.
    int xStart = pX < 0 ? chunkSide * (((pX + 1) / chunkSide) - 1) : chunkSide * (pX / chunkSide);
    int yStart = pY < 0 ? chunkSide * (((pY + 1) / chunkSide) - 1) : chunkSide * (pY / chunkSide);
    for (int x = xStart; x < xStart + chunkSide; x++) {
      for (int y = yStart; y < yStart + chunkSide; y++) {
        currentPoint = new Point(x, y);
        if (!world.hasLocation(currentPoint)) {
          if (riverGenerator.isRiver(currentPoint)) {
            world.addLocation(createRandomRiverLocation(), currentPoint);
          } else if (riverGenerator.isBridge(currentPoint)) {
            world.addLocation(createRandomBridgeLocation(), currentPoint);
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
