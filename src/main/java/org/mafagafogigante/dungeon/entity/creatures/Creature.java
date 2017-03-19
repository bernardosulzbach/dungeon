package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.entity.Entity;
import org.mafagafogigante.dungeon.entity.LightSource;
import org.mafagafogigante.dungeon.entity.Luminosity;
import org.mafagafogigante.dungeon.entity.TagSet;
import org.mafagafogigante.dungeon.entity.items.CreatureInventory;
import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.game.Location;
import org.mafagafogigante.dungeon.logging.DungeonLogger;
import org.mafagafogigante.dungeon.stats.CauseOfDeath;
import org.mafagafogigante.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The Creature class.
 */
public class Creature extends Entity {

  private final int attack;
  private final AttackAlgorithmId attackAlgorithmId;
  private final TagSet<Tag> tagSet;
  private final CreatureInventory inventory;
  private final LightSource lightSource;
  private final CreatureHealth health;
  private final Dropper dropper;
  private final Observer observer = new Observer(this);
  private Item weapon;
  private Location location;

  /**
   * What caused the death of this creature. If getHealth().isAlive() evaluates to true, this should be null.
   */
  private CauseOfDeath causeOfDeath;

  /**
   * Constructs a Creature from the provided CreaturePreset. Most of the application code should obtain new creatures
   * from the CreatureFactory.
   */
  public Creature(CreaturePreset preset) {
    super(preset);
    health = CreatureHealth.makeCreatureHealth(preset.getHealth(), this);
    attack = preset.getAttack();
    tagSet = TagSet.copyTagSet(preset.getTagSet());
    attackAlgorithmId = preset.getAttackAlgorithmId();
    inventory = new CreatureInventory(this, preset.getInventoryItemLimit(), preset.getInventoryWeightLimit());
    lightSource = new LightSource(preset.getLuminosity());
    dropper = new Dropper(this, preset.getDropList());
  }

  public boolean hasTag(Tag tag) {
    return tagSet.hasTag(tag);
  }

  public CreatureHealth getHealth() {
    return health;
  }

  Dropper getDropper() {
    return dropper;
  }

  public int getAttack() {
    return attack;
  }

  public CreatureInventory getInventory() {
    return inventory;
  }

  @Override
  public Luminosity getLuminosity() {
    if (hasWeapon()) {
      double luminosityFromWeapon = getWeapon().getLuminosity().toPercentage().toDouble();
      double luminosityFromLightSource = lightSource.getLuminosity().toPercentage().toDouble();
      return new Luminosity(new Percentage(luminosityFromWeapon + luminosityFromLightSource));
    } else {
      return lightSource.getLuminosity();
    }
  }

  public LightSource getLightSource() {
    return lightSource;
  }

  public Item getWeapon() {
    return weapon;
  }

  /**
   * Sets an Item as the currently equipped weapon. The Item must be in this Creature's inventory and have the WEAPON
   * tag.
   *
   * @param weapon an Item that must be in this Creature's inventory and have the WEAPON tag
   */
  public void setWeapon(Item weapon) {
    if (inventory.hasItem(weapon)) {
      if (weapon.hasTag(Item.Tag.WEAPON)) {
        this.weapon = weapon;
      } else {
        DungeonLogger.warning(String.format("Tried to equip %s (no WEAPON tag) on %s.", weapon.getName(), getName()));
      }
    } else {
      DungeonLogger.warning("Tried to equip an Item that is not in the inventory of " + getName() + ".");
    }
  }

  /**
   * Unequips the currently equipped weapon.
   */
  public void unsetWeapon() {
    this.weapon = null;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  /**
   * Hits the specified target Creature.
   *
   * @param target the target
   */
  public void hit(Creature target) {
    AttackAlgorithms.renderAttack(this, target);
  }

  boolean hasWeapon() {
    return getWeapon() != null;
  }

  /**
   * Effectively drops an item.
   *
   * @param item the Item to be dropped
   * @throws IllegalStateException if the item is not in the creature's inventory
   */
  void dropItem(Item item) {
    if (getInventory().hasItem(item)) {
      getInventory().removeItem(item);
      getLocation().addItem(item);
    } else {
      throw new IllegalStateException("Item should be in the creature's inventory");
    }
  }

  public AttackAlgorithmId getAttackAlgorithmId() {
    return attackAlgorithmId;
  }

  /**
   * Retrieves what caused the death of this creature. If getHealth().isAlive() evaluates to true, this method returns
   * null.
   */
  public CauseOfDeath getCauseOfDeath() {
    return causeOfDeath;
  }

  /**
   * Sets what caused the death of this creature. Should be called only once and after the creature is dead.
   */
  public void setCauseOfDeath(@NotNull CauseOfDeath causeOfDeath) {
    if (this.causeOfDeath != null) {
      throw new IllegalStateException("Creature already has a CauseOfDeath");
    } else if (getHealth().isAlive()) {
      throw new IllegalStateException("Creature is still alive");
    } else {
      this.causeOfDeath = causeOfDeath;
    }
  }

  /**
   * Returns a List with all the items this Creature dropped when it died. If this Creature is still alive, returns an
   * empty list.
   *
   * @return a List with the Items this Creature dropped when it died
   */
  @NotNull
  public List<Item> getDroppedItemsList() {
    return getDropper().getDroppedItemsList();
  }

  @Override
  public String toString() {
    return getName().getSingular();
  }

  boolean canSeeTheSky() {
    return getLocation().getPoint().getZ() >= 0;
  }

  /**
   * Checks if the Hero can see a given Entity based on the luminosity of the Location the Hero is in and on the
   * visibility of the specified Entity.
   */
  boolean canSee(Entity entity) {
    // The Hero is always able to find himself.
    return entity == this || entity.getVisibilityCriteria().isMetBy(observer);
  }

  <T extends Entity> List<T> filterByVisibility(List<T> list) {
    List<T> visible = new ArrayList<>();
    for (T entity : list) {
      if (canSee(entity)) {
        visible.add(entity);
      }
    }
    return visible;
  }

  public enum Tag {MILKABLE, CORPSE}

}
