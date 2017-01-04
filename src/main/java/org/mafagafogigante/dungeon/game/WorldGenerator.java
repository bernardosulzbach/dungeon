package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.game.LocationPreset.Type;
import org.mafagafogigante.dungeon.io.Version;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * The world generator. This class should be instantiated by a World object.
 */
class WorldGenerator implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private static final int DEFAULT_CHUNK_SIDE = 5;

  private final World world;
  private final RiverGenerator riverGenerator;
  private final DungeonDistributor dungeonDistributor = new DungeonDistributor();
  private final DungeonCreator dungeonCreator = new DungeonCreator(dungeonDistributor);
  private final int chunkSide;

  WorldGenerator(World world) {
    this.world = world;
    this.riverGenerator = new RiverGenerator();
    this.chunkSide = WorldGenerator.DEFAULT_CHUNK_SIDE;
  }

  /**
   * Retrieves a random LocationPreset whose type is "Land".
   *
   * @return a LocationPreset
   */
  private static LocationPreset getRandomLandLocationPreset() {
    LocationPresetStore locationPresetStore = LocationPresetStore.getDefaultLocationPresetStore();
    return Random.select(locationPresetStore.getLocationPresetsByType(Type.LAND));
  }

  private Location createRandomRiverLocation(@NotNull final Point point) {
    LocationPresetStore locationPresetStore = LocationPresetStore.getDefaultLocationPresetStore();
    return new Location(Random.select(locationPresetStore.getLocationPresetsByType(Type.RIVER)), world, point);
  }

  private Location createRandomBridgeLocation(@NotNull final Point point) {
    LocationPresetStore locationPresetStore = LocationPresetStore.getDefaultLocationPresetStore();
    return new Location(Random.select(locationPresetStore.getLocationPresetsByType(Type.BRIDGE)), world, point);
  }

  public void expand(Point point) {
    riverGenerator.expand(point, chunkSide);
    Point currentPoint;
    LocationPreset currentLocationPreset = null;
    int remainingLocationsOfCurrentPreset = 0;
    int pX = point.getX();
    int pY = point.getY();
    // Get the closest smaller chunkSide multiple of x and y.
    // For instance, if chunkSide == 5, x == -2 and y == 1, then it makes xStart == -5 and yStart == 0.
    int xStart = pX < 0 ? chunkSide * (((pX + 1) / chunkSide) - 1) : chunkSide * (pX / chunkSide);
    int yStart = pY < 0 ? chunkSide * (((pY + 1) / chunkSide) - 1) : chunkSide * (pY / chunkSide);
    for (int x = xStart; x < xStart + chunkSide; x++) {
      for (int y = yStart; y < yStart + chunkSide; y++) {
        currentPoint = new Point(x, y, 0);
        if (!world.alreadyHasLocationAt(currentPoint)) {
          if (riverGenerator.isRiver(currentPoint)) {
            world.addLocation(createRandomRiverLocation(currentPoint), currentPoint);
          } else if (riverGenerator.isBridge(currentPoint)) {
            world.addLocation(createRandomBridgeLocation(currentPoint), currentPoint);
          } else if (dungeonDistributor.rollForDungeon(currentPoint)) {
            dungeonCreator.createDungeon(world, currentPoint);
          } else {
            if (currentLocationPreset == null || remainingLocationsOfCurrentPreset == 0) {
              currentLocationPreset = getRandomLandLocationPreset();
              remainingLocationsOfCurrentPreset = currentLocationPreset.getBlobSize();
            }
            world.addLocation(new Location(currentLocationPreset, world, currentPoint), currentPoint);
            remainingLocationsOfCurrentPreset--;
          }
        }
      }
    }
  }

}
