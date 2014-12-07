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
import org.dungeon.io.IO;
import org.dungeon.items.Item;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Utils;

import java.awt.*;

/**
 * AttackAlgorithm class that defines all the attack algorithms. Specific attack algorithms are used by using invoking
 * the AttackAlgorithm.attack() method with the right parameters.
 * <p/>
 * Created by Bernardo Sulzbach on 29/09/14.
 */
class AttackAlgorithm {

    private static final double HERO_CRITICAL_CHANCE = 0.1;
    private static final double HERO_CRITICAL_CHANCE_UNARMED = 0.05;

    public static void attack(Creature attacker, Creature defender, String algorithmID) {
        if (algorithmID.equals("BAT")) {
            batAttack(attacker, defender);
        } else if (algorithmID.equals("BEAST")) {
            beastAttack(attacker, defender);
        } else if (algorithmID.equals("CRITTER")) {
            critterAttack(attacker);
        } else if (algorithmID.equals("UNDEAD")) {
            undeadAttack(attacker, defender);
        } else if (algorithmID.equals("HERO")) {
            heroAttack(attacker, defender);
        } else {
            throw new IllegalArgumentException("algorithmID does not match any implemented algorithm.");
        }
    }

    // Similar to beastAttack, but with miss chance dependant on luminosity and critical chance in complete darkness.
    private static void batAttack(Creature attacker, Creature defender) {
        double luminosity = attacker.getLocation().getLuminosity();
        // At complete darkness: 90% hit chance.
        //      noon's sunlight: 40% hit chance.
        if (Utils.roll(0.9 - luminosity / 2)) {
            int hitDamage = attacker.getAttack();
            if (luminosity == 0.0) {
                hitDamage *= 2;
                printInflictedDamage(attacker, hitDamage, defender, true);
            } else {
                printInflictedDamage(attacker, hitDamage, defender, false);
            }
            defender.takeDamage(hitDamage);
        } else {
            printMiss(attacker);
        }
    }

    private static void beastAttack(Creature attacker, Creature defender) {
        // 10% miss chance.
        if (Utils.roll(0.9)) {
            int hitDamage = attacker.getAttack();
            defender.takeDamage(hitDamage);
            printInflictedDamage(attacker, hitDamage, defender, false);
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

    private static void undeadAttack(Creature attacker, Creature defender) {
        Item weapon = attacker.getWeapon();
        int hitDamage;
        // Check that there is a weapon and that it is not broken.
        if (weapon != null && !weapon.isBroken()) {
            if (weapon.rollForHit()) {
                hitDamage = weapon.getDamage() + attacker.getAttack();
                printInflictedDamage(attacker, hitDamage, defender, false);
                weapon.decrementIntegrityByHit();
                if (weapon.isBroken()) {
                    if (!weapon.isRepairable()) {
                        attacker.getInventory().removeItem(weapon);
                    }
                }
            } else {
                printMiss(attacker);
                return;
            }
        } else {
            // Hardcoded 15% miss chance.
            if (0.85 > Engine.RANDOM.nextDouble()) {
                hitDamage = attacker.getAttack();
                printInflictedDamage(attacker, hitDamage, defender, false);
            } else {
                printMiss(attacker);
                return;
            }
        }
        defender.takeDamage(hitDamage);
        // The inflicted damage message cannot be here (what would avoid code duplication) as that would make it appear
        // after an eventual "weaponName broke" message, what looks really weird.
    }

    private static void heroAttack(Creature attacker, Creature defender) {
        Item weapon = attacker.getWeapon();
        int hitDamage;
        // Check that there is a weapon and that it is not broken.
        if (weapon != null && !weapon.isBroken()) {
            if (weapon.rollForHit()) {
                hitDamage = weapon.getDamage() + attacker.getAttack();
                if (Utils.roll(HERO_CRITICAL_CHANCE)) {
                    hitDamage *= 2;
                    printInflictedDamage(attacker, hitDamage, defender, true);
                } else {
                    printInflictedDamage(attacker, hitDamage, defender, false);
                }
                weapon.decrementIntegrityByHit();
                if (weapon.isBroken()) {
                    if (!weapon.isRepairable()) {
                        attacker.getInventory().removeItem(weapon);
                    }
                }
            } else {
                printMiss(attacker);
                return;
            }
        } else {
            hitDamage = attacker.getAttack();
            if (Utils.roll(HERO_CRITICAL_CHANCE_UNARMED)) {
                hitDamage *= 2;
                printInflictedDamage(attacker, hitDamage, defender, true);
            } else {
                printInflictedDamage(attacker, hitDamage, defender, false);
            }
        }
        defender.takeDamage(hitDamage);
        // The inflicted damage message cannot be here (what would avoid code duplication) as that would make it appear
        // after an eventual "weaponName broke" message, what looks really weird.
    }

    /**
     * Prints a message about the inflicted damage based on the parameters.
     *
     * @param attacker    the Creature that performed the attack.
     * @param hitDamage   the damage inflicted by the attacker.
     * @param defender    the target of the attack.
     * @param criticalHit a boolean indicating if the attack was a critical hit or not.
     */
    private static void printInflictedDamage(Creature attacker, int hitDamage, Creature defender, boolean criticalHit) {
        StringBuilder builder = new StringBuilder();
        builder.append(attacker.getName());
        builder.append(" inflicted ");
        builder.append(hitDamage);
        builder.append(" damage points to ");
        builder.append(defender.getName());
        if (criticalHit) {
            builder.append(" with a critical hit");
        }
        builder.append(".");
        IO.writeBattleString(builder.toString(), attacker.getId().equals(Constants.HERO_ID) ? Color.GREEN : Color.RED);
    }

    // Simple method that prints a miss message.
    private static void printMiss(Creature attacker) {
        IO.writeBattleString(attacker.getName() + " missed.", Color.YELLOW);
    }

}
