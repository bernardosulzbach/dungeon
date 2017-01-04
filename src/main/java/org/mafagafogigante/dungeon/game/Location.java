package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.entity.Entity;
import org.mafagafogigante.dungeon.entity.Luminosity;
import org.mafagafogigante.dungeon.entity.TagSet;
import org.mafagafogigante.dungeon.entity.creatures.Creature;
import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.entity.items.ItemFactory;
import org.mafagafogigante.dungeon.entity.items.LocationInventory;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * The Location class that defines a Location of a World.
 */
public class Location implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final Id id;
  private final Name name;
  private final LocationDescription description;
  private final BlockedEntrances blockedEntrances;
  private final List<Creature> creatures;
  private final List<Spawner> spawners;
  private final TagSet<Tag> tagSet;
  private final LocationInventory items;
  private final Percentage lightPermittivity;
  private final World world;
  private final Point point;

  /**
   * Constructs a new location for the specified world based on the provided preset.
   *
   * <p>The creation date of the items in this location is the world date at the time this location was created.
   *
   * @param preset the LocationPreset object
   * @param world the World object
   */
  public Location(@NotNull LocationPreset preset, @NotNull World world, @NotNull Point point) {
    this.id = preset.getId();
    this.name = preset.getName();
    this.description = preset.getDescription();
    this.world = world;
    this.point = point;
    this.blockedEntrances = preset.getBlockedEntrances();
    this.lightPermittivity = preset.getLightPermittivity();
    this.creatures = new ArrayList<>();
    this.spawners = new ArrayList<>(preset.getSpawners().size());
    for (SpawnerPreset spawner : preset.getSpawners()) {
      spawners.add(new Spawner(spawner, this));
    }
    this.tagSet = TagSet.copyTagSet(preset.getTagSet());
    this.items = new LocationInventory();
    ItemFactory itemFactory = getWorld().getItemFactory();
    for (Entry<Id, Percentage> entry : preset.getItems()) {
      if (Random.roll(entry.getValue())) {
        Id id = entry.getKey();
        if (itemFactory.canMakeItem(id)) {
          this.addItem(itemFactory.makeItem(id, world.getWorldDate()));
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

  /**
   * Refreshes all the Spawners of this location.
   */
  void refreshSpawners() {
    for (Spawner spawner : spawners) {
      spawner.refresh();
    }
  }

  public TagSet<Tag> getTagSet() {
    return tagSet;
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
    List<Entity> entities = new ArrayList<>();
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

  /**
   * Returns the number of creatures in this Location.
   */
  int getCreatureCount(Id id) {
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

  /**
   * Removes a creature of this Location.
   */
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

  /**
   * Returns at which point of the world this location is in it is.
   */
  public Point getPoint() {
    return point;
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
  void refreshItems() {
    getInventory().refreshItems();
    for (Creature creature : creatures) {
      creature.getInventory().refreshItems();
    }
  }

  @Override
  public String toString() {
    return name.getSingular();
  }

  public enum Tag {FISHABLE}

}
