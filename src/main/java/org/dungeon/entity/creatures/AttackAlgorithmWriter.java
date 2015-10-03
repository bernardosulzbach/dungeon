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
import org.dungeon.game.DungeonString;
import org.dungeon.game.Game;
import org.dungeon.io.Writer;

import java.awt.Color;

/**
 * This class is uninstantiable and provides utility IO methods for AttackAlgorithm implementations.
 */
final class AttackAlgorithmWriter {

  private AttackAlgorithmWriter() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Writes a message about the inflicted damage based on the parameters.
   *
   * @param attacker the Creature that performed the attack
   * @param hitDamage the damage inflicted by the attacker
   * @param defender the target of the attack
   * @param criticalHit a boolean indicating if the attack was a critical hit or not
   */
  static void writeInflictedDamage(Creature attacker, int hitDamage, Creature defender, boolean criticalHit) {
    DungeonString string = new DungeonString();
    string.setColor(attacker.getId().equals(Game.getGameState().getHero().getId()) ? Color.GREEN : Color.RED);
    string.append(attacker.getName().getSingular());
    string.append(" inflicted ");
    string.append(String.valueOf(hitDamage));
    string.append(" damage points to ");
    string.append(defender.getName().getSingular());
    if (criticalHit) {
      string.append(" with a critical hit");
    }
    string.append(".");
    string.append(" It looks ");
    string.append(defender.getHealth().getHealthState().toString().toLowerCase());
    string.append(".\n");
    Writer.writeAndWait(string);
  }

  /**
   * Writes a miss message.
   *
   * @param attacker the attacker creature
   */
  static void writeMiss(Creature attacker) {
    Writer.writeAndWait(new DungeonString(attacker.getName() + " missed.\n", Color.YELLOW));
  }

  /**
   * Writes a weapon breakage message.
   *
   * @param weapon the weapon that broke, should be broken
   */
  static void writeWeaponBreak(Item weapon) {
    if (!weapon.isBroken()) {
      throw new IllegalArgumentException("weapon is not broken.");
    }
    Writer.write(new DungeonString(weapon.getName() + " broke!\n", Color.RED));
  }

}
