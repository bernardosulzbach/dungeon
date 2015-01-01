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
final class LocationPreset extends Preset {

  final ID id;
  private final String name;
  private final BlockedEntrances blockedEntrances;
  private final ArrayList<SpawnerPreset> spawners;
  private final ArrayList<ItemFrequencyPair> items;
  private Percentage lightPermittivity;

  LocationPreset(String id, String name) {
    this.id = new ID(id);
    this.name = name;
    blockedEntrances = new BlockedEntrances();
    spawners = new ArrayList<SpawnerPreset>();
    items = new ArrayList<ItemFrequencyPair>();
  }

  public LocationPreset addSpawner(SpawnerPreset spawner) {
    if (!isLocked()) {
      this.spawners.add(spawner);
    }
    return this;
  }

  public LocationPreset addItem(String id, Double likelihood) {
    if (!isLocked()) {
      this.items.add(new ItemFrequencyPair(new ID(id), likelihood));
    }
    return this;
  }

  public void setLightPermittivity(double lightPermittivity) {
    if (!isLocked()) {
      this.lightPermittivity = new Percentage(lightPermittivity);
    }
  }

  /**
   * Block exiting and entering into the location by a given direction.
   *
   * @param direction a Direction to be blocked.
   */
  public LocationPreset block(Direction direction) {
    if (!isLocked()) {
      blockedEntrances.block(direction);
    }
    return this;
  }

  public String getName() {
    return name;
  }

  public BlockedEntrances getBlockedEntrances() {
    return new BlockedEntrances(blockedEntrances);
  }

  public List<SpawnerPreset> getSpawners() {
    return spawners;
  }

  public List<ItemFrequencyPair> getItems() {
    return items;
  }

  public Percentage getLightPermittivity() {
    return lightPermittivity;
  }

  void finish() {
    if (!isLocked()) {
      spawners.trimToSize();
      items.trimToSize();
      lock();
    }
  }

}
