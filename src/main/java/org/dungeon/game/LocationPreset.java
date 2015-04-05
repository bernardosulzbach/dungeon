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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The LocationPreset class that serves as a recipe for Locations.
 */
public final class LocationPreset implements Serializable {

  private final ID id;
  private final String type;
  private final Name name;
  private final BlockedEntrances blockedEntrances = new BlockedEntrances();
  private final List<SpawnerPreset> spawners = new ArrayList<SpawnerPreset>();
  private final Map<ID, Percentage> items = new HashMap<ID, Percentage>();
  private Percentage lightPermittivity;
  private int blobSize;

  LocationPreset(ID id, String type, Name name) {
    this.id = id;
    this.type = type;
    this.name = name;
  }

  public ID getID() {
    return id;
  }

  public String getType() {
    return type;
  }

  public String getName() {
    return name.getName();
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

  public Set<Entry<ID, Percentage>> getItems() {
    return items.entrySet();
  }

  /**
   * Adds an Item to this Location based on an ItemFrequencyPair.
   *
   * @param id         the ID string of the item
   * @param likelihood the likelihood of the item appearing
   * @return a reference to this LocationPreset
   */
  public LocationPreset addItem(String id, Double likelihood) {
    items.put(new ID(id), new Percentage(likelihood));
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

}
