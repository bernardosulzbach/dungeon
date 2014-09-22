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
import org.dungeon.io.IO;

class CreatureAttackNone implements CreatureAttack {

    @Override
    public void setBaseAttack(int baseAttack) {

    }

    @Override
    public int getBaseAttack() {
        return 0;
    }

    @Override
    public void attack(Creature attacker, Creature target) {
        if (Game.RANDOM.nextBoolean()) {
            IO.writeString(attacker.getName() + " does nothing.");
        } else {
            IO.writeString(attacker.getName()+ " tries to run away.");
        }
    }
}
