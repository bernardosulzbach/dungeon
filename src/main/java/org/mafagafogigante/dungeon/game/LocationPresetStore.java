package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.game.LocationPreset.Type;
import org.mafagafogigante.dungeon.io.DungeonResource;
import org.mafagafogigante.dungeon.io.JsonObjectFactory;
import org.mafagafogigante.dungeon.io.ResourceNameResolver;
import org.mafagafogigante.dungeon.io.TagSetParser;
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
 * A class that stores and provides easy retrieval of a set of LocationPresets. Application code should access this
 * class through the public getDefaultLocationPresetStore() method.
 */
public final class LocationPresetStore {

  private static final LocationPresetStore defaultLocationPresetStore = new LocationPresetStore();
  private static boolean defaultLocationPresetStoreIsUninitialized = true;

  private final Map<Id, LocationPreset> idLocationPresetMap = new HashMap<>();
  private final Map<Type, List<LocationPreset>> typeLocationPresetMap = new HashMap<>();

  private LocationPresetStore() {
  }

  private static Color colorFromJsonArray(JsonArray color) {
    return new Color(color.get(0).asInt(), color.get(1).asInt(), color.get(2).asInt());
  }

  /**
   * Returns the default LocationPresetStore, initializing it if it hasn't already been initialized.
   */
  public static LocationPresetStore getDefaultLocationPresetStore() {
    if (defaultLocationPresetStoreIsUninitialized) {
      defaultLocationPresetStore.loadLocationPresets();
      defaultLocationPresetStoreIsUninitialized = false;
    }
    return defaultLocationPresetStore;
  }

  private void loadLocationPresets() {
    String filename = ResourceNameResolver.resolveName(DungeonResource.LOCATIONS);
    JsonObject jsonObject = JsonObjectFactory.makeJsonObject(filename);
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
      preset.setTagSet(new TagSetParser<>(Location.Tag.class, presetObject.get("tags")).parse());
      if (presetObject.get("spawners") != null) {
        for (JsonValue spawnerValue : presetObject.get("spawners").asArray()) {
          JsonObject spawner = spawnerValue.asObject();
          String spawnerId = spawner.get("id").asString();
          JsonObject population = spawner.get("population").asObject();
          int minimumPopulation = population.get("minimum").asInt();
          int maximumPopulation = population.get("maximum").asInt();
          int delay = spawner.get("delay").asInt();
          preset.addSpawner(new SpawnerPreset(spawnerId, minimumPopulation, maximumPopulation, delay));
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
   * Adds a LocationPreset to the store. Throws an IllegalArgumentException if there is already a preset registered with
   * the same Id.
   */
  private void addLocationPreset(LocationPreset preset) {
    if (idLocationPresetMap.containsKey(preset.getId())) {
      throw new IllegalArgumentException("idLocationPresetMap already contains a preset with the Id " + preset.getId());
    }
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
