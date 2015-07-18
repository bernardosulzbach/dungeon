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
import org.dungeon.io.Writer;
import org.dungeon.util.Constants;

import java.awt.Color;

/**
 * This class is uninstantiable and provides utility IO methods for AttackAlgorithm implementations.
 */
class AttackAlgorithmWriter {

  /**
   * Prints a message about the inflicted damage based on the parameters.
   *
   * @param attacker the Creature that performed the attack
   * @param hitDamage the damage inflicted by the attacker
   * @param defender the target of the attack
   * @param criticalHit a boolean indicating if the attack was a critical hit or not
   * @param healthStateChanged a boolean indicating if the HealthState of the defender changed or not
   */
  static void writeInflictedDamage(Creature attacker, int hitDamage, Creature defender, boolean criticalHit,
      boolean healthStateChanged) {
    String s = String.format("%s inflicted %d damage points to %s", attacker.getName(), hitDamage, defender.getName());
    s += criticalHit ? " with a critical hit." : ".";
    if (healthStateChanged) {
      HealthState currentHealthState = HealthState.getHealthState(defender.getCurHealth(), defender.getMaxHealth());
      s += String.format(" It looks %s.", currentHealthState.toString().toLowerCase());
    }
    Writer.writeBattleString(s, attacker.getId().equals(Constants.HERO_ID) ? Color.GREEN : Color.RED);
  }

  /**
   * Prints a miss message.
   *
   * @param attacker the attacker creature
   */
  static void writeMiss(Creature attacker) {
    Writer.writeBattleString(attacker.getName() + " missed.", Color.YELLOW);
  }

  /**
   * Prints that a weapon broke.
   *
   * @param weapon the weapon that broke
   */
  static void writeWeaponBreak(Item weapon) {
    Writer.writeString(weapon.getName() + " broke!", Color.RED);
  }

  public static void writeCritterAttackMessage(Creature attacker) {
    if (Random.nextBoolean()) {
      Writer.writeBattleString(attacker.getName() + " does nothing.", Color.YELLOW);
    } else {
      Writer.writeBattleString(attacker.getName() + " tries to run away.", Color.YELLOW);
    }
  }

  public static void writeDummyAttackMessage(Creature attacker) {
    Writer.writeBattleString(attacker.getName() + " stands still.", Color.YELLOW);
  }

}
