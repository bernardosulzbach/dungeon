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
import org.mafagafogigante.dungeon.io.JsonObjectFactory;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.awt.Color;
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

  private static final LocationPresetStore locationPresetStore = new LocationPresetStore();

  private final Map<Id, LocationPreset> idLocationPresetMap = new HashMap<>();
  private final Map<Type, List<LocationPreset>> typeLocationPresetMap = new HashMap<>();

  private LocationPresetStore() {
    loadLocationPresets();
  }

  private static Color colorFromJsonArray(JsonArray color) {
    return new Color(color.get(0).asInt(), color.get(1).asInt(), color.get(2).asInt());
  }

  public static LocationPresetStore getLocationPresetStore() {
    return locationPresetStore;
  }

  private void loadLocationPresets() {
    JsonObject jsonObject = JsonObjectFactory.makeJsonObject("locations.json");
    for (JsonValue jsonValue : jsonObject.get("locations").asArray()) {
      JsonObject presetObject = jsonValue.asObject();
      Id id = new Id(presetObject.get("id").asString());
      Type type = Type.valueOf(presetObject.get("type").asString());
      Name name = NameFactory.fromJsonObject(presetObject.get("name").asObject());
      LocationPreset preset = new LocationPreset(id, type, name);
      char symbol = presetObject.get("symbol").asString().charAt(0);
      preset.setDescription(new LocationDescription(symbol, colorFromJsonArray(presetObject.get("color").asArray())));
      preset.getDescription().setInfo(presetObject.get("info").asString());
      preset.setBlobSize(presetObject.get("blobSize").asInt());
      preset.setLightPermittivity(presetObject.get("lightPermittivity").asDouble());
      if (presetObject.get("spawners") != null) {
        for (JsonValue spawnerValue : presetObject.get("spawners").asArray()) {
          JsonObject spawner = spawnerValue.asObject();
          String spawnerId = spawner.get("id").asString();
          int population = spawner.get("population").asInt();
          int delay = spawner.get("delay").asInt();
          preset.addSpawner(new SpawnerPreset(spawnerId, population, delay));
        }
      }
      if (presetObject.get("items") != null) {
        for (JsonValue itemValue : presetObject.get("items").asArray()) {
          JsonObject item = itemValue.asObject();
          String itemId = item.get("id").asString();
          double probability = item.get("probability").asDouble();
          preset.addItem(itemId, probability);
        }
      }
      if (presetObject.get("blockedEntrances") != null) {
        for (JsonValue abbreviation : presetObject.get("blockedEntrances").asArray()) {
          preset.block(Direction.fromAbbreviation(abbreviation.asString()));
        }
      }
      addLocationPreset(preset);
    }
    DungeonLogger.info("Loaded " + getSize() + " location presets.");
  }

  /**
   * Adds a LocationPreset to the store.
   */
  private void addLocationPreset(LocationPreset preset) {
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

  private int getSize() {
    return idLocationPresetMap.size();
  }

  @Override
  public String toString() {
    Set<Type> types = typeLocationPresetMap.keySet();
    return String.format("LocationPresetStore with %d presets of the following types %s.", getSize(), types);
  }

}
