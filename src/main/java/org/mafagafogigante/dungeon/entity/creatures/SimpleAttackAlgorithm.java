package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.game.Random;
import org.mafagafogigante.dungeon.stats.CauseOfDeath;
import org.mafagafogigante.dungeon.stats.TypeOfCauseOfDeath;
import org.mafagafogigante.dungeon.util.Percentage;

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

  private static boolean isEquippingBrokenWeapon(@NotNull Creature creature) {
    return creature.hasWeapon() && creature.getWeapon().isBroken();
  }

  private static boolean isEquippingWorkingWeapon(@NotNull Creature creature) {
    return creature.hasWeapon() && !creature.getWeapon().isBroken();
  }

  /**
   * Retrieves the hit rate of a creature. The implementation provided by SimpleAttackAlgorithm uses a default hit rate
   * if the creature is not equipping an unbroken weapon. Otherwise the hit rate of the weapon is used.
   */
  Percentage getHitRate(@NotNull Creature creature) {
    Percentage hitRate;
    if (isEquippingWorkingWeapon(creature)) {
      hitRate = creature.getWeapon().getWeaponComponent().getHitRate();
    } else {
      hitRate = DEFAULT_UNARMED_HIT_RATE;
    }
    for (Condition condition : creature.getConditions()) {
      hitRate = condition.modifyHitRate(hitRate);
    }
    return hitRate;
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
      boolean attackerIsEquippingBrokenWeapon = isEquippingBrokenWeapon(attacker);
      boolean attackerIsEquippingWorkingWeapon = isEquippingWorkingWeapon(attacker);
      if (attackerIsEquippingWorkingWeapon) {
        damage += attacker.getWeapon().getWeaponComponent().getDamage();
      }
      boolean isCriticalHit = Random.roll(getCriticalChance(attacker));
      if (isCriticalHit) {
        damage *= 2;
      }
      // Decrement the health of the defender.
      DamageHandler.inflictDamage(attacker, defender, damage);
      AttackAlgorithmWriter.writeInflictedDamage(attacker, damage, defender, isCriticalHit);
      // Respect the contract: If the defender is dead, set its cause of death.
      if (defender.getHealth().isDead()) {
        if (attackerIsEquippingWorkingWeapon) {
          defender.setCauseOfDeath(new CauseOfDeath(TypeOfCauseOfDeath.WEAPON, attacker.getWeapon().getId()));
        } else if (attackerIsEquippingBrokenWeapon) {
          defender.setCauseOfDeath(new CauseOfDeath(TypeOfCauseOfDeath.BROKEN_WEAPON, attacker.getWeapon().getId()));
        } else {
          defender.setCauseOfDeath(CauseOfDeath.getUnarmedCauseOfDeath());
        }
      }
      // Decrement the integrity of the weapon.
      if (attackerIsEquippingWorkingWeapon) {
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
