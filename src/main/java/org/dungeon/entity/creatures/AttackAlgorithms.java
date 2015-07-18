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
import org.dungeon.game.Id;
import org.dungeon.game.Random;
import org.dungeon.skill.Skill;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.TypeOfCauseOfDeath;
import org.dungeon.util.DungeonMath;
import org.dungeon.util.Percentage;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class presents a static renderAttack method that retrieves the appropriate algorithm to render an attack.
 */
final class AttackAlgorithms {

  private static final Id UNARMED_ID = new Id("UNARMED");
  private static final Map<AttackAlgorithmId, AttackAlgorithm> ATTACK_ALGORITHM_MAP =
      new EnumMap<AttackAlgorithmId, AttackAlgorithm>(AttackAlgorithmId.class);

  static {

    final double BAT_CRITICAL_MAXIMUM_LUMINOSITY = 0.5;
    final double BAT_MAX_HIT_RATE = 0.9;
    final double BAT_MIN_HIT_RATE = 0.1;
    registerAttackAlgorithm(AttackAlgorithmId.BAT, new AttackAlgorithm() {
      @Override
      public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
        Percentage luminosity = attacker.getLocation().getLuminosity().toPercentage();
        double hitRate = DungeonMath.weightedAverage(BAT_MIN_HIT_RATE, BAT_MAX_HIT_RATE, luminosity);
        if (Random.roll(hitRate)) {
          int hitDamage = attacker.getAttack();
          boolean criticalHit = luminosity.toDouble() <= BAT_CRITICAL_MAXIMUM_LUMINOSITY;
          if (criticalHit) {
            hitDamage *= 2;
          }
          boolean healthStateChanged = defender.takeDamage(hitDamage);
          AttackAlgorithmWriter.writeInflictedDamage(attacker, hitDamage, defender, criticalHit, healthStateChanged);
        } else {
          AttackAlgorithmWriter.writeMiss(attacker);
        }
        return null;
      }
    });

    final double BEAST_HIT_RATE = 0.9;
    registerAttackAlgorithm(AttackAlgorithmId.BEAST, new AttackAlgorithm() {
      @Override
      public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
        if (Random.roll(BEAST_HIT_RATE)) {
          int hitDamage = attacker.getAttack();
          boolean healthStateChanged = defender.takeDamage(hitDamage);
          AttackAlgorithmWriter.writeInflictedDamage(attacker, hitDamage, defender, false, healthStateChanged);
        } else {
          AttackAlgorithmWriter.writeMiss(attacker);
        }
        return null;
      }
    });

    registerAttackAlgorithm(AttackAlgorithmId.CRITTER, new AttackAlgorithm() {
      @Override
      public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
        AttackAlgorithmWriter.writeCritterAttackMessage(attacker);
        return null;
      }
    });

    registerAttackAlgorithm(AttackAlgorithmId.DUMMY, new AttackAlgorithm() {
      @Override
      public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
        AttackAlgorithmWriter.writeDummyAttackMessage(attacker);
        return null;
      }
    });

    final double ORC_UNARMED_HIT_RATE = 0.95;
    final double ORC_MIN_CRIT_CHANCE = 0.1;
    final double ORC_MAX_CRIT_CHANCE = 0.5;
    registerAttackAlgorithm(AttackAlgorithmId.ORC, new AttackAlgorithm() {
      @Override
      public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
        Item weapon = attacker.getWeapon();
        int hitDamage;
        Percentage healthiness = new Percentage(attacker.getCurHealth() / (double) attacker.getMaxHealth());
        double criticalChance = DungeonMath.weightedAverage(ORC_MIN_CRIT_CHANCE, ORC_MAX_CRIT_CHANCE, healthiness);
        boolean criticalHit = Random.roll(criticalChance);
        if (weapon != null && !weapon.isBroken()) {
          if (weapon.rollForHit()) {
            hitDamage = weapon.getWeaponComponent().getDamage() + attacker.getAttack();
            weapon.decrementIntegrityByHit();
          } else {
            AttackAlgorithmWriter.writeMiss(attacker);
            return null;
          }
        } else {
          if (Random.roll(ORC_UNARMED_HIT_RATE)) {
            hitDamage = attacker.getAttack();
          } else {
            AttackAlgorithmWriter.writeMiss(attacker);
            return null;
          }
        }
        if (criticalHit) {
          hitDamage *= 2;
        }
        boolean healthStateChanged = defender.takeDamage(hitDamage);
        AttackAlgorithmWriter.writeInflictedDamage(attacker, hitDamage, defender, criticalHit, healthStateChanged);
        if (weapon != null && weapon.isBroken()) {
          AttackAlgorithmWriter.writeWeaponBreak(weapon);
        }
        return null;
      }
    });

    final double UNDEAD_UNARMED_HIT_RATE = 0.85;
    registerAttackAlgorithm(AttackAlgorithmId.UNDEAD, new AttackAlgorithm() {
      @Override
      public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
        Item weapon = attacker.getWeapon();
        int hitDamage;
        // Check that there is a weapon and that it is not broken.
        if (weapon != null && !weapon.isBroken()) {
          if (weapon.rollForHit()) {
            hitDamage = weapon.getWeaponComponent().getDamage() + attacker.getAttack();
            weapon.decrementIntegrityByHit();
          } else {
            AttackAlgorithmWriter.writeMiss(attacker);
            return null;
          }
        } else {
          if (Random.roll(UNDEAD_UNARMED_HIT_RATE)) {
            hitDamage = attacker.getAttack();
          } else {
            AttackAlgorithmWriter.writeMiss(attacker);
            return null;
          }
        }
        boolean healthStateChanged = defender.takeDamage(hitDamage);
        AttackAlgorithmWriter.writeInflictedDamage(attacker, hitDamage, defender, false, healthStateChanged);
        if (weapon != null && weapon.isBroken()) {
          AttackAlgorithmWriter.writeWeaponBreak(weapon);
        }
        return null;
      }
    });

    final double HERO_CRITICAL_CHANCE = 0.1;
    final double HERO_CRITICAL_CHANCE_UNARMED = 0.05;
    registerAttackAlgorithm(AttackAlgorithmId.HERO, new AttackAlgorithm() {
      @Override
      public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
        CauseOfDeath causeOfDeath;
        if (attacker.getSkillRotation().hasReadySkill()) {
          Skill skill = attacker.getSkillRotation().getNextSkill();
          skill.cast(attacker, defender);
          causeOfDeath = new CauseOfDeath(TypeOfCauseOfDeath.SKILL, skill.getId());
        } else {
          Item weapon = attacker.getWeapon();
          boolean criticalHit;
          int hitDamage;
          // Check that there is a weapon and that it is not broken.
          if (weapon != null && !weapon.isBroken()) {
            if (weapon.rollForHit()) {
              hitDamage = weapon.getWeaponComponent().getDamage() + attacker.getAttack();
              criticalHit = Random.roll(HERO_CRITICAL_CHANCE);
              weapon.decrementIntegrityByHit();
              causeOfDeath = new CauseOfDeath(TypeOfCauseOfDeath.WEAPON, weapon.getId());
            } else {
              AttackAlgorithmWriter.writeMiss(attacker);
              return null;
            }
          } else {
            hitDamage = attacker.getAttack();
            criticalHit = Random.roll(HERO_CRITICAL_CHANCE_UNARMED);
            causeOfDeath = new CauseOfDeath(TypeOfCauseOfDeath.UNARMED, UNARMED_ID);
          }
          if (criticalHit) {
            hitDamage *= 2;
          }
          boolean healthStateChanged = defender.takeDamage(hitDamage);
          AttackAlgorithmWriter.writeInflictedDamage(attacker, hitDamage, defender, criticalHit, healthStateChanged);
          if (weapon != null && weapon.isBroken()) {
            AttackAlgorithmWriter.writeWeaponBreak(weapon);
          }
        }
        return causeOfDeath;
      }
    });

    validateMap();

  }

  private AttackAlgorithms() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Ensure that all AttackAlgorithmsID got implemented.
   */
  private static void validateMap() {
    for (AttackAlgorithmId id : AttackAlgorithmId.values()) {
      if (!ATTACK_ALGORITHM_MAP.containsKey(id)) {
        throw new AssertionError(id + " is not mapped to an AttackAlgorithm!");
      }
    }
  }

  /**
   * Registers an AttackAlgorithm using specified AttackAlgorithmId.
   */
  private static void registerAttackAlgorithm(AttackAlgorithmId id, AttackAlgorithm algorithm) {
    if (ATTACK_ALGORITHM_MAP.containsKey(id)) {
      throw new IllegalStateException("There is an AttackAlgorithm already defined for this AttackAlgorithmId!");
    }
    ATTACK_ALGORITHM_MAP.put(id, algorithm);
  }

  /**
   * Makes the specified attacker attack the defender using the specified attack algorithm.
   *
   * <p>Returns what would be the CauseOfDeath if the attack killed the defender. If the attack algorithm is not the
   * Hero's one or even if it is but the Hero missed, null is returned.
   *
   * @param attacker the Creature that is attacking
   * @param defender the Creature that is being attacked
   * @return what would be the CauseOfDeath if the attack killed the defender
   */
  public static CauseOfDeath renderAttack(Creature attacker, Creature defender) {
    return ATTACK_ALGORITHM_MAP.get(attacker.getAttackAlgorithmId()).renderAttack(attacker, defender);
  }

}
