/*
 * Copyright (C) 2014 Bernardo Sulzbach
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

package org.dungeon.creatures;

import org.dungeon.game.Engine;
import org.dungeon.io.DLogger;
import org.dungeon.io.IO;
import org.dungeon.items.Item;
import org.dungeon.skill.Skill;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.TypeOfCauseOfDeath;
import org.dungeon.util.Constants;

import java.awt.Color;

/**
 * AttackAlgorithm class that defines all the attack algorithms. Specific attack algorithms are used by using invoking
 * the AttackAlgorithm.attack() method with the right parameters.
 * <p/>
 * Created by Bernardo Sulzbach on 29/09/14.
 */
class AttackAlgorithm {

  private static final double BAT_CRITICAL_MAXIMUM_LUMINOSITY = 0.5;
  private static final double BEAST_HIT_RATE = 0.9;
  private static final double HERO_CRITICAL_CHANCE = 0.1;
  private static final double HERO_CRITICAL_CHANCE_UNARMED = 0.05;
  private static final double UNDEAD_UNARMED_HIT_RATE = 0.85;

  /**
   * Makes the specified attacker attack the defender using the specified attack algorithm.
   * <p/>
   * Returns what would be the CauseOfDeath if the attack killed the defender.
   * If the attack algorithm is not the Hero's one or even if it is but the Hero missed, null is returned.
   *
   * @param attacker    the Creature that is attacking
   * @param defender    the Creature that is being attacked
   * @param algorithmID the algorithm's ID String
   * @return what would be the CauseOfDeath if the attack killed the defender
   */
  public static CauseOfDeath attack(Creature attacker, Creature defender, String algorithmID) {
    if (algorithmID.equals("BAT")) {
      batAttack(attacker, defender);
    } else if (algorithmID.equals("BEAST")) {
      beastAttack(attacker, defender);
    } else if (algorithmID.equals("CRITTER")) {
      critterAttack(attacker);
    } else if (algorithmID.equals("DUMMY")) {
      dummyAttack(attacker);
    } else if (algorithmID.equals("UNDEAD")) {
      undeadAttack(attacker, defender);
    } else if (algorithmID.equals("HERO")) {
      return heroAttack(attacker, defender);
    } else {
      DLogger.warning("algorithmID does not match any implemented algorithm.");
    }
    return null;
  }

  // Similar to beastAttack, but with miss chance dependant on luminosity and critical chance in complete darkness.
  private static void batAttack(Creature attacker, Creature defender) {
    double luminosity = attacker.getLocation().getLuminosity().toDouble();
    if (Engine.roll(0.9 - luminosity / 2)) { // If the permittivity is 1, this value ranges from 0.8 to 0.4.
      int hitDamage = attacker.getAttack();
      boolean criticalHit = luminosity <= BAT_CRITICAL_MAXIMUM_LUMINOSITY;
      if (criticalHit) {
        hitDamage *= 2;
      }
      boolean healthStateChanged = defender.takeDamage(hitDamage);
      printInflictedDamage(attacker, hitDamage, defender, criticalHit, healthStateChanged);
    } else {
      printMiss(attacker);
    }
  }

  private static void beastAttack(Creature attacker, Creature defender) {
    if (Engine.roll(BEAST_HIT_RATE)) {
      int hitDamage = attacker.getAttack();
      boolean healthStateChanged = defender.takeDamage(hitDamage);
      printInflictedDamage(attacker, hitDamage, defender, false, healthStateChanged);
    } else {
      printMiss(attacker);
    }
  }

  private static void critterAttack(Creature attacker) {
    if (Engine.RANDOM.nextBoolean()) {
      IO.writeBattleString(attacker.getName() + " does nothing.", Color.YELLOW);
    } else {
      IO.writeBattleString(attacker.getName() + " tries to run away.", Color.YELLOW);
    }
  }

  private static void dummyAttack(Creature attacker) {
    IO.writeBattleString(attacker.getName() + " stands still.", Color.YELLOW);
  }

  private static void undeadAttack(Creature attacker, Creature defender) {
    Item weapon = attacker.getWeapon();
    int hitDamage;
    boolean weaponBroke = false;
    // Check that there is a weapon and that it is not broken.
    if (weapon != null && !weapon.isBroken()) {
      if (weapon.rollForHit()) {
        hitDamage = weapon.getDamage() + attacker.getAttack();
        weapon.decrementIntegrityByHit();
        weaponBroke = weapon.isBroken();
      } else {
        printMiss(attacker);
        return;
      }
    } else {
      if (Engine.roll(UNDEAD_UNARMED_HIT_RATE)) {
        hitDamage = attacker.getAttack();
      } else {
        printMiss(attacker);
        return;
      }
    }
    boolean healthStateChanged = defender.takeDamage(hitDamage);
    printInflictedDamage(attacker, hitDamage, defender, false, healthStateChanged);
    if (weaponBroke) {
      printWeaponBreak(weapon);
      if (!weapon.isRepairable()) {
        attacker.getInventory().removeItem(weapon);
      }
    }
  }

  private static CauseOfDeath heroAttack(Creature attacker, Creature defender) {
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
          hitDamage = weapon.getDamage() + attacker.getAttack();
          criticalHit = Engine.roll(HERO_CRITICAL_CHANCE);
          weapon.decrementIntegrityByHit();
          weaponBroke = weapon.isBroken();
          causeOfDeath = new CauseOfDeath(TypeOfCauseOfDeath.WEAPON, weapon.getID());
        } else {
          printMiss(attacker);
          return null;
        }
      } else {
        hitDamage = attacker.getAttack();
        criticalHit = Engine.roll(HERO_CRITICAL_CHANCE_UNARMED);
        causeOfDeath = new CauseOfDeath(TypeOfCauseOfDeath.WEAPON, Constants.UNARMED_ID);
      }
      if (criticalHit) {
        hitDamage *= 2;
      }
      boolean healthStateChanged = defender.takeDamage(hitDamage);
      printInflictedDamage(attacker, hitDamage, defender, criticalHit, healthStateChanged);
      if (weaponBroke) {
        printWeaponBreak(weapon);
        if (!weapon.isRepairable()) {
          attacker.getInventory().removeItem(weapon);
        }
      }
    }
    return causeOfDeath;
  }

  /**
   * Prints that a weapon broke.
   *
   * @param weapon the weapon that broke.
   */
  private static void printWeaponBreak(Item weapon) {
    IO.writeString(weapon.getName() + " broke!", Color.RED);
  }

  /**
   * Prints a message about the inflicted damage based on the parameters.
   *
   * @param attacker    the Creature that performed the attack.
   * @param hitDamage   the damage inflicted by the attacker.
   * @param defender    the target of the attack.
   * @param criticalHit a boolean indicating if the attack was a critical hit or not.
   */
  private static void printInflictedDamage(Creature attacker, int hitDamage, Creature defender, boolean criticalHit,
      boolean healthStateChanged) {
    String s = String.format("%s inflicted %d damage points to %s", attacker.getName(), hitDamage, defender.getName());
    s += criticalHit ? " with a critical hit." : ".";
    if (healthStateChanged) {
      HealthState currentHealthState = HealthState.getHealthState(defender.getCurHealth(), defender.getMaxHealth());
      s += String.format(" It looks %s.", currentHealthState.toString().toLowerCase());
    }
    IO.writeBattleString(s, attacker.getID().equals(Constants.HERO_ID) ? Color.GREEN : Color.RED);
  }

  /**
   * Prints a miss message.
   *
   * @param attacker the attacker creature.
   */
  private static void printMiss(Creature attacker) {
    IO.writeBattleString(attacker.getName() + " missed.", Color.YELLOW);
  }

}
