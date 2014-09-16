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
        super(name, 1, 50, 4, CreatureID.HERO);
    }

    /**
     * Rest until the creature is completely healed.
     */
    public void rest() {
        setCurHealth(getMaxHealth());
        IO.writeString("You are completely rested.");
    }

    /**
     * Print the name of the player's current location and list all creatures
     * and items the player can see.
     */
    public void look() {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.MARGIN).append(getLocation().getName());

        builder.append('\n').append(Constants.LINE_1);
        
        builder.append(getLocation().getCreaturesString());

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
                dropWeapon();
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
        builder.append(Constants.MARGIN).append(String.format("%s (%s)\n", name, this.id));
        builder.append(Constants.MARGIN).append(String.format("%-20s%10d\n", "Level", getLevel()));
        builder.append(Constants.MARGIN).append(
                String.format("%-20s%10s\n", "Experience", String.format("%d/%d", getExperience(), getExperienceToNextLevel())));
        builder.append(Constants.MARGIN).append(String.format("%-20s%10d\n", "Gold", gold));
        builder.append(Constants.MARGIN).append(String.format("%-20s%10s\n", "Health", String.format("%d/%d", curHealth, maxHealth)));
        builder.append(Constants.MARGIN).append(String.format("%-20s%10d", "Attack", attack));
        return builder.toString();

    }

    private String getWeaponStatusString() {
        if (weapon == null) {
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
        IO.writeString(getHeroStatusString() + "\n" + getWeaponStatusString());
    }

}
