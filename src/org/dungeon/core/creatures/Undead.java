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
     * @param target
     */
    @Override
    public void hit(Creature target) {
        int hitDamage;
        // Check that there is a weapon and that it is not broken.
        if (getWeapon() != null && !getWeapon().isBroken()) {
            // Check if the attack is a miss.
            if (getWeapon().isMiss()) {
                IO.writeString(getName() + " misses.");
                return;
            } else {
                hitDamage = getWeapon().getDamage();
                target.takeDamage(hitDamage);
                getWeapon().decrementIntegrity();
            }
        } else {
            hitDamage = this.getAttack();
            target.takeDamage(hitDamage);
        }
        IO.writeString(String.format("%s inflicted %d damage points to %s.\n", getName(), hitDamage, target.getName()));

    }

}
