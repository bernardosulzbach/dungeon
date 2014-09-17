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

import java.util.List;
import org.dungeon.core.counters.CounterMap;

import org.dungeon.core.game.Item;
import org.dungeon.core.game.Weapon;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Utils;

/**
 * Hero class that defines the creature that the player controls.
 */
public class Hero extends Creature {

    private static final long serialVersionUID = 1L;

    public Hero(String name) {
        setId(CreatureID.HERO);
        setName(name);
        setLevel(1);
        setMaxHealth(50);
        setCurHealth(50);
        setAttack(4);
        setHealthIncrement(10);
        setAttackIncrement(4);
    }

    /**
     * Checks if the Hero is completely healed.
     */
    private boolean isCompletelyHealed() {
        return getMaxHealth() == getCurHealth();
    }

    /**
     * Rest until the creature is completely healed.
     */
    public void rest() {
        if (isCompletelyHealed()) {
            IO.writeString("You are already completely healed.");
        } else {
            IO.writeString("Resting...");
            setCurHealth(getMaxHealth());
            IO.writeString("You are completely rested.");
        }
    }

    /**
     * Print the name of the player's current location and list all creatures and items the player can see.
     */
    public void look() {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.MARGIN).append(getLocation().getName());

        builder.append('\n').append(Constants.LINE_1);

        if (getLocation().getCreatureCount() == 1) {
            builder.append('\n').append(Constants.MARGIN).append(Constants.NO_CREATURES);
        } else {
            CounterMap<CreatureID> counter = new CounterMap<CreatureID>();
            for (Creature creature : getLocation().getCreatures()) {
                if (creature.getId() != CreatureID.HERO) {
                    counter.incrementCounter(creature.getId());
                }
            }
            for (CreatureID id : counter.keySet()) {
                String line = String.format("%-20s(%d)", id.toString(), counter.getCounter(id));
                builder.append('\n').append(Constants.MARGIN).append(line);
            }
        }

        builder.append('\n').append(Constants.LINE_1);

        if (getLocation().getItemCount() == 0) {
            builder.append('\n').append(Constants.MARGIN).append(Constants.NO_ITEMS);
        } else {
            for (Item curItem : getLocation().getItems()) {
                builder.append('\n').append(Constants.MARGIN).append(curItem.toSelectionEntry());
            }
        }

        builder.append('\n').append(Constants.LINE_1);

        IO.writeString(builder.toString());
    }

    public Creature selectTarget(String[] inputWords) {
        List<Creature> locationCreatures = getLocation().getCreatures();
        if (inputWords.length == 1) {
            return Utils.selectFromList(locationCreatures);
        } else {
            return getLocation().findCreature(inputWords[1]);
        }
    }

    /**
     * Picks a weapon from the ground.
     */
    public void pickWeapon(String[] words) {
        Item selectedWeapon = Utils.selectFromList(getLocation().getItems());
        if (selectedWeapon != null) {
            if (selectedWeapon instanceof Weapon) {
                if (getWeapon() != null) {
                    dropWeapon();
                }
                equipWeapon((Weapon) selectedWeapon);
                getLocation().removeItem(selectedWeapon);
            } else {
                IO.writeString("You cannot equip that.");
            }
        }
    }

    /**
     * Tries to destroy an item from the current location.
     */
    public void destroyItem(String[] words) {
        Item target;
        if (words.length == 1) {
            target = Utils.selectFromList(getLocation().getItems());
        } else {
            target = getLocation().findItem(words[1]);
        }
        if (target != null) {
            if (target.isDestructible()) {
                getLocation().removeItem(target);
                IO.writeString(getName() + " destroyed " + target.getName() + ".");
            } else {
                IO.writeString(target.getName() + " is indestructible.");
            }
        }
    }

    private String getHeroStatusString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.MARGIN).append(String.format("%s (%s)\n", getName(), getId()));
        builder.append(Constants.MARGIN).append(String.format("%-20s%10d\n", "Level", getLevel()));
        builder.append(Constants.MARGIN).append(
                String.format("%-20s%10s\n", "Experience", String.format("%d/%d", getExperience(), getExperienceToNextLevel())));
        builder.append(Constants.MARGIN).append(String.format("%-20s%10d\n", "Gold", getGold()));
        builder.append(Constants.MARGIN).append(
                String.format("%-20s%10s\n", "Health", String.format("%d/%d", getCurHealth(), getMaxHealth())));
        builder.append(Constants.MARGIN).append(String.format("%-20s%10d", "Attack", getAttack()));
        return builder.toString();

    }

    private String getWeaponStatusString() {
        if (getWeapon() == null) {
            return "You are not carrying a weapon.";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.MARGIN).append(String.format("%-20s%10s\n", "Name", getWeapon().getName()));
        builder.append(Constants.MARGIN).append(String.format("%-20s%10s\n", "Damage", getWeapon().getDamage()));
        builder.append(Constants.MARGIN).append(
                String.format("%-20s%10s", "Integrity",
                        String.format("%d/%d", getWeapon().getCurIntegrity(), getWeapon().getMaxIntegrity())));
        return builder.toString();
    }

    public void printHeroStatus() {
        IO.writeString(getHeroStatusString());
    }

    public void printWeaponStatus() {
        IO.writeString(getWeaponStatusString());
    }

    /**
     * Output a table with both the hero's status and his weapon's status.
     */
    public void printAllStatus() {
        // Check to see if there is a weapon.
        if (getWeapon() != null) {
            IO.writeString(getHeroStatusString() + "\n" + getWeaponStatusString());
        } else {
            // If the hero is not carrying a weapon, avoid printing that he is not carrying a weapon.
            printHeroStatus();
        }
    }

}
