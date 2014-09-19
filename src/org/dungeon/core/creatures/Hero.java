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

import org.dungeon.core.counters.CounterMap;
import org.dungeon.core.creatures.enums.CreatureID;
import org.dungeon.core.creatures.enums.CreatureType;
import org.dungeon.core.items.Item;
import org.dungeon.core.items.Weapon;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Hero class that defines the creature that the player controls.
 */
public class Hero extends Creature {

    private static final long serialVersionUID = 1L;

    // Has the player already attempted suicide?
    private boolean attemptedSuicide;

    public Hero(String name) {
        super(CreatureType.HERO, CreatureID.HERO, name);
        setLevel(1);
        setMaxHealth(50);
        setCurHealth(50);
        setAttack(4);
        setHealthIncrement(20);
        setAttackIncrement(4);
    }

    public boolean isAttemptedSuicide() {
        return attemptedSuicide;
    }

    public void setAttemptedSuicide(boolean attemptedSuicide) {
        this.attemptedSuicide = attemptedSuicide;
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
                String line;
                int creatureCount = counter.getCounter(id);
                // If there is only one creature, do not print its count.
                if (creatureCount == 1) {
                    line = String.format("%-20s", id.toString());
                } else {
                    line = String.format("%-20s(%d)", id.toString(), creatureCount);
                }
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
        if (inputWords.length == 1) {
            List<Creature> locationCreatures = new ArrayList<Creature>(getLocation().getCreatures());
            locationCreatures.remove(this);
            return Utils.selectFromList(locationCreatures);
        } else {
            return getLocation().findCreature(inputWords[1]);
        }
    }

    /**
     * Picks a weapon from the ground.
     */
    public void pickWeapon(String[] words) {
        Item selectedItem;
        if (words.length == 1) {
            selectedItem = Utils.selectFromList(getLocation().getItems());
        } else {
            selectedItem = getLocation().findItem(words[1]);
        }
        if (selectedItem != null) {
            if (selectedItem instanceof Weapon) {
                if (hasWeapon()) {
                    dropWeapon();
                }
                equipWeapon((Weapon) selectedItem);
                getLocation().removeItem(selectedItem);
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

    /**
     * Try to hit a target. If the creature has a weapon, it will be used to perform the attack. Otherwise, the creature will attack with
     * its bare hands.
     */
    @Override
    public void hit(Creature target) {
        int hitDamage;
        // Check that there is a weapon and that it is not broken.
        if (getWeapon() != null && !getWeapon().isBroken()) {
            // Check if the attack is a miss.
            if (getWeapon().isMiss()) {
                IO.writeString(getName() + " misses.");
                return;
            } else {
                hitDamage = getWeapon().getDamage();
                target.takeDamage(hitDamage);
                getWeapon().decrementIntegrity();
            }
        } else {
            hitDamage = this.getAttack();
            target.takeDamage(hitDamage);
        }
        IO.writeString(String.format("%s inflicted %d damage points to %s.\n", getName(), hitDamage, target.getName()));

    }

}
