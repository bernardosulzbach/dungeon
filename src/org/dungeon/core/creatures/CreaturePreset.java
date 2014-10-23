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

/**
 * An enumerated type of presets for creature creation. Created by Bernardo Sulzbach on 16/09/14.
 */
public enum CreaturePreset {

    FROG("FROG", "CRITTER", "Frog", 5, 3, 2, "CRITTER", 12, 10),
    RABBIT("RABBIT", "CRITTER", "Rabbit", 5, 3, 2, "CRITTER", 10, 10),
    BAT("BAT", "BEAST", "Bat", 6, 5, 4, "BAT", 15, 12),
    BEAR("BEAR", "BEAST", "Bear", 14, 15, 10, "BEAST", 40, 30),
    RAT("RAT", "BEAST", "Rat", 8, 7, 4, "BEAST", 17, 14),
    SNAKE("SNAKE", "BEAST", "Snake", 14, 9, 7, "BEAST", 25, 23),
    SPIDER("SPIDER", "BEAST", "Spider", 12, 8, 5, "BEAST", 20, 20),
    WOLF("WOLF", "BEAST", "Wolf", 15, 11, 7, "BEAST", 30, 30),
    SKELETON("SKELETON", "UNDEAD", "Skeleton", 18, 15, 7, "UNDEAD", 55, 30),
    ZOMBIE("ZOMBIE", "UNDEAD", "Zombie", 17, 13, 6, "UNDEAD", 45, 29);

    private final String id;
    private final String type;
    private final String name;
    private final int health;
    private final int healthIncrement;
    private final int attack;
    private final int attackIncrement;
    private final String attackAlgorithm;
    private final int experienceDropFactor;

    CreaturePreset(String id, String type, String name, int healthIncrement, int attack, int attackIncrement, String attackAlgorithm, int experienceDropFactor, int health) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.health = health;
        this.healthIncrement = healthIncrement;
        this.attack = attack;
        this.attackIncrement = attackIncrement;
        this.attackAlgorithm = attackAlgorithm;
        this.experienceDropFactor = experienceDropFactor;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getHealthIncrement() {
        return healthIncrement;
    }

    public int getAttack() {
        return attack;
    }

    public int getAttackIncrement() {
        return attackIncrement;
    }

    public String getAttackAlgorithm() {
        return attackAlgorithm;
    }

    public int getExperienceDropFactor() {
        return experienceDropFactor;
    }
}
