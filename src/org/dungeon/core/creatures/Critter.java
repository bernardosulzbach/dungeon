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
import org.dungeon.core.game.Game;
import org.dungeon.io.IO;

/**
 * Defines critters.
 *
 * @author Bernardo Sulzbach
 */
public class Critter extends Creature {

    public Critter(CreatureID id, String name) {
        super(CreatureType.CRITTER, id, name);
    }

    @Override
    public void hit(Creature target) {
        // Two different output strings are possible for critters.
        if (Game.RANDOM.nextBoolean()) {
            IO.writeString(getName() + " does nothing.");
        } else {
            IO.writeString(getName() + " tries to run away.");
        }
    }

    @Override
    public void takeDamage(int damage) {
        // Attacks against critters deal 100% bonus damage.
        damage = damage * 2;
        if (damage > getCurHealth()) {
            setCurHealth(0);
        } else {
            setCurHealth(getCurHealth() - damage);
        }
    }
}
