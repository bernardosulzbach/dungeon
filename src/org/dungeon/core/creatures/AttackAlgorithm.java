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
package org.dungeon.core.creatures;

import org.dungeon.core.game.Game;
import org.dungeon.core.items.Item;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Utils;

import java.awt.*;

/**
 * AttackAlgorithm class that defines all the attack algorithms. Specific attack algorithms are used by using invoking
 * the AttackAlgorithm.attack() method with the right parameters.
 * <p/>
 * Created by Bernardo Sulzbach on 29/09/14.
 */
public class AttackAlgorithm {

    public static void attack(Creature attacker, Creature defender, AttackAlgorithmID algorithm) {
        switch (algorithm) {
            case BAT:
                batAttack(attacker, defender);
                break;
            case BEAST:
                beastAttack(attacker, defender);
                break;
            case CRITTER:
                critterAttack(attacker);
                break;
            case UNDEAD:
                undeadAttack(attacker, defender);
                break;
            case HERO:
                heroAttack(attacker, defender);
                break;
        }
    }

    private static void batAttack(Creature attacker, Creature defender) {
        // TODO: implement luminosity check here.
        beastAttack(attacker, defender);
    }

    private static void beastAttack(Creature attacker, Creature defender) {
        // 10% miss chance.
        if (0.9 > Game.RANDOM.nextDouble()) {
            int hitDamage = attacker.getAttack();
            defender.takeDamage(hitDamage);
            printInflictedDamage(attacker, hitDamage, defender, false);
        } else {
            printMiss(attacker);
        }
    }

    private static void critterAttack(Creature attacker) {
        if (Game.RANDOM.nextBoolean()) {
            IO.writeString(attacker.getName() + " does nothing.", Color.YELLOW);
        } else {
            IO.writeString(attacker.getName() + " tries to run away.", Color.YELLOW);
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
            if (0.85 > Game.RANDOM.nextDouble()) {
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
                // Hardcoded 5 % chance of a critical hit (double damage).
                if (Utils.roll(0.05)) {
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
            printInflictedDamage(attacker, hitDamage, defender, false);
        }
        defender.takeDamage(hitDamage);
        // The inflicted damage message cannot be here (what would avoid code duplication) as that would make it appear
        // after an eventual "weaponName broke" message, what looks really weird.
    }
    
    /**
     * Prints a message about the inflicted damage based on the parameters.
     * @param attacker the Creature that performed the attack.
     * @param hitDamage the damage inflicted by the attacker.
     * @param defender the target of the attack.
     * @param criticalHit a boolean indicating if the attack was a critical hit or not.
     */
    public static void printInflictedDamage(Creature attacker, int hitDamage, Creature defender, boolean criticalHit) {
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
        IO.writeString(builder.toString(), attacker.getId().equals(Constants.HERO_ID) ? Color.GREEN : Color.RED);
    }
    
    // Simple method that prints a miss message.
    public static void printMiss(Creature attacker) {
        IO.writeString(attacker.getName() + " missed.", Color.YELLOW);
    }

}
