package org.dungeon.core.creatures.enums;

/**
 * An enumerated type of presets for creature creation. Created by Bernardo Sulzbach on 16/09/14.
 */
public enum CreaturePreset {

    BAT(CreatureID.BAT, 12, 4, 6, 4, 15),
    BEAR(CreatureID.BEAR, 20, 10, 10, 7, 40),
    RABBIT(CreatureID.RABBIT, 10, 5, 3, 2, 10),
    RAT(CreatureID.RAT, 14, 4, 8, 5, 20),
    SPIDER(CreatureID.SPIDER, 16, 6, 7, 7, 25),
    WOLF(CreatureID.WOLF, 18, 7, 6, 8, 30),
    ZOMBIE(CreatureID.ZOMBIE, 22, 8, 7, 8, 35);

    private final CreatureID id;
    private final int health;
    private final int healthIncrement;
    private final int attack;
    private final int attackIncrement;
    private final int experienceDropFactor;

    CreaturePreset(CreatureID id, int health, int healthIncrement, int attack, int attackIncrement, int experienceDropFactor) {
        this.id = id;
        this.health = health;
        this.healthIncrement = healthIncrement;
        this.attack = attack;
        this.attackIncrement = attackIncrement;
        this.experienceDropFactor = experienceDropFactor;
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

    public int getExperienceDropFactor() {
        return experienceDropFactor;
    }
}
