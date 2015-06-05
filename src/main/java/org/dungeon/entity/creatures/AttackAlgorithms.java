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
import org.dungeon.game.Engine;
import org.dungeon.game.ID;
import org.dungeon.io.DLogger;
import org.dungeon.skill.Skill;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.TypeOfCauseOfDeath;
import org.dungeon.util.Math;
import org.dungeon.util.Percentage;

import java.util.HashMap;
import java.util.Map;

/**
 * This class presents a static renderAttack method that retrieves the appropriate algorithm to render an attack.
 */
final class AttackAlgorithms {

  private static final ID UNARMED_ID = new ID("");
  private static final Map<ID, AttackAlgorithm> ATTACK_ALGORITHM_MAP = new HashMap<ID, AttackAlgorithm>();

  static {

    final double BAT_CRITICAL_MAXIMUM_LUMINOSITY = 0.5;
    final double BAT_HIT_RATE_MAX_LUMINOSITY = 0.9;
    final double BAT_HIT_RATE_MIN_LUMINOSITY = 0.1;
    registerAttackAlgorithm("BAT", new AttackAlgorithm() {
      @Override
      public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
        Percentage luminosity = attacker.getLocation().getLuminosity();
        double hitRate = Math.weightedAverage(BAT_HIT_RATE_MIN_LUMINOSITY, BAT_HIT_RATE_MAX_LUMINOSITY, luminosity);
        if (Engine.roll(hitRate)) {
          int hitDamage = attacker.getAttack();
          boolean criticalHit = luminosity.toDouble() <= BAT_CRITICAL_MAXIMUM_LUMINOSITY;
          if (criticalHit) {
            hitDamage *= 2;
          }
          boolean healthStateChanged = defender.takeDamage(hitDamage);
          AttackAlgorithmIO.printInflictedDamage(attacker, hitDamage, defender, criticalHit, healthStateChanged);
        } else {
          AttackAlgorithmIO.printMiss(attacker);
        }
        return null;
      }
    });

    final double BEAST_HIT_RATE = 0.9;
    registerAttackAlgorithm("BEAST", new AttackAlgorithm() {
      @Override
      public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
        if (Engine.roll(BEAST_HIT_RATE)) {
          int hitDamage = attacker.getAttack();
          boolean healthStateChanged = defender.takeDamage(hitDamage);
          AttackAlgorithmIO.printInflictedDamage(attacker, hitDamage, defender, false, healthStateChanged);
        } else {
          AttackAlgorithmIO.printMiss(attacker);
        }
        return null;
      }
    });

    registerAttackAlgorithm("CRITTER", new AttackAlgorithm() {
      @Override
      public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
        AttackAlgorithmIO.writeCritterAttackMessage(attacker);
        return null;
      }
    });

    registerAttackAlgorithm("DUMMY", new AttackAlgorithm() {
      @Override
      public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
        AttackAlgorithmIO.writeDummyAttackMessage(attacker);
        return null;
      }
    });

    final double UNDEAD_UNARMED_HIT_RATE = 0.85;
    registerAttackAlgorithm("UNDEAD", new AttackAlgorithm() {
      @Override
      public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
        Item weapon = attacker.getWeapon();
        int hitDamage;
        boolean weaponBroke = false;
        // Check that there is a weapon and that it is not broken.
        if (weapon != null && !weapon.isBroken()) {
          if (weapon.rollForHit()) {
            hitDamage = weapon.getWeaponComponent().getDamage() + attacker.getAttack();
            weapon.decrementIntegrityByHit();
            weaponBroke = weapon.isBroken();
          } else {
            AttackAlgorithmIO.printMiss(attacker);
            return null;
          }
        } else {
          if (Engine.roll(UNDEAD_UNARMED_HIT_RATE)) {
            hitDamage = attacker.getAttack();
          } else {
            AttackAlgorithmIO.printMiss(attacker);
            return null;
          }
        }
        boolean healthStateChanged = defender.takeDamage(hitDamage);
        AttackAlgorithmIO.printInflictedDamage(attacker, hitDamage, defender, false, healthStateChanged);
        if (weaponBroke) {
          AttackAlgorithmIO.printWeaponBreak(weapon);
          if (!weapon.hasTag(Item.Tag.REPAIRABLE)) {
            attacker.getInventory().removeItem(weapon);
          }
        }
        return null;
      }
    });

    final double HERO_CRITICAL_CHANCE = 0.1;
    final double HERO_CRITICAL_CHANCE_UNARMED = 0.05;
    registerAttackAlgorithm("HERO", new AttackAlgorithm() {
      @Override
      public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
        CauseOfDeath causeOfDeath;
        if (attacker.getSkillRotation().hasReadySkill()) {
          Skill skill = attacker.getSkillRotation().getNextSkill();
          skill.cast(attacker, defender);
          causeOfDeath = new CauseOfDeath(TypeOfCauseOfDeath.SKILL, skill.getID());
        } else {
          Item weapon = attacker.getWeapon();
          boolean weaponBroke = false;
          boolean criticalHit;
          int hitDamage;
          // Check that there is a weapon and that it is not broken.
          if (weapon != null && !weapon.isBroken()) {
            if (weapon.rollForHit()) {
              hitDamage = weapon.getWeaponComponent().getDamage() + attacker.getAttack();
              criticalHit = Engine.roll(HERO_CRITICAL_CHANCE);
              weapon.decrementIntegrityByHit();
              weaponBroke = weapon.isBroken();
              causeOfDeath = new CauseOfDeath(TypeOfCauseOfDeath.WEAPON, weapon.getID());
            } else {
              AttackAlgorithmIO.printMiss(attacker);
              return null;
            }
          } else {
            hitDamage = attacker.getAttack();
            criticalHit = Engine.roll(HERO_CRITICAL_CHANCE_UNARMED);
            causeOfDeath = new CauseOfDeath(TypeOfCauseOfDeath.WEAPON, UNARMED_ID);
          }
          if (criticalHit) {
            hitDamage *= 2;
          }
          boolean healthStateChanged = defender.takeDamage(hitDamage);
          AttackAlgorithmIO.printInflictedDamage(attacker, hitDamage, defender, criticalHit, healthStateChanged);
          if (weaponBroke) {
            AttackAlgorithmIO.printWeaponBreak(weapon);
            if (!weapon.hasTag(Item.Tag.REPAIRABLE)) {
              attacker.getInventory().removeItem(weapon);
            }
          }
        }
        return causeOfDeath;
      }
    });

  }

  private AttackAlgorithms() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Registers an AttackAlgorithm using the ID produced by the specified name.
   */
  private static void registerAttackAlgorithm(String name, AttackAlgorithm algorithm) {
    ID id = new ID(name);
    if (ATTACK_ALGORITHM_MAP.containsKey(id)) {
      throw new AssertionError("There is an AttackAlgorithm already defined with the ID produced by this name!");
    }
    ATTACK_ALGORITHM_MAP.put(id, algorithm);
  }

  /**
   * Makes the specified attacker attack the defender using the specified attack algorithm.
   * <p/>
   * Returns what would be the CauseOfDeath if the attack killed the defender.
   * If the attack algorithm is not the Hero's one or even if it is but the Hero missed, null is returned.
   *
   * @param attacker the Creature that is attacking
   * @param defender the Creature that is being attacked
   * @return what would be the CauseOfDeath if the attack killed the defender
   */
  public static CauseOfDeath renderAttack(Creature attacker, Creature defender) {
    AttackAlgorithm attackAlgorithm = ATTACK_ALGORITHM_MAP.get(attacker.getAttackAlgorithmID());
    if (attackAlgorithm != null) {
      return attackAlgorithm.renderAttack(attacker, defender);
    } else {
      DLogger.warning("AttackAlgorithmID does not match any implemented algorithm.");
      return null;
    }
  }

}
