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

import org.dungeon.entity.Entity;
import org.dungeon.entity.Luminosity;
import org.dungeon.entity.creatures.Creature;
import org.dungeon.entity.items.Item;
import org.dungeon.entity.items.ItemFactory;
import org.dungeon.entity.items.LocationInventory;
import org.dungeon.logging.DungeonLogger;
import org.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * The Location class that defines a Location of a World.
 */
public final class Location implements Serializable {

  private final Id id;
  private final Name name;
  private final LocationDescription description;
  private final BlockedEntrances blockedEntrances;
  private final List<Creature> creatures;
  private final List<Spawner> spawners;
  private final LocationInventory items;
  private final Percentage lightPermittivity;
  private final World world;

  /**
   * Constructs a new location for the specified world based on the provided preset.
   *
   * <p>The creation date of the items in this location is the world date at the time this location was created.
   *
   * @param preset the LocationPreset object
   * @param world the World object
   */
  public Location(@NotNull LocationPreset preset, @NotNull World world) {
    this.id = preset.getId();
    this.name = preset.getName();
    this.description = preset.getDescription();
    this.world = world;
    this.blockedEntrances = preset.getBlockedEntrances();
    this.lightPermittivity = preset.getLightPermittivity();
    this.creatures = new ArrayList<Creature>();
    this.spawners = new ArrayList<Spawner>(preset.getSpawners().size());
    for (SpawnerPreset spawner : preset.getSpawners()) {
      spawners.add(new Spawner(spawner, this));
    }
    this.items = new LocationInventory();
    for (Entry<Id, Percentage> entry : preset.getItems()) {
      if (Random.roll(entry.getValue())) {
        Item item = ItemFactory.makeItem(entry.getKey(), world.getWorldDate());
        if (item != null) {
          this.addItem(item);
        } else {
          DungeonLogger.warning("Item preset not found: " + entry.getKey().toString() + ".");
        }
      }
    }
  }

  public Id getId() {
    return id;
  }

  public Name getName() {
    return name;
  }

  public LocationDescription getDescription() {
    return description;
  }

  public void refreshSpawners() {
    for (Spawner spawner : spawners) {
      spawner.refresh();
    }
  }

  public Percentage getLightPermittivity() {
    return lightPermittivity;
  }

  /**
   * Returns the luminosity of the Location. This value depends on the World luminosity, on the Location's specific
   * light permittivity and on the luminosity of the Entities in this location.
   */
  public Luminosity getLuminosity() {
    // Light permittivity is only applied to the luminosity that comes from the sky.
    Percentage fromEntities = Luminosity.resultantLuminosity(getEntities()).toPercentage();
    Percentage fromTheWorld = getLightPermittivity().multiply(getWorld().getPartOfDay().getLuminosity().toPercentage());
    return new Luminosity(new Percentage(Math.min(fromEntities.toDouble() + fromTheWorld.toDouble(), 1.0)));
  }

  public List<Creature> getCreatures() {
    return creatures;
  }

  public LocationInventory getInventory() {
    return items;
  }

  /**
   * Returns a list with all the entities in the current location.
   */
  private List<Entity> getEntities() {
    List<Entity> entities = new ArrayList<Entity>();
    entities.addAll(getCreatures());
    entities.addAll(getItemList());
    return entities;
  }

  public List<Item> getItemList() {
    return items.getItems();
  }

  public int getCreatureCount() {
    return creatures.size();
  }

  public int getCreatureCount(Id id) {
    int count = 0;
    for (Creature creature : creatures) {
      if (creature.getId().equals(id)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Adds a Creature to this Location's Creature Collection. Also sets the location attribute of the specified Creature
   * to this Location.
   *
   * @param creature a Creature object
   */
  public void addCreature(Creature creature) {
    creature.setLocation(this);
    creatures.add(creature);
  }

  public void addItem(Item item) {
    items.addItem(item);
  }

  public void removeItem(Item item) {
    items.removeItem(item);
  }

  public void removeCreature(Creature creature) {
    for (Spawner spawner : spawners) {
      spawner.notifyKill(creature);
    }
    // The creature must be removed after the spawns are notified.
    creatures.remove(creature);
  }

  public World getWorld() {
    return world;
  }

  public BlockedEntrances getBlockedEntrances() {
    return blockedEntrances;
  }

  public boolean isBlocked(Direction direction) {
    return blockedEntrances.isBlocked(direction);
  }

  /**
   * Refreshes all the items in this location's inventory and all the items in the inventories of the creatures in this
   * location.
   */
  public void refreshItems() {
    getInventory().refreshItems();
    for (Creature creature : creatures) {
      creature.getInventory().refreshItems();
    }
  }

  @Override
  public String toString() {
    return name.getSingular();
  }

}
