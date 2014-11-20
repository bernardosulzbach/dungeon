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

import org.dungeon.core.achievements.AchievementTracker;
import org.dungeon.core.counters.BattleStatistics;
import org.dungeon.core.counters.ExplorationLog;
import org.dungeon.core.game.Game;
import org.dungeon.core.game.Location;
import org.dungeon.core.game.TimeConstants;
import org.dungeon.core.game.World;
import org.dungeon.core.items.CreatureInventory;
import org.dungeon.core.items.FoodComponent;
import org.dungeon.core.items.Item;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Utils;
import org.joda.time.DateTime;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Hero class that defines the creature that the player controls.
 */
public class Hero extends Creature {

    private final double minimumLuminosity = 0.3;

    private final DateTime dateOfBirth;
    private final ExplorationLog explorationLog;
    private final BattleStatistics battleStatistics;
    private final AchievementTracker achievementTracker;

    public Hero(String name) {
        super(makeHeroBlueprint(name));
        setInventory(new CreatureInventory(this, 3));
        dateOfBirth = new DateTime(1952, 6, 4, 8, 32);
        explorationLog = new ExplorationLog();
        battleStatistics = new BattleStatistics();
        achievementTracker = new AchievementTracker();
    }

    private static CreatureBlueprint makeHeroBlueprint(String name) {
        CreatureBlueprint heroBlueprint = new CreatureBlueprint();
        heroBlueprint.setId(Constants.HERO_ID);
        heroBlueprint.setName(name);
        heroBlueprint.setType("Hero");
        heroBlueprint.setAttack(4);
        heroBlueprint.setAttackIncrement(2);
        heroBlueprint.setAttackAlgorithmID("HERO");
        heroBlueprint.setMaxHealth(50);
        heroBlueprint.setCurHealth(50);
        heroBlueprint.setMaxHealthIncrement(20);
        return heroBlueprint;
    }

    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    public ExplorationLog getExplorationLog() {
        return explorationLog;
    }

    public AchievementTracker getAchievementTracker() {
        return achievementTracker;
    }

    private DateTime getDateOfBirth() {
        return dateOfBirth;
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
        Location location = getLocation(); // Avoid multiple calls to the getter.
        IO.writeString(location.getName());
        IO.writeNewLine();
        if (location.getLuminosity() < minimumLuminosity) {
            IO.writeString(Constants.CANT_SEE_ANYTHING);
        } else {
            if (location.getCreatureCount() == 1) {
                if (Game.RANDOM.nextBoolean()) {
                    IO.writeString("You do not see anyone here.");
                } else {
                    IO.writeString("Only you are in this location.");
                }
            } else {
                String curName;
                int curCount;
                ArrayList<String> alreadyListedCreatures = new ArrayList<String>();
                alreadyListedCreatures.add(getName()); // Avoid listing the Hero.
                for (Creature creature : location.getCreatures()) {
                    curName = creature.getName();
                    if (!alreadyListedCreatures.contains(curName)) {
                        alreadyListedCreatures.add(curName);
                        curCount = location.getCreatureCount(creature.getId());
                        if (curCount > 1) {
                            IO.writeKeyValueString(curName, Integer.toString(curCount));
                        } else {
                            IO.writeString(curName);
                        }
                    }
                }
            }
            if (getLocation().getItemCount() != 0) {
                IO.writeNewLine();
                for (Item curItem : getLocation().getItemList()) {
                    IO.writeString(curItem.toListEntry());
                }
            }
        }
    }

    Item selectInventoryItem(String[] inputWords) {
        if (inputWords.length == 1) {
            Utils.printMissingArgumentsMessage();
            return null;
        } else {
            return getInventory().findItem(Arrays.copyOfRange(inputWords, 1, inputWords.length));
        }
    }

    Item selectLocationItem(String[] inputWords) {
        if (inputWords.length == 1) {
            IO.writeString("Pick what?", Color.ORANGE);
            return null;
        } else {
            return getLocation().getInventory().findItem(Arrays.copyOfRange(inputWords, 1, inputWords.length));
        }
    }

    /**
     * The method that lets the hero attack a target.
     *
     * @param inputWords the array of the individual words of the user input.
     * @return an integer representing how many seconds the battle lasted.
     */
    public int attackTarget(String[] inputWords) {
        if (getLocation().getLuminosity() < minimumLuminosity) {
            IO.writeString("It is too dark to find your target.");
        } else {
            Creature target = selectTarget(inputWords);
            if (target != null) {
                return Game.battle(this, target) * TimeConstants.BATTLE_TURN_DURATION;
            }
        }
        return 0;
    }

    /**
     * Attempts to select a target from the current location using the player input.
     *
     * @param inputWords the array of the individual words of the user input.
     * @return a target Creature.
     */
    public Creature selectTarget(String[] inputWords) {
        if (inputWords.length == 1) {
            if (Game.RANDOM.nextBoolean()) {
                IO.writeString("Attack what?", Color.ORANGE);
            } else {
                IO.writeString("You must specify a target.", Color.ORANGE);
            }
            return null;
        } else {
            return getLocation().findCreature(Arrays.copyOfRange(inputWords, 1, inputWords.length));
        }
    }

    /**
     * Attempts to pick and item and add it to the inventory.
     */
    public void pickItem(String[] inputWords) {
        if (getLocation().getLuminosity() < minimumLuminosity) {
            IO.writeString("It is too dark for you too see anything.");
            return;
        }
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
        IO.writeString("Items: " + getInventory().getItemCount() + "/" + getInventory().getLimit(), Color.CYAN);
        getInventory().printItems();
    }

    /**
     * Attempts to eat an item from the ground.
     */
    public void eatItem(String[] inputWords) {
        Item selectedItem = selectInventoryItem(inputWords);
        if (selectedItem != null) {
            if (selectedItem.isFood()) {
                FoodComponent food = selectedItem.getFoodComponent();
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
    public void destroyItem(String[] inputWords) {
        Item target;
        if (inputWords.length == 1) {
            IO.writeString(Constants.INVALID_INPUT);
            target = null;
        } else {
            target = getLocation().getInventory().findItem(Arrays.copyOfRange(inputWords, 1, inputWords.length));
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
        IO.writeString(getName());
        IO.writeKeyValueString("Level", Integer.toString(getLevel()));
        if (Game.getGameState().isUsingBars()) {
            IO.writeNamedBar("Experience", getLevelProgress(), Constants.LEVEL_BAR_COLOR);
            IO.writeNamedBar("Health", (double) getCurHealth() / getMaxHealth(), Constants.HEALTH_BAR_COLOR);
        } else {
            IO.writeKeyValueString("Experience", String.format("%d/%d", getExperience(), getExperienceToNextLevel()));
            IO.writeKeyValueString("Health", String.format("%d/%d", getCurHealth(), getMaxHealth()));
        }
        IO.writeKeyValueString("Attack", Integer.toString(getAttack()));
    }

    public void printWeaponStatus() {
        if (hasWeapon()) {
            Item heroWeapon = getWeapon();
            IO.writeString(heroWeapon.getQualifiedName());
            IO.writeKeyValueString("Damage", Integer.toString(heroWeapon.getDamage()));
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
    public void printAge() {
        String age = Utils.dateDifferenceToString(getDateOfBirth(), Game.getGameState().getWorld().getWorldDate());
        IO.writeString(String.format("You are %s old.", age), Color.CYAN);
    }

    /**
     * Makes the hero read the current date and time as well as he can.
     *
     * @return how many seconds the action lasted.
     */
    public int printDateAndTime() {
        World world = getLocation().getWorld();
        int timeSpent = 2;
        if (hasClock()) {
            if (hasWeapon() && getWeapon().isClock() && !getWeapon().isBroken()) {
                // Reading the time from an equipped clock is the fastest possible action.
                timeSpent += 2;
            } else {
                // The hero needed to pick up a watch or something from his inventory, consuming more time.
                timeSpent += 8;
            }
            // Prints whatever the clock shows.
            IO.writeString(getClock().getClockComponent().getTimeString());
        }

        DateTime worldDate = world.getWorldDate();
        IO.writeString("You think it is " + Constants.DATE_FORMAT.print(worldDate) + ".");

        String holiday = org.dungeon.utils.Holiday.getHoliday(worldDate);
        if (holiday != null) {
            IO.writeString("You remember it is " + holiday + ".");
        }

        DateTime dob = getDateOfBirth();
        if (worldDate.getMonthOfYear() == dob.getMonthOfYear() && worldDate.getDayOfMonth() == dob.getDayOfMonth()) {
            IO.writeString("Today is your birthday.");
        }

        IO.writeString("You can see that it is " + world.getPartOfDay().toString().toLowerCase() + ".");
        return timeSpent;
    }

}
