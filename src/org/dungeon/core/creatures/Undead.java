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

import org.dungeon.core.creatures.enums.CreatureID;
import org.dungeon.core.creatures.enums.CreatureType;
import org.dungeon.core.items.Breakable;
import org.dungeon.core.items.IWeapon;
import org.dungeon.io.IO;

/**
 *
 * @author Bernardo Sulzbach
 */
public class Undead extends Creature {

    public Undead(CreatureID id, String name) {
        super(CreatureType.UNDEAD, id, name);
    }

    /**
     * Try to hit a target. If the creature has a weapon, it will be used to perform the attack. Otherwise, the creature will attack with
     * its bare hands.
     *
     */
    @Override
    public void hit(Creature target) {
        IWeapon heroWeapon = getWeapon();
        int hitDamage;
        // Check that there is a weapon and that it is not broken.
        if (heroWeapon != null) {
            if (heroWeapon instanceof Breakable) {
                Breakable breakableWeapon = (Breakable) heroWeapon;
                if (!breakableWeapon.isBroken()) {
                    if (!heroWeapon.isMiss()) {
                        breakableWeapon.decrementIntegrity();
                        hitDamage = heroWeapon.getDamage();
                    } else {
                        IO.writeString(getName() + " misses.");
                        return;
                    }
                } else {
                    hitDamage = getAttack();
                }
            } else {
                if (!heroWeapon.isMiss()) {
                    hitDamage = heroWeapon.getDamage();
                } else {
                    IO.writeString(getName() + " misses.");
                    return;

                }
            }
        } else {
            hitDamage = getAttack();
        }
        target.takeDamage(hitDamage);
        IO.writeString(String.format("%s inflicted %d damage points to %s.\n", getName(), hitDamage, target.getName()));
    }

}
