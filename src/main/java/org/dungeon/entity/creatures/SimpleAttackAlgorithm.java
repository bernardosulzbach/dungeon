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

package org.dungeon.entity.creatures;

import org.dungeon.entity.items.Item;
import org.dungeon.game.Random;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.TypeOfCauseOfDeath;
import org.dungeon.util.Percentage;

import org.jetbrains.annotations.NotNull;

/**
 * A simple implementation of the AttackAlgorithm interface.
 *
 * <p>This class has two non-final protected methods getHitRate and getCriticalChance that may be overridden in order to
 * get simple and foolproof behavior modification. Reasonable defaults are provided if these methods are not
 * overridden.
 */
class SimpleAttackAlgorithm implements AttackAlgorithm {

  private static final Percentage DEFAULT_UNARMED_HIT_RATE = new Percentage(0.9);
  private static final Percentage DEFAULT_CRITICAL_CHANCE = new Percentage(0.1);

  private static boolean isEquippingUnbrokenWeapon(@NotNull Creature attacker) {
    return attacker.hasWeapon() && !attacker.getWeapon().isBroken();
  }

  /**
   * Retrieves the hit rate of a creature. The implementation provided by SimpleAttackAlgorithm uses a default hit rate
   * if the creature is not equipping an unbroken weapon. Otherwise the hit rate of the weapon is used.
   */
  Percentage getHitRate(@NotNull Creature creature) {
    if (isEquippingUnbrokenWeapon(creature)) {
      return creature.getWeapon().getWeaponComponent().getHitRate();
    } else {
      return DEFAULT_UNARMED_HIT_RATE;
    }
  }

  /**
   * Retrieves the critical chance of a creature. The implementation provided by SimpleAttackAlgorithm just uses a 10%
   * default critical chance.
   */
  Percentage getCriticalChance(@NotNull Creature creature) {
    return DEFAULT_CRITICAL_CHANCE;
  }

  public void renderAttack(@NotNull Creature attacker, @NotNull Creature defender) {
    if (Random.roll(getHitRate(attacker))) {
      int damage = attacker.getAttack();
      boolean attackerIsEquippingUnbrokenWeapon = isEquippingUnbrokenWeapon(attacker);
      if (attackerIsEquippingUnbrokenWeapon) {
        damage += attacker.getWeapon().getWeaponComponent().getDamage();
      }
      boolean isCriticalHit = Random.roll(getCriticalChance(attacker));
      if (isCriticalHit) {
        damage *= 2;
      }
      // Decrement the health of the defender.
      defender.getHealth().decrementBy(damage);
      AttackAlgorithmWriter.writeInflictedDamage(attacker, damage, defender, isCriticalHit);
      // Respect the contract: If the defender is dead, set its cause of death.
      if (defender.getHealth().isDead()) {
        if (attackerIsEquippingUnbrokenWeapon) {
          defender.setCauseOfDeath(new CauseOfDeath(TypeOfCauseOfDeath.WEAPON, attacker.getWeapon().getId()));
        } else {
          defender.setCauseOfDeath(CauseOfDeath.getUnarmedCauseOfDeath());
        }
      }
      // Decrement the integrity of the weapon.
      if (attackerIsEquippingUnbrokenWeapon) {
        Item weapon = attacker.getWeapon();
        weapon.decrementIntegrityByHit();
        if (weapon.isBroken()) {
          AttackAlgorithmWriter.writeWeaponBreak(weapon);
        }
      }
    } else {
      AttackAlgorithmWriter.writeMiss(attacker);
    }
  }

}
