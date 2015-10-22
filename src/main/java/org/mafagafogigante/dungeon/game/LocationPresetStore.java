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

package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.game.LocationPreset.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A store for all LocationPresets.
 */
public class LocationPresetStore {

  private final Map<Id, LocationPreset> idLocationPresetMap = new HashMap<Id, LocationPreset>();
  private final Map<Type, List<LocationPreset>> typeLocationPresetMap = new HashMap<Type, List<LocationPreset>>();

  LocationPresetStore() {
  }

  /**
   * Adds a LocationPreset to the store.
   */
  public void addLocationPreset(LocationPreset preset) {
    idLocationPresetMap.put(preset.getId(), preset);
    if (!typeLocationPresetMap.containsKey(preset.getType())) {
      typeLocationPresetMap.put(preset.getType(), new ArrayList<LocationPreset>());
    }
    typeLocationPresetMap.get(preset.getType()).add(preset);
  }

  public Collection<LocationPreset> getAllPresets() {
    return idLocationPresetMap.values();
  }

  List<LocationPreset> getLocationPresetsByType(Type type) {
    return typeLocationPresetMap.get(type);
  }

  public int getSize() {
    return idLocationPresetMap.size();
  }

  @Override
  public String toString() {
    Set<Type> types = typeLocationPresetMap.keySet();
    return String.format("LocationPresetStore with %d presets of the following types %s.", getSize(), types);
  }

}
