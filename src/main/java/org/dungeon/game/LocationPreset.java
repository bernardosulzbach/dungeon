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

import org.dungeon.util.Percentage;

import java.util.ArrayList;
import java.util.List;

/**
 * The LocationPreset class that serves as a recipe for Locations.
 */
public final class LocationPreset extends Entity {

  private final BlockedEntrances blockedEntrances = new BlockedEntrances();
  private final ArrayList<SpawnerPreset> spawners = new ArrayList<SpawnerPreset>();
  private final ArrayList<ItemFrequencyPair> items = new ArrayList<ItemFrequencyPair>();
  private Percentage lightPermittivity;
  private int blobSize;

  LocationPreset(String id, String type, String name) {
    super(new ID(id), type, name);
  }

  public List<SpawnerPreset> getSpawners() {
    return spawners;
  }

  /**
   * Adds a Spawner to this Location based on a SpawnerPreset.
   *
   * @param preset the SpawnerPreset
   * @return a reference to this LocationPreset
   */
  public LocationPreset addSpawner(SpawnerPreset preset) {
    this.spawners.add(preset);
    return this;
  }

  public List<ItemFrequencyPair> getItems() {
    return items;
  }

  /**
   * Adds an Item to this Location based on an ItemFrequencyPair.
   *
   * @param id         the ID string of the item
   * @param likelihood the likelihood of the item appearing
   * @return a reference to this LocationPreset
   */
  public LocationPreset addItem(String id, Double likelihood) {
    this.items.add(new ItemFrequencyPair(new ID(id), likelihood));
    return this;
  }

  public BlockedEntrances getBlockedEntrances() {
    return new BlockedEntrances(blockedEntrances);
  }

  /**
   * Blocks exiting and entering into the location by a given direction.
   *
   * @param direction a Direction to be blocked.
   */
  public LocationPreset block(Direction direction) {
    blockedEntrances.block(direction);
    return this;
  }

  public Percentage getLightPermittivity() {
    return lightPermittivity;
  }

  public void setLightPermittivity(double lightPermittivity) {
    this.lightPermittivity = new Percentage(lightPermittivity);
  }

  public int getBlobSize() {
    return blobSize;
  }

  public void setBlobSize(int blobSize) {
    this.blobSize = blobSize;
  }

  /**
   * Trims all ArrayLists to size, reducing the memory used by this LocationPreset.
   * <p/>
   * This method should be called after all modifications have been made.
   */
  void finish() {
    spawners.trimToSize();
    items.trimToSize();
  }

}
