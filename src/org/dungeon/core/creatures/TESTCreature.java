package org.dungeon.core.creatures;

import org.dungeon.core.game.Weapon;

/**
 * The new creature class. Created by Bernardo on 16/09/14.
 */
public class TESTCreature {

    private CreatureType type;
    private CreatureID id;
    private String name;
    private int gold;
    private int level;
    private int health;
    private int healthIncrement;
    private int attack;
    private int attackIncrement;
    private int experienceDropFactor;

    private static TESTCreature createCreature(CreatureType type, CreatureID id, String name, int health, int healthIncrement, int attack,
            int attackIncrement, int experienceDropFactor) {
        return new TESTCreature(type, id, name, health, healthIncrement, attack, attackIncrement, experienceDropFactor);
    }

    private static TESTCreature createCreature(CreaturePreset preset) {
        return new TESTCreature(preset.getType(), preset.getId(), preset.getId().getName(), preset.getHealth(),
                preset.getHealthIncrement(), preset.getAttack(), preset.getAttackIncrement(), preset.getExperienceDropFactor());
    }

    public TESTCreature() {

    }

    private TESTCreature(CreatureType type, CreatureID id, String name, int health, int healthIncrement, int attack, int attackIncrement,
            int experienceDropFactor) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.health = health;
        this.healthIncrement = healthIncrement;
        this.attack = attack;
        this.attackIncrement = attackIncrement;
        this.experienceDropFactor = experienceDropFactor;
    }

    public void setType(CreatureType type) {
        this.type = type;
    }

    public void setId(CreatureID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setHealthIncrement(int healthIncrement) {
        this.healthIncrement = healthIncrement;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setAttackIncrement(int attackIncrement) {
        this.attackIncrement = attackIncrement;
    }

    public void setExperienceDropFactor(int experienceDropFactor) {
        this.experienceDropFactor = experienceDropFactor;
    }

    public CreatureType getType() {
        return type;
    }

    public CreatureID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGold() {
        return gold;
    }

    public int getLevel() {
        return level;
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

    public void addGold(int amount) {
        setGold(getGold() + amount);
    }

}
