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

import org.dungeon.core.items.Item;
import org.dungeon.io.IO;

import java.io.Serializable;

class CreatureAttackWeapon implements CreatureAttack, Serializable {

    private int baseAttack;

    public CreatureAttackWeapon(int attack) {
        this.baseAttack = attack;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }

    @Override
    public void attack(Creature attacker, Creature target) {
        Item weapon = attacker.getWeapon();
        int hitDamage;
        // Check that there is a weapon and that it is not broken.
        if (weapon != null) {
            if (weapon.rollForHit()) {
                hitDamage = weapon.getDamage();
                IO.writeString(String.format("%s inflicted %d damage points to %s.\n", attacker.getName(), hitDamage, target.getName()));
                weapon.decrementIntegrityByHit();
                if (weapon.isBroken()) {
                    attacker.getInventory().removeItem(weapon);
                }
            } else {
                IO.writeString(attacker.getName() + " misses.");
                return;
            }
        } else {
            hitDamage = getBaseAttack();
            IO.writeString(String.format("%s inflicted %d damage points to %s.\n", attacker.getName(), hitDamage, target.getName()));
        }
        target.takeDamage(hitDamage);
        // The inflicted damage message cannot be here (what would avoid code duplication) as that would make it appear
        // after an eventual "weaponName broke" message, what looks really weird.
    }
}