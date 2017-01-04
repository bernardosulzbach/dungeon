package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.Percentage;

import java.io.Serializable;

/**
 * The weapon component of some items.
 */
public class WeaponComponent implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final int damage;
  private final Percentage hitRate;
  private final int integrityDecrementOnHit;

  /**
   * Constructs a new WeaponComponent.
   */
  public WeaponComponent(int damage, Percentage hitRate, int integrityDecrementOnHit) {
    this.damage = damage;
    this.hitRate = hitRate;
    this.integrityDecrementOnHit = integrityDecrementOnHit;
  }

  public int getDamage() {
    return damage;
  }

  public Percentage getHitRate() {
    return hitRate;
  }

  public int getIntegrityDecrementOnHit() {
    return integrityDecrementOnHit;
  }

}
