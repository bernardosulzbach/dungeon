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
import org.dungeon.core.items.Item;
import org.dungeon.core.items.Weapon;
import org.dungeon.io.IO;

/**
 * Created by Bernardo on 21/09/2014.
 */
public abstract class ArmedCreature extends Creature {

    public ArmedCreature(CreatureType type, CreatureID id, String name) {
        super(type, id, name);
    }

    /**
     * Try to hit a target. If the creature has a weapon, it will be used to perform the attack. Otherwise, the creature will attack with
     * its bare hands.
     */
    @Override
    public void hit(Creature target) {
        Weapon heroWeapon = getWeapon();
        int hitDamage;
        // Check that there is a weapon and that it is not broken.
        if (heroWeapon != null) {
            if (!heroWeapon.isMiss()) {
                hitDamage = heroWeapon.getDamage();
                IO.writeString(String.format("%s inflicted %d damage points to %s.\n", getName(), hitDamage, target.getName()));
                if (heroWeapon instanceof Breakable) {
                    Breakable breakableWeapon = (Breakable) heroWeapon;
                    breakableWeapon.decrementIntegrity();
                    if (breakableWeapon.isBroken()) {
                        setWeapon(null);
                        // TODO: fix this cast. All IWeapon (now) is-a item.
                        // This reinforces the idea that I should be using inheritance?
                        getInventory().removeItem((Item) getWeapon());
                    }
                }
            } else {
                IO.writeString(getName() + " misses.");
                return;
            }
        } else {
            hitDamage = getAttack();
            IO.writeString(String.format("%s inflicted %d damage points to %s.\n", getName(), hitDamage, target.getName()));
        }
        target.takeDamage(hitDamage);
        // The inflicted damage message cannot be here (what would avoid code duplication) as that would make it appear
        // after an eventual "weaponName broke" message, what looks really weird.
    }
}
