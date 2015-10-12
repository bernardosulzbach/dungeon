/*
 * Copyright (C) 2015 Bernardo Sulzbach
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

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * A class that know how to create dungeons.
 */
class DungeonCreator implements Serializable {

  private static final MinimumBoundingRectangle minimumBoundingRectangle = new MinimumBoundingRectangle(1, 1);
  private final DungeonDistributor distributor;

  public DungeonCreator(DungeonDistributor distributor) {
    this.distributor = distributor;
  }

  public static MinimumBoundingRectangle getMinimumBoundingRectangle() {
    return minimumBoundingRectangle;
  }

  private static LocationPreset getRandomEntrancePreset() {
    LocationPresetStore locationPresetStore = GameData.getLocationPresetStore();
    List<LocationPreset> entrancePresets = locationPresetStore.getLocationPresetsByType(Type.DUNGEON_ENTRANCE);
    return Random.select(entrancePresets);
  }

  /**
   * Creates a dungeon placing the entrance at the specified point. The world should not have a location at the
   * specified point.
   */
  public void createDungeon(@NotNull World world, @NotNull Point entrance) {
    createEntrance(world, entrance);
    Location dungeonRoom = new Location(getRandomRoomPreset(), world);
    world.addLocation(dungeonRoom, new Point(entrance, Direction.DOWN));
  }

  private void createEntrance(@NotNull World world, @NotNull Point entrance) {
    if (world.alreadyHasLocationAt(entrance)) {
      throw new IllegalStateException("world has location at the specified entrance.");
    }
    Location dungeonEntrance = new Location(getRandomEntrancePreset(), world);
    world.addLocation(dungeonEntrance, entrance);
    distributor.registerDungeonEntrance(entrance);
  }

  private LocationPreset getRandomRoomPreset() {
    LocationPresetStore locationPresetStore = GameData.getLocationPresetStore();
    List<LocationPreset> entrancePresets = locationPresetStore.getLocationPresetsByType(Type.DUNGEON_ROOM);
    return Random.select(entrancePresets);
  }

}
