package org.mafagafogigante.dungeon.entity;

public interface Enchantment {
  String getName();

  String getDescription();

  void modifyAttackDamage(Damage damage);
}
