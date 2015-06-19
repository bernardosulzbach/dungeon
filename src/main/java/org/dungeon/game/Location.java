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

import org.dungeon.entity.creatures.Creature;
import org.dungeon.entity.items.Item;
import org.dungeon.entity.items.ItemFactory;
import org.dungeon.entity.items.LocationInventory;
import org.dungeon.io.DLogger;
import org.dungeon.util.Percentage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * The Location class that defines a Location of a World.
 */
public final class Location implements Serializable {

  private final ID id;
  private final String type;
  private final Name name;
  private final LocationDescription description;
  private final BlockedEntrances blockedEntrances;
  private final List<Creature> creatures;
  private final List<Spawner> spawners;
  private final LocationInventory items;
  private final Percentage lightPermittivity;
  private final World world;

  public Location(LocationPreset preset, World world) {
    this.id = preset.getID();
    this.type = preset.getType();
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
    for (Entry<ID, Percentage> entry : preset.getItems()) {
      if (Random.roll(entry.getValue())) {
        Item item = ItemFactory.makeItem(entry.getKey(), world.getWorldDate());
        if (item != null) {
          this.addItem(item);
        } else {
          DLogger.warning("ItemBlueprint not found: " + entry.getKey().toString());
        }
      }
    }
  }

  public ID getID() {
    return id;
  }

  public String getType() {
    return type;
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
   * Returns the luminosity of the Location. This value depends on the World luminosity and the Location's specific
   * light permittivity.
   */
  public Percentage getLuminosity() {
    return getLightPermittivity().multiply(getWorld().getPartOfDay().getLuminosity());
  }

  public List<Creature> getCreatures() {
    return creatures;
  }

  public LocationInventory getInventory() {
    return items;
  }

  public List<Item> getItemList() {
    return items.getItems();
  }

  public int getCreatureCount() {
    return creatures.size();
  }

  public int getCreatureCount(ID id) {
    int count = 0;
    for (Creature creature : creatures) {
      if (creature.getID().equals(id)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Adds a Creature to this Location's Creature Collection.
   * Also sets the location attribute of the specified Creature to this Location.
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

  @Override
  public String toString() {
    return name.getSingular();
  }

}
