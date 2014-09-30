package org.dungeon.core.creatures.enums;

import org.dungeon.core.creatures.AttackAlgorithmID;

/**
 * An enumerated type of presets for creature creation. Created by Bernardo Sulzbach on 16/09/14.
 */
public enum CreaturePreset {

    FROG(CreatureType.CRITTER, CreatureID.FROG, 10, 5, 3, 2, AttackAlgorithmID.CRITTER, 12),
    RABBIT(CreatureType.CRITTER, CreatureID.RABBIT, 10, 5, 3, 2, AttackAlgorithmID.CRITTER, 10),

    BAT(CreatureType.BEAST, CreatureID.BAT, 12, 6, 5, 4, AttackAlgorithmID.BAT, 15),

    BEAR(CreatureType.BEAST, CreatureID.BEAR, 30, 14, 15, 10, AttackAlgorithmID.BEAST, 40),
    RAT(CreatureType.BEAST, CreatureID.RAT, 14, 8, 7, 4, AttackAlgorithmID.BEAST, 17),
    SNAKE(CreatureType.BEAST, CreatureID.SNAKE, 23, 14, 9, 7, AttackAlgorithmID.BEAST, 25),
    SPIDER(CreatureType.BEAST, CreatureID.SPIDER, 20, 12, 8, 5, AttackAlgorithmID.BEAST, 20),
    WOLF(CreatureType.BEAST, CreatureID.WOLF, 30, 15, 11, 7, AttackAlgorithmID.BEAST, 30),

    SKELETON(CreatureType.UNDEAD, CreatureID.SKELETON, 30, 18, 15, 7, AttackAlgorithmID.UNDEAD, 55),
    ZOMBIE(CreatureType.UNDEAD, CreatureID.ZOMBIE, 29, 17, 13, 6, AttackAlgorithmID.UNDEAD, 45);

    private final CreatureType type;
    private final CreatureID id;
    private final int health;
    private final int healthIncrement;
    private final int attack;
    private final int attackIncrement;
    private final AttackAlgorithmID attackAlgorithm;
    private final int experienceDropFactor;

    CreaturePreset(CreatureType type, CreatureID id, int health, int healthIncrement, int attack, int attackIncrement,
                   AttackAlgorithmID attackAlgorithm, int experienceDropFactor) {
        this.type = type;
        this.id = id;
        this.health = health;
        this.healthIncrement = healthIncrement;
        this.attack = attack;
        this.attackIncrement = attackIncrement;
        this.attackAlgorithm = attackAlgorithm;
        this.experienceDropFactor = experienceDropFactor;
    }

    public CreatureType getType() {
        return type;
    }

    public CreatureID getId() {
        return id;
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
