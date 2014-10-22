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

    FROG("FROG", CreatureType.CRITTER, "Frog", 5, 3, 2, AttackAlgorithmID.CRITTER, 12, 10),
    RABBIT("RABBIT", CreatureType.CRITTER, "Rabbit", 5, 3, 2, AttackAlgorithmID.CRITTER, 10, 10),
    BAT("BAT", CreatureType.BEAST, "Bat", 6, 5, 4, AttackAlgorithmID.BAT, 15, 12),
    BEAR("BEAR", CreatureType.BEAST, "Bear", 14, 15, 10, AttackAlgorithmID.BEAST, 40, 30),
    RAT("RAT", CreatureType.BEAST, "Rat", 8, 7, 4, AttackAlgorithmID.BEAST, 17, 14),
    SNAKE("SNAKE", CreatureType.BEAST, "Snake", 14, 9, 7, AttackAlgorithmID.BEAST, 25, 23),
    SPIDER("SPIDER", CreatureType.BEAST, "Spider", 12, 8, 5, AttackAlgorithmID.BEAST, 20, 20),
    WOLF("WOLF", CreatureType.BEAST, "Wolf", 15, 11, 7, AttackAlgorithmID.BEAST, 30, 30),
    SKELETON("SKELETON", CreatureType.UNDEAD, "Skeleton", 18, 15, 7, AttackAlgorithmID.UNDEAD, 55, 30),
    ZOMBIE("ZOMBIE", CreatureType.UNDEAD, "Zombie", 17, 13, 6, AttackAlgorithmID.UNDEAD, 45, 29);

    private final String id;
    private final CreatureType type;
    private final String name;
    private final int health;
    private final int healthIncrement;
    private final int attack;
    private final int attackIncrement;
    private final AttackAlgorithmID attackAlgorithm;
    private final int experienceDropFactor;

    CreaturePreset(String id, CreatureType type, String name, int healthIncrement, int attack, int attackIncrement, AttackAlgorithmID attackAlgorithm, int experienceDropFactor, int health) {
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

    public CreatureType getType() {
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

    public AttackAlgorithmID getAttackAlgorithm() {
        return attackAlgorithm;
    }

    public int getExperienceDropFactor() {
        return experienceDropFactor;
    }
}
