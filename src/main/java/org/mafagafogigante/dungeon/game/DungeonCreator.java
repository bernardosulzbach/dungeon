package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.game.LocationPreset.Type;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.logging.DungeonLogger;
import org.mafagafogigante.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * A class that know how to create dungeons.
 */
class DungeonCreator implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private static final Percentage HORIZONTAL_EXPANSION_PROBABILITY = Percentage.fromString("50%");

  /**
   * It is of uttermost importance that we respect this rectangle.
   *
   * <p>Currently a 5x1 So that we can make dungeons like this: R=R=R (where R is a room and = is a corridor).
   */
  private static final MinimumBoundingRectangle minimumBoundingRectangle = new MinimumBoundingRectangle(5, 1);
  private final DungeonDistributor distributor;

  public DungeonCreator(DungeonDistributor distributor) {
    this.distributor = distributor;
  }

  public static MinimumBoundingRectangle getMinimumBoundingRectangle() {
    return minimumBoundingRectangle;
  }

  private static LocationPreset getRandomLocationPreset(Type type) {
    LocationPresetStore locationPresetStore = LocationPresetStore.getDefaultLocationPresetStore();
    List<LocationPreset> entrancePresets = locationPresetStore.getLocationPresetsByType(type);
    return Random.select(entrancePresets);
  }

  /**
   * Creates a dungeon placing the entrance at the specified point. The world should not have a location at the
   * specified point.
   */
  public void createDungeon(@NotNull World world, @NotNull Point entrance) {
    Point mainRoomPoint = createEntrance(world, entrance);
    Location mainRoomLocation = createMainRoom(world, mainRoomPoint);
    finishDungeon(world, mainRoomPoint, mainRoomLocation);
  }

  /**
   * Creates the dungeon's entrance and the necessary stairways.
   *
   * <p>Returns the point where the main dungeon room should be.
   */
  private Point createEntrance(@NotNull World world, @NotNull Point entrance) {
    // The entrance.
    if (world.alreadyHasLocationAt(entrance)) {
      throw new IllegalStateException("world has location at the specified entrance.");
    }
    Location dungeonEntrance = new Location(getRandomLocationPreset(Type.DUNGEON_ENTRANCE), world, entrance);
    world.addLocation(dungeonEntrance, entrance);
    distributor.registerDungeonEntrance(entrance);
    // The stairway.
    Point stairwayPoint = new Point(entrance, Direction.DOWN);
    // Note that all DUNGEON_STAIRWAY presets are blocked towards North, East, South, and West.
    Location stairwayLocation = new Location(getRandomLocationPreset(Type.DUNGEON_STAIRWAY), world, stairwayPoint);
    world.addLocation(stairwayLocation, stairwayPoint);
    return new Point(stairwayPoint, Direction.DOWN);
  }

  @NotNull
  private Location createMainRoom(@NotNull World world, Point mainRoomPoint) {
    // Note that all DUNGEON_ROOM presets are open on all directions. It is up to the code to properly block them.
    Location dungeonRoom = new Location(getRandomLocationPreset(Type.DUNGEON_ROOM), world, mainRoomPoint);
    dungeonRoom.getBlockedEntrances().block(Direction.NORTH);
    dungeonRoom.getBlockedEntrances().block(Direction.DOWN);
    dungeonRoom.getBlockedEntrances().block(Direction.SOUTH);
    world.addLocation(dungeonRoom, mainRoomPoint); // The main room. All dungeons have one.
    return dungeonRoom;
  }

  /**
   * Finishes the dungeon by randomly deciding to expand it to the east and west.
   *
   * <p>If this method does not make a corridor to east or west, it blocks that entrance in the main room to prevent
   * glitches.
   */
  private void finishDungeon(@NotNull World world, Point mainRoomPoint, Location mainRoomLocation) {
    // UPDATING THIS LOGIC MAY REQUIRE YOU TO UPDATE THE minimumBoundingRectangle variable.
    if (Random.roll(HORIZONTAL_EXPANSION_PROBABILITY)) {
      expandTowards(world, mainRoomPoint, Direction.EAST);
    } else {
      mainRoomLocation.getBlockedEntrances().block(Direction.EAST);
    }
    if (Random.roll(HORIZONTAL_EXPANSION_PROBABILITY)) {
      expandTowards(world, mainRoomPoint, Direction.WEST);
    } else {
      mainRoomLocation.getBlockedEntrances().block(Direction.WEST);
    }
  }

  private void expandTowards(@NotNull World world, @NotNull Point origin, Direction direction) {
    Point corridorPoint = new Point(origin, direction);
    if (world.alreadyHasLocationAt(corridorPoint)) {
      DungeonLogger.warning("Found an existing location when attempting to expand a Dungeon at " + corridorPoint + ".");
    }
    // Note that all DUNGEON_CORRIDOR presets have blocked UP and DOWN. It is up to the code to properly block the rest.
    Location corridorLocation = new Location(getRandomLocationPreset(Type.DUNGEON_CORRIDOR), world, corridorPoint);
    corridorLocation.getBlockedEntrances().block(Direction.NORTH);
    corridorLocation.getBlockedEntrances().block(Direction.SOUTH);
    world.addLocation(corridorLocation, corridorPoint);
    Point roomPoint = new Point(corridorPoint, direction);
    if (world.alreadyHasLocationAt(roomPoint)) {
      DungeonLogger.warning("Found an existing location when attempting to expand a Dungeon at " + roomPoint + ".");
    }
    Location roomLocation = new Location(getRandomLocationPreset(Type.DUNGEON_ROOM), world, roomPoint);
    roomLocation.getBlockedEntrances().block(Direction.UP);
    roomLocation.getBlockedEntrances().block(Direction.NORTH);
    roomLocation.getBlockedEntrances().block(Direction.DOWN);
    roomLocation.getBlockedEntrances().block(Direction.SOUTH);
    roomLocation.getBlockedEntrances().block(direction);
    world.addLocation(roomLocation, roomPoint);
  }

}
