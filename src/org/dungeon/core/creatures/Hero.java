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
import org.dungeon.core.game.TimeConstants;
import org.dungeon.core.items.FoodComponent;
import org.dungeon.core.items.Inventory;
import org.dungeon.core.items.Item;
import org.dungeon.io.IO;
import org.dungeon.io.WriteStyle;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Utils;
import org.joda.time.Period;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Hero class that defines the creature that the player controls.
 */
public class Hero extends Creature {

    private static final long serialVersionUID = 1L;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private BattleLog battleLog;
    private Date dateOfBirth;
    private double minimumLuminosity = 0.3;

    public Hero(String name) {
        super(CreatureType.HERO, CreatureID.HERO, name);
        setLevel(1);
        setMaxHealth(50);
        setCurHealth(50);
        setHealthIncrement(20);

        setAttack(3);
        setAttackIncrement(2);
        setAttackAlgorithm(AttackAlgorithmID.HERO);

        setInventory(new Inventory(this, 2));
        setBattleLog(new BattleLog());

        // Currently, the hero's birthday is hardcoded.
        Calendar calendar = Calendar.getInstance();
        calendar.set(1952, Calendar.JUNE, 4, 8, 32, 55);  // Yes, I know his date of birth THIS precisely.
        dateOfBirth = calendar.getTime();
    }

    public BattleLog getBattleLog() {
        return battleLog;
    }

    public void setBattleLog(BattleLog battleLog) {
        this.battleLog = battleLog;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public double getMinimumLuminosity() {
        return minimumLuminosity;
    }

    /**
     * Checks if the Hero is completely healed.
     */
    private boolean isCompletelyHealed() {
        return getMaxHealth() == getCurHealth();
    }

    /**
     * Rest until the creature is healed to 60% of its health points.
     * <p/>
     * Returns the number of seconds the hero rested.
     */
    public int rest() {
        if (getCurHealth() >= (int) (0.6 * getMaxHealth())) {
            IO.writeString("You are already rested.", WriteStyle.MARGIN);
            return 0;
        } else {
            double fractionHealed = 0.6 - (double) getCurHealth() / (double) getMaxHealth();
            IO.writeString("Resting...", WriteStyle.MARGIN);
            setCurHealth((int) (0.6 * getMaxHealth()));
            IO.writeString("You feel rested.", WriteStyle.MARGIN);
            return (int) (TimeConstants.REST_COMPLETE * fractionHealed);
        }
    }

    /**
     * Print the name of the player's current location and list all creatures and items the player can see.
     */
    public void look() {
        StringBuilder builder = new StringBuilder();
        builder.append(getLocation().getName());

        builder.append('\n').append(Constants.LINE_1);
        if (getLocation().getLuminosity() >= getMinimumLuminosity()) {
            if (getLocation().getCreatureCount() == 1) {
                builder.append('\n').append(Constants.NO_CREATURES);
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
                    builder.append('\n').append(line);
                }
            }

            builder.append('\n').append(Constants.LINE_1);

            if (getLocation().getItemCount() == 0) {
                builder.append('\n').append(Constants.NO_ITEMS);
            } else {
                for (Item curItem : getLocation().getItems()) {
                    builder.append('\n').append(curItem.toSelectionEntry());
                }
            }
        } else {
            builder.append('\n').append(Constants.CANT_SEE_ANYTHING);
        }

        builder.append('\n').append(Constants.LINE_1);

        IO.writeString(builder.toString(), WriteStyle.MARGIN);
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
            Item queryResult = getInventory().findItem(inputWords[1]);
            if (queryResult == null) {
                IO.writeString(Constants.ITEM_NOT_FOUND_IN_INVENTORY, WriteStyle.MARGIN);
            }
            return queryResult;
        }
    }

    public Item selectLocationItem(String[] inputWords) {
        if (inputWords.length == 1) {
            return Utils.selectFromList(getLocation().getItems());
        } else {
            Item queryResult = getLocation().findItem(inputWords[1]);
            if (queryResult == null) {
                IO.writeString(Constants.ITEM_NOT_FOUND_IN_LOCATION, WriteStyle.MARGIN);
            }
            return queryResult;
        }
    }

    public Creature selectTarget(String[] inputWords) {
        if (getLocation().getLuminosity() >= getMinimumLuminosity()) {
            if (inputWords.length == 1) {
                List<Creature> locationCreatures = new ArrayList<Creature>(getLocation().getCreatures());
                locationCreatures.remove(this);
                return Utils.selectFromList(locationCreatures);
            } else {
                return getLocation().findCreature(inputWords[1]);
            }
        } else {
            IO.writeString(Constants.CANT_SEE_ANYTHING, WriteStyle.MARGIN);
            return null;
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
            //
            // Bernardo Sulzbach (mafagafogigante): It is not necessary to check for a full inventory before trying to
            // add an item, but attempting to add an item to a full Inventory will display a warning.
            //
            // This warning exists because the game should not print a inventory full message to the player when trying
            // to add something to the full inventory of an NPC.
            //
            if (getInventory().isFull()) {
                IO.writeString(Constants.INVENTORY_FULL, WriteStyle.MARGIN);
            } else {
                getInventory().addItem(selectedItem);
                getLocation().removeItem(selectedItem);
            }
        }
    }

    /**
     * Tries to equip an item from the inventory.
     */
    public void parseEquip(String[] inputWords) {
        Item selectedItem = selectInventoryItem(inputWords);
        if (selectedItem != null) {
            if (selectedItem.isWeapon()) {
                equipWeapon(selectedItem);
            } else {
                IO.writeString("You cannot equip that.", WriteStyle.MARGIN);
            }
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
            IO.writeString("Dropped " + selectedItem.getName() + ".", WriteStyle.MARGIN);
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
        if (selectedItem != null) {
            if (selectedItem.isFood()) {
                FoodComponent food = selectedItem.getFood();
                addHealth(food.getNutrition());
                selectedItem.decrementIntegrity(food.getIntegrityDecrementOnEat());
                // TODO: make not-enough-for-a-full-bite food heal less than a enough-for-a-full-bite food would.
                if (selectedItem.isBroken() && !selectedItem.isRepairable()) {
                    IO.writeString("You ate " + selectedItem.getName() + ".", WriteStyle.MARGIN);
                    getInventory().removeItem(selectedItem);
                } else {
                    IO.writeString("You ate a bit of " + selectedItem.getName() + ".", WriteStyle.MARGIN);
                }
                if (isCompletelyHealed()) {
                    IO.writeString("You are completely healed.", WriteStyle.MARGIN);
                }
                // When addExperience() is called a message is printed, so this line must come after the eat message.
                addExperience(food.getExperienceOnEat());
            } else {
                IO.writeString("You can only eat food.", WriteStyle.MARGIN);
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
            if (target.isRepairable()) {
                if (!target.isBroken()) {

                    target.setCurIntegrity(0);
                    IO.writeString(getName() + " crashed " + target.getName() + ".", WriteStyle.MARGIN);
                }
            } else {
                getLocation().removeItem(target);
                IO.writeString(getName() + " destroyed " + target.getName() + ".", WriteStyle.MARGIN);
            }
        }
    }

    public boolean hasClock() {
        for (Item item : getInventory().getItems()) {
            if (item.isClock()) {
                return true;
            }
        }
        return false;
    }

    public Item getClock() {
        for (Item item : getInventory().getItems()) {
            if (item.isClock()) {
                return item;
            }
        }
        return null;
    }

    //
    //
    // Weapon methods.
    //
    //
    public void equipWeapon(Item weapon) {
        if (hasWeapon()) {
            if (getWeapon() == weapon) {
                IO.writeString(getName() + " is already equipping " + weapon.getName() + ".", WriteStyle.MARGIN);
                return;
            } else {
                unequipWeapon();
            }
        }
        this.setWeapon(weapon);
        IO.writeString(getName() + " equipped " + weapon.getName() + ".", WriteStyle.MARGIN);
    }

    public void unequipWeapon() {
        if (hasWeapon()) {
            IO.writeString(getName() + " unequipped " + getWeapon().getName() + ".", WriteStyle.MARGIN);
            setWeapon(null);
        } else {
            IO.writeString(Constants.NOT_EQUIPPING_A_WEAPON, WriteStyle.MARGIN);
        }
    }

    //
    //
    // Status methods.
    //
    //
    private String getHeroStatusString() {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("%s (%s)\n", getName(), getId()));
        builder.append(String.format("%-20s%20d\n", "Level", getLevel()));

        String experienceFraction = String.format("%d/%d", getExperience(), getExperienceToNextLevel());
        builder.append(String.format("%-20s%20s\n", "Experience", experienceFraction));

        builder.append(String.format("%-20s%20d\n", "Gold", getGold()));

        String healthFraction = String.format("%d/%d", getCurHealth(), getMaxHealth());
        builder.append(String.format("%-20s%20s\n", "Health", healthFraction));

        builder.append(String.format("%-20s%20d", "Attack", getAttack()));
        return builder.toString();

    }

    private String getWeaponStatusString() {
        if (getWeapon() == null) {
            return Constants.NOT_EQUIPPING_A_WEAPON;
        } else {
            return getWeapon().getStatusString();
        }
    }

    public void printHeroStatus() {
        IO.writeString(getHeroStatusString(), WriteStyle.MARGIN);
    }

    public void printWeaponStatus() {
        IO.writeString(getWeaponStatusString(), WriteStyle.MARGIN);
    }

    /**
     * Output a table with both the hero's status and his weapon's status.
     */
    public void printAllStatus() {
        // Check to see if there is a weapon.
        if (getWeapon() != null) {
            IO.writeString(getHeroStatusString() + "\n" + getWeaponStatusString(), WriteStyle.MARGIN);
        } else {
            // If the hero is not carrying a weapon, avoid printing that he is not carrying a weapon.
            printHeroStatus();
        }
    }

    /**
     * Prints the hero's age.
     */
    // TODO: Write tests to this function.
    // TODO: Extract to ageDiffToString (an util).
    public void printAge() {
        Period p = new Period(getDateOfBirth().getTime(), getLocation().getWorld().getWorldDate().getTime());
        int years = p.getYears();
        int months = p.getMonths();
        int days = p.getDays();
        StringBuilder builder = new StringBuilder();
        if (years != 0) {
            if (years == 1) {
                builder.append(years).append(" year");
            } else {
                builder.append(years).append(" years");
            }
        }
        if (months != 0) {
            if (builder.length() != 0) {
                if (days == 0) {
                    builder.append(" and ");
                } else {
                    builder.append(", ");
                }
            }
            if (months == 1) {
                builder.append(months).append(" month");
            } else {
                builder.append(months).append(" months");
            }
        }
        if (days != 0) {
            if (builder.length() != 0) {
                builder.append(" and ");
            }
            if (days == 1) {
                builder.append(days).append(" day");
            } else {
                builder.append(days).append(" days");
            }
        }
        if (builder.length() == 0) {
            builder.append("Less than a day.");
        } else {
            builder.append(".");
        }
        IO.writeString(builder.toString(), WriteStyle.MARGIN);
    }

    public void printDateAndTime() {
        // TODO: checking time should cost some time.
        long time = getLocation().getWorld().getWorldDate().getTime();
        if (hasClock()) {
            // TODO: this repeated getClock() is terrible. Fix it.
            IO.writeString(getClock().getClock().getTimeString(time), WriteStyle.MARGIN);
        } else {
            IO.writeString(dateFormat.format(time) + " " + "(" + getLocation().getWorld().getDayPart() + ")", WriteStyle.MARGIN);
        }
    }
}
