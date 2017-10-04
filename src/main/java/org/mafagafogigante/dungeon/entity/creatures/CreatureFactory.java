package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.entity.items.CreatureInventory.SimulationResult;
import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.entity.items.ItemFactory;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.World;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.logging.DungeonLogger;
import org.mafagafogigante.dungeon.stats.Statistics;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The factory of creatures.
 */
public final class CreatureFactory implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final Map<Id, CreaturePreset> creaturePresets = new HashMap<>();

  /**
   * Constructs an ItemFactory from one or more CreaturePresetFactories.
   */
  public CreatureFactory(@NotNull CreaturePresetFactory... creaturePresetFactories) {
    for (CreaturePresetFactory creaturePresetFactory : creaturePresetFactories) {
      addAllPresets(creaturePresetFactory.getCreaturePresets());
    }
  }

  private static void equipWeapon(Creature creature, CreaturePreset preset) {
    if (preset.getWeaponId() != null) {
      // Get the weapon from the creature's inventory.
      for (Item item : creature.getInventory().getItems()) {
        if (item.getId().equals(preset.getWeaponId())) {
          creature.setWeapon(item);
          break;
        }
      }
      if (!creature.hasWeapon()) { // Did not found a suitable Item in the inventory.
        String format = "%s not found in the inventory of %s!";
        DungeonLogger.warning(String.format(format, preset.getWeaponId(), creature.getId()));
      }
    }
  }

  /**
   * Iterates over all presets of a Collection, adding them to this factory after they are validated.
   */
  private void addAllPresets(Collection<CreaturePreset> presets) {
    for (CreaturePreset preset : presets) {
      Id id = preset.getId();
      if (creaturePresets.containsKey(id)) {
        throw new IllegalArgumentException("factory already contains a preset with the Id " + preset.getId() + ".");
      }
      creaturePresets.put(id, preset);
    }
  }

  /**
   * Gives a Creature all the Items defined in the corresponding CreaturePreset and equips its weapon, if there is one.
   *
   * @param creature the Creature
   */
  private void giveItems(Creature creature, World world) {
    CreaturePreset preset = creaturePresets.get(creature.getId());
    ItemFactory itemFactory = world.getItemFactory();
    for (Id itemId : preset.getItems()) {
      if (itemFactory.canMakeItem(itemId)) {
        Item item = itemFactory.makeItem(itemId, world.getWorldDate());
        SimulationResult result = creature.getInventory().simulateItemAddition(item);
        if (result == SimulationResult.SUCCESSFUL) {
          creature.getInventory().addItem(item);
        } else {
          DungeonLogger.warning("Could not add " + itemId + " to " + creature.getId() + ". Reason: " + result + ".");
        }
      }
    }
    equipWeapon(creature, preset);
  }

  public Collection<CreaturePreset> getPresets() {
    return creaturePresets.values();
  }

  /**
   * Attempts to create a creature from the CreaturePreset specified by an ID. Returns null if no preset was found.
   *
   * <p>Also adds the new creature to the statistics.
   */
  public Creature makeCreature(Id id, World world) {
    CreaturePreset preset = creaturePresets.get(id);
    if (preset != null) {
      Creature creature = new Creature(preset);
      Game.getGameState().getStatistics().getWorldStatistics().addSpawn(creature.getName().getSingular());
      giveItems(creature, world);
      return creature;
    } else {
      return null;
    }
  }

  /**
   * Creates the Hero.
   *
   * @param date the Date when the Items the Hero has were created
   * @return the Hero object
   */
  public Hero makeHero(Date date, World world, Statistics statistics) {
    DateOfBirthGenerator dateOfBirthGenerator = new DateOfBirthGenerator(date, 30);
    Date dateOfBirth = dateOfBirthGenerator.generateDateOfBirth();
    Hero hero = new Hero(creaturePresets.get(new Id("HERO")), statistics, dateOfBirth);
    giveItems(hero, world);
    return hero;
  }

}
