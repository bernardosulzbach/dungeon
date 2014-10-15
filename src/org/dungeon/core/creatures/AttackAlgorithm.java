package org.dungeon.core.creatures;

import org.dungeon.core.game.Game;
import org.dungeon.core.items.Item;
import org.dungeon.io.IO;
import org.dungeon.io.WriteStyle;
import org.dungeon.utils.Utils;

/**
 * AttackAlgorithm class that defines all the attack algorithms.
 * Specific attack algorithms are used by using invoking the AttackAlgorithm.attack() method with the right parameters.
 * <p/>
 * Created by bernardo on 29/09/14.
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
        if (0.9 > Game.RANDOM.nextInt()) {
            defender.takeDamage(attacker.getAttack());
            // Damage == attacker's attack.
            IO.writeString(String.format("%s inflicted %d damage points to %s.",
                    attacker.getName(), attacker.getAttack(), defender.getName()), WriteStyle.MARGIN);
        } else {
            IO.writeString(String.format("%s tried to hit %s but missed.",
                    attacker.getName(), defender.getName()), WriteStyle.MARGIN);
        }
    }

    private static void critterAttack(Creature attacker) {
        if (Game.RANDOM.nextBoolean()) {
            IO.writeString(attacker.getName() + " does nothing.", WriteStyle.MARGIN);
        } else {
            IO.writeString(attacker.getName() + " tries to run away.", WriteStyle.MARGIN);
        }
    }

    private static void undeadAttack(Creature attacker, Creature defender) {
        Item weapon = attacker.getWeapon();
        int hitDamage;
        // Check that there is a weapon and that it is not broken.
        if (weapon != null && !weapon.isBroken()) {
            if (weapon.rollForHit()) {
                hitDamage = weapon.getDamage() + attacker.getAttack();
                IO.writeString(String.format("%s inflicted %d damage points to %s.", attacker.getName(), hitDamage,
                        defender.getName()), WriteStyle.MARGIN);
                weapon.decrementIntegrityByHit();
                if (weapon.isBroken()) {
                    if (!weapon.isRepairable()) {
                        attacker.getInventory().removeItem(weapon);
                    }
                }
            } else {
                IO.writeString(attacker.getName() + " misses.", WriteStyle.MARGIN);
                return;
            }
        } else {
            // Hardcoded 15% miss chance.
            if (0.85 > Game.RANDOM.nextDouble()) {
                hitDamage = attacker.getAttack();
                IO.writeString(String.format("%s inflicted %d damage points to %s.", attacker.getName(), hitDamage,
                        defender.getName()), WriteStyle.MARGIN);
            } else {
                IO.writeString(attacker.getName() + " misses.", WriteStyle.MARGIN);
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
                    IO.writeString(String.format("%s inflicted %d damage points to %s with a critical hit!",
                            attacker.getName(), hitDamage, defender.getName()), WriteStyle.MARGIN);
                } else {
                    IO.writeString(String.format("%s inflicted %d damage points to %s.",
                            attacker.getName(), hitDamage, defender.getName()), WriteStyle.MARGIN);
                }
                weapon.decrementIntegrityByHit();
                if (weapon.isBroken()) {
                    if (!weapon.isRepairable()) {
                        attacker.getInventory().removeItem(weapon);
                    }
                }
            } else {
                IO.writeString(attacker.getName() + " misses.", WriteStyle.MARGIN);
                return;
            }
        } else {
            hitDamage = attacker.getAttack();
            IO.writeString(String.format("%s inflicted %d damage points to %s.", attacker.getName(), hitDamage,
                    defender.getName()), WriteStyle.MARGIN);
        }
        defender.takeDamage(hitDamage);
        // The inflicted damage message cannot be here (what would avoid code duplication) as that would make it appear
        // after an eventual "weaponName broke" message, what looks really weird.
    }

}
