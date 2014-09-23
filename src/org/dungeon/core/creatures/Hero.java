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

import org.dungeon.core.counters.BattleLog;
import org.dungeon.core.counters.CounterMap;
import org.dungeon.core.creatures.enums.CreatureID;
import org.dungeon.core.creatures.enums.CreatureType;
import org.dungeon.core.items.Inventory;
import org.dungeon.core.items.Item;
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

    private BattleLog battleLog;

    public Hero(String name) {
        super(CreatureType.HERO, CreatureID.HERO, name);
        setLevel(1);
        setMaxHealth(50);
        setCurHealth(50);
        setCreatureAttack(new CreatureAttackWeapon(4));
        setHealthIncrement(20);
        setAttackIncrement(4);
        setInventory(new Inventory(this, 4));
        setBattleLog(new BattleLog());
    }

    public BattleLog getBattleLog() {
        return battleLog;
    }

    public void setBattleLog(BattleLog battleLog) {
        this.battleLog = battleLog;
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

    //
    //
    // Selection methods.
    //
    //
    public Item selectInventoryItem(String[] inputWords) {
        if (inputWords.length == 1) {
            return Utils.selectFromList(getInventory().getItems());
        } else {
            return getInventory().findItem(inputWords[1]);
        }
    }

    public Item selectLocationItem(String[] inputWords) {
        if (inputWords.length == 1) {
            return Utils.selectFromList(getLocation().getItems());
        } else {
            return getLocation().findItem(inputWords[1]);
        }
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

    //
    //
    // Inventory methods.
    //
    //

    /**
     * Attempts to pick and item and add it to the inventory.
     */
    public void pickItem(String[] inputWords) {
        Item selectedItem = selectLocationItem(inputWords);
        if (selectedItem != null) {
            getInventory().addItem(selectedItem);
            getLocation().removeItem(selectedItem);
        }
    }

    //
    //
    // Weapon methods.
    //
    //

    /**
     * Tries to equip an item from the inventory.
     */
    public void parseEquip(String[] inputWords) {
        Item selectedItem = selectInventoryItem(inputWords);
        if (selectedItem.isWeapon()) {
            equipWeapon(selectedItem);
        } else {
            IO.writeString("You cannot equip that.");
        }
    }

    /**
     * Attempts to drop an item from the hero's inventory.
     */
    public void dropItem(String[] inputWords) {
        Item selectedItem = selectInventoryItem(inputWords);
        if (selectedItem != null) {
            if (selectedItem == getWeapon()) {
                unequipWeapon();
            }
            getInventory().removeItem(selectedItem);
            getLocation().addItem(selectedItem);
            IO.writeString("Dropped " + selectedItem.getName() + ".");
        } else {
            IO.writeString("Item not found in inventory.");
        }
    }

    public void printInventory() {
        getInventory().printItems();
    }

    /**
     * Attempts to eat an item from the ground.
     */
    public void eatItem(String[] inputWords) {
        Item selectedItem = selectInventoryItem(inputWords);
        if (selectedItem.isFood()) {
            IO.writeString("You ate " + selectedItem.getName() + ".");
            addHealth(selectedItem.getNutrition());
            if (isCompletelyHealed()) {
                IO.writeString("You are completely healed.");
            }
            // TODO: re-implement eating experience here.
            if (selectedItem.isBroken() && !selectedItem.isRepairable()) {
                getInventory().removeItem(selectedItem);
            }
        } else {
            IO.writeString("You can only eat food.");
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
            if (target.isRepairable()) {
                if (!target.isBroken()) {

                    target.setCurIntegrity(0);
                    IO.writeString(getName() + " crashed " + target.getName() + ".");
                }
            } else {
                getLocation().removeItem(target);
                IO.writeString(getName() + " destroyed " + target.getName() + ".");
            }
        }
    }

    //
    //
    // Status methods.
    //
    //

    private String getHeroStatusString() {
        StringBuilder builder = new StringBuilder();

        builder.append(Constants.MARGIN).append(String.format("%s (%s)\n", getName(), getId()));
        builder.append(Constants.MARGIN).append(String.format("%-20s%20d\n", "Level", getLevel()));

        String experienceFraction = String.format("%d/%d", getExperience(), getExperienceToNextLevel());
        builder.append(Constants.MARGIN).append(String.format("%-20s%20s\n", "Experience", experienceFraction));

        builder.append(Constants.MARGIN).append(String.format("%-20s%20d\n", "Gold", getGold()));

        String healthFraction = String.format("%d/%d", getCurHealth(), getMaxHealth());
        builder.append(Constants.MARGIN).append(String.format("%-20s%20s\n", "Health", healthFraction));

        builder.append(Constants.MARGIN).append(String.format("%-20s%20d", "Attack", getAttack()));
        return builder.toString();

    }

    private String getWeaponStatusString() {
        if (getWeapon() == null) {
            return "You are not equipping a weapon.";
        } else {
            return getWeapon().getStatusString();
        }
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
