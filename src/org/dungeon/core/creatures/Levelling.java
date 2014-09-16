package org.dungeon.core.creatures;

public interface Levelling {

    public int getExperience();

    public int getRequiredExperience();

    public void addExperience(int amount);

}
