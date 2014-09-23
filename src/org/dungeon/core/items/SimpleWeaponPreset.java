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

package org.dungeon.core.items;

/**
 * Enumerated presets used to create weapons.
 * <p/>
 * Change log
 * Created by Bernardo on 18/09/2014.
 */
public enum SimpleWeaponPreset {
    DAGGER("Dagger", 13, 15, 80, 1),
    LONGSWORD("Longsword", 18, 20, 100, 1),
    MACE("Mace", 15, 20, 90, 1),
    SPEAR("Spear", 10, 12, 70, 1),
    STAFF("Staff", 14, 20, 75, 1),
    STICK("Stick", 6, 22, 30, 1);

    protected final String name;
    protected final int damage;
    protected final int missRate;
    protected final int startingIntegrity;
    protected final int hitDecrement;

    SimpleWeaponPreset(String name, int damage, int missRate, int startingIntegrity, int hitDecrement) {
        this.name = name;
        this.damage = damage;
        this.missRate = missRate;
        this.startingIntegrity = startingIntegrity;
        this.hitDecrement = hitDecrement;
    }
}
