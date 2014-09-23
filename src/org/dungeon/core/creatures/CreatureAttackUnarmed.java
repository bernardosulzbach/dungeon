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

import java.io.Serializable;

class CreatureAttackUnarmed implements CreatureAttack, Serializable {

    private int baseAttack;

    public CreatureAttackUnarmed(int attack) {
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
        // Hardcoded miss rate of 10%.
        if (10 > Game.RANDOM.nextInt(100)) {
            IO.writeString(String.format("%s tried to hit %s but missed.", attacker.getName(), target.getName()));
        } else {
            target.takeDamage(attacker.getAttack());
            IO.writeString(String.format("%s inflicted %d damage points to %s.\n", attacker.getName(), getBaseAttack(), target.getName()));
        }
    }
}