package org.dungeon.core.creatures;

/**
 * CreatureBlueprint class that defines attributes used to generate a certain creature.
 * <p/>
 * Created by Bernardo on 23/10/2014.
 */
public class CreatureBlueprint {

    private String id;
    private String type;
    private String name;
    private int curHealth;
    private int maxHealth;
    private int maxHealthIncrement;
    private int attack;
    private int attackIncrement;
    private String attackAlgorithmID;
    private int experienceDropFactor;

    public CreatureBlueprint() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurHealth() {
        return curHealth;
    }

    public void setCurHealth(int curHealth) {
        this.curHealth = curHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getMaxHealthIncrement() {
        return maxHealthIncrement;
    }

    public void setMaxHealthIncrement(int maxHealthIncrement) {
        this.maxHealthIncrement = maxHealthIncrement;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getAttackIncrement() {
        return attackIncrement;
    }

    public void setAttackIncrement(int attackIncrement) {
        this.attackIncrement = attackIncrement;
    }

    public String getAttackAlgorithmID() {
        return attackAlgorithmID;
    }

    public void setAttackAlgorithmID(String attackAlgorithmID) {
        this.attackAlgorithmID = attackAlgorithmID;
    }

    public int getExperienceDropFactor() {
        return experienceDropFactor;
    }

    public void setExperienceDropFactor(int experienceDropFactor) {
        this.experienceDropFactor = experienceDropFactor;
    }

}
