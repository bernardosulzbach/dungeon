package org.dungeon.core.creatures;

/**
 * An enumerated type of presets for creature creation. Created by Bernardo Sulzbach on 16/09/14.
 */
public enum CreaturePreset {

    BAT(CreatureID.BAT, 12, 4, 5, 2, 15);

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
