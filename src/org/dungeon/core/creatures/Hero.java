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
import org.dungeon.utils.Constants;
import org.joda.time.Period;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Hero class that defines the creature that the player controls.
 */
public class Hero extends Creature {

    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private BattleLog battleLog;
    private final Date dateOfBirth;
    private final double minimumLuminosity = 0.3;

    public Hero(String name) {
        super("HERO", "HERO", name);
        setLevel(1);
        setMaxHealth(50);
        setCurHealth(50);
        setHealthIncrement(20);

        setAttack(3);
        setAttackIncrement(2);
        setAttackAlgorithm("HERO");

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

    void setBattleLog(BattleLog battleLog) {
        this.battleLog = battleLog;
    }

    Date getDateOfBirth() {
        return dateOfBirth;
    }

    double getMinimumLuminosity() {
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
            IO.writeString("You are already rested.");
            return 0;
        } else {
            double fractionHealed = 0.6 - (double) getCurHealth() / (double) getMaxHealth();
            IO.writeString("Resting...");
            setCurHealth((int) (0.6 * getMaxHealth()));
            IO.writeString("You feel rested.");
            return (int) (TimeConstants.REST_COMPLETE * fractionHealed);
        }
    }

    /**
     * Print the name of the player's current location and list all creatures and items the player can see.
     */
    public void look() {
        StringBuilder builder = new StringBuilder();
        builder.append(getLocation().getName()).append('\n');
        builder.append(Constants.LINE_1).append('\n');
        if (getLocation().getLuminosity() >= getMinimumLuminosity()) {
            if (getLocation().getCreatureCount() == 1) {
                // If there is only the hero, say that there are no creatures.
                builder.append(Constants.NO_CREATURES).append('\n');
            } else {
                CounterMap<String> counter = new CounterMap<String>();
                for (Creature creature : getLocation().getCreatures()) {
                    if (!creature.getName().equals(getName())) {
                        counter.incrementCounter(creature.getName());
                    }
                }
                for (String name : counter.keySet()) {
                    String line;
                    int creatureCount = counter.getCounter(name);
                    // If there is only one creature, do not print its count.
                    if (creatureCount == 1) {
                        line = String.format("%-20s", name);
                    } else {
                        line = String.format("%-20s(%d)", name, creatureCount);
                    }
                    builder.append(line).append('\n');
                }
            }
            builder.append(Constants.LINE_1).append('\n');

            if (getLocation().getItemCount() == 0) {
                builder.append(Constants.NO_ITEMS).append('\n');
            } else {
                for (Item curItem : getLocation().getItems()) {
                    builder.append(curItem.toSelectionEntry()).append('\n');
                }
            }
        } else {
            builder.append(Constants.CANT_SEE_ANYTHING).append('\n');
        }

        builder.append(Constants.LINE_1).append('\n');

        IO.writeString(builder.toString());
    }

    //
    //
    // Selection methods.
    //
    //
    Item selectInventoryItem(String[] inputWords) {
        if (inputWords.length == 1) {
            IO.writeString(Constants.INVALID_INPUT);
            return null;
        } else {
            Item queryResult = getInventory().findItem(inputWords[1]);
            if (queryResult == null) {
                IO.writeString(Constants.ITEM_NOT_FOUND_IN_INVENTORY);
            }
            return queryResult;
        }
    }

    Item selectLocationItem(String[] inputWords) {
        if (inputWords.length == 1) {
//            return Utils.selectFromList(getLocation().getItems());
            IO.writeString(Constants.INVALID_INPUT);
            return null;
        } else {
            Item queryResult = getLocation().findItem(inputWords[1]);
            if (queryResult == null) {
                IO.writeString(Constants.ITEM_NOT_FOUND_IN_LOCATION);
            }
            return queryResult;
        }
    }

    public Creature selectTarget(String[] inputWords) {
        if (getLocation().getLuminosity() >= getMinimumLuminosity()) {
            if (inputWords.length == 1) {
                IO.writeString(Constants.INVALID_INPUT);
                return null;
            } else {
                return getLocation().findCreature(inputWords[1]);
            }
        } else {
            IO.writeString(Constants.CANT_SEE_ANYTHING);
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
            if (getInventory().isFull()) {
                IO.writeString(Constants.INVENTORY_FULL);
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
                IO.writeString("You cannot equip that.");
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
            IO.writeString("Dropped " + selectedItem.getName() + ".");
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
                    IO.writeString("You ate " + selectedItem.getName() + ".");
                    getInventory().removeItem(selectedItem);
                } else {
                    IO.writeString("You ate a bit of " + selectedItem.getName() + ".");
                }
                if (isCompletelyHealed()) {
                    IO.writeString("You are completely healed.");
                }
                // When addExperience() is called a message is printed, so this line must come after the eat message.
                addExperience(food.getExperienceOnEat());
            } else {
                IO.writeString("You can only eat food.");
            }
        }
    }

    /**
     * Tries to destroy an item from the current location.
     */
    public void destroyItem(String[] words) {
        Item target;
        if (words.length == 1) {
            IO.writeString(Constants.INVALID_INPUT);
            target = null;
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

    boolean hasClock() {
        for (Item item : getInventory().getItems()) {
            if (item.isClock()) {
                return true;
            }
        }
        return false;
    }

    Item getClock() {
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
    void equipWeapon(Item weapon) {
        if (hasWeapon()) {
            if (getWeapon() == weapon) {
                IO.writeString(getName() + " is already equipping " + weapon.getName() + ".");
                return;
            } else {
                unequipWeapon();
            }
        }
        this.setWeapon(weapon);
        IO.writeString(getName() + " equipped " + weapon.getName() + ".");
    }

    public void unequipWeapon() {
        if (hasWeapon()) {
            IO.writeString(getName() + " unequipped " + getWeapon().getName() + ".");
            setWeapon(null);
        } else {
            IO.writeString(Constants.NOT_EQUIPPING_A_WEAPON);
        }
    }

    public void printHeroStatus() {
        //IO.writeString(getHeroStatusString());
        IO.writeString(String.format("%s (%s)\n", getName(), getId()));
        IO.writeFilledLine("Level", Integer.toString(getLevel()));
        IO.writeFilledLine("Experience", String.format("%d/%d", getExperience(), getExperienceToNextLevel()));
        // TODO: Enable Health coloring. Red / Yellow / Green / ...
        IO.writeFilledLine("Health", String.format("%d/%d", getCurHealth(), getMaxHealth()));
        IO.writeFilledLine("Attack", Integer.toString(getAttack()));
    }

    public void printWeaponStatus() {
        if (hasWeapon()) {
            Item heroWeapon = getWeapon();
            IO.writeString(heroWeapon.getIntegrityString() + " " + heroWeapon.getName());
            IO.writeFilledLine("Damage", Integer.toString(heroWeapon.getDamage()));
        } else {
            IO.writeString(Constants.NOT_EQUIPPING_A_WEAPON);
        }
    }

    /**
     * Output a table with both the hero's status and his weapon's status.
     */
    public void printAllStatus() {
        printHeroStatus();
        if (getWeapon() != null) {
            printWeaponStatus();
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
        IO.writeString(builder.toString());
    }

    public void printDateAndTime() {
        // TODO: checking time should cost some time.
        long time = getLocation().getWorld().getWorldDate().getTime();
        if (hasClock()) {
            // TODO: this repeated getClock() is terrible. Fix it.
            IO.writeString(getClock().getClock().getTimeString(time));
        } else {
            IO.writeString(dateFormat.format(time) + " " + "(" + getLocation().getWorld().getDayPart() + ")");
        }
    }
}
