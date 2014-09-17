package org.dungeon.core.creatures.enums;

/**
 * An enumerated type of presets for creature creation. Created by Bernardo Sulzbach on 16/09/14.
 */
public enum CreaturePreset {

    FROG(CreatureType.CRITTER, CreatureID.FROG, 10, 5, 3, 2, 12),
    RABBIT(CreatureType.CRITTER, CreatureID.RABBIT, 10, 5, 3, 2, 10),
    
    BAT(CreatureType.BEAST, CreatureID.BAT, 12, 4, 6, 4, 15),
    BEAR(CreatureType.BEAST, CreatureID.BEAR, 20, 10, 10, 7, 40),
    RAT(CreatureType.BEAST, CreatureID.RAT, 14, 4, 8, 5, 20),
    SPIDER(CreatureType.BEAST, CreatureID.SPIDER, 16, 6, 7, 7, 25),
    WOLF(CreatureType.BEAST, CreatureID.WOLF, 18, 7, 6, 8, 30),
    ZOMBIE(CreatureType.BEAST, CreatureID.ZOMBIE, 22, 8, 7, 8, 35);

    private final CreatureType type;
    private final CreatureID id;
    private final int health;
    private final int healthIncrement;
    private final int attack;
    private final int attackIncrement;
    private final int experienceDropFactor;

    CreaturePreset(CreatureType type, CreatureID id, int health, int healthIncrement, int attack, int attackIncrement, int experienceDropFactor) {
        this.type = type;
        this.id = id;
        this.health = health;
        this.healthIncrement = healthIncrement;
        this.attack = attack;
        this.attackIncrement = attackIncrement;
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

    public int getExperienceDropFactor() {
        return experienceDropFactor;
    }
}
