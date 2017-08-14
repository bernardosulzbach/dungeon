package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

public class WeaponEnchantment implements Enchantment, Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final String name;
  private final DamageAmount amount;

  WeaponEnchantment(String name, DamageAmount amount) {
    this.name = name;
    this.amount = amount;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return "+" + amount.getDescription();
  }

  @Override
  public void modifyAttackDamage(Damage damage) {
    damage.getAmounts().add(amount);
  }

}
