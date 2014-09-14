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
package game;

import java.util.List;
import utils.Constants;

/**
 * Hero class that defines the creature that the player controls.
 */
public class Hero extends Creature {

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
     * Print the name of the player's current location and list all creatures and items the player can see.
     */
    public void look() {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.MARGIN).append(getLocation().getName());

        builder.append('\n').append(Constants.LINE_1);

        // Creature count must be different than one in order not to take the hero into account.
        if (getLocation().getCreatureCount() == 1) {
            builder.append('\n').append(Constants.MARGIN).append("You do not see any creatures here.");
        } else {
            for (Creature aCreature : getLocation().getCreatures()) {
                if (aCreature.getId() != CreatureID.HERO) {
                    builder.append('\n').append(Constants.MARGIN).append(aCreature.toShortString());
                }
            }
        }

        builder.append('\n').append(Constants.LINE_1);

        if (getLocation().getItemCount() == 0) {
            builder.append('\n').append(Constants.MARGIN).append("You do not see any items here.");
        } else {
            for (Item curItem : getLocation().getItems()) {
                builder.append('\n').append(Constants.MARGIN).append(curItem.toShortString());
            }
        }

        builder.append('\n').append(Constants.LINE_1);

        IO.writeString(builder.toString());
    }

    public Creature selectTarget(String[] inputWords) {
        List<Creature> locationCreatures = getLocation().getCreatures();
        if (inputWords.length == 1) {
            return selectFromList(locationCreatures);
        } else {
            return getLocation().findCreature(inputWords[1]);
        }
    }

    /**
     * Picks a weapon from the ground.
     */
    public void pickWeapon(String[] words) {
        Weapon selectedWeapon = selectFromList(getLocation().getVisibleWeapons());
        if (selectedWeapon != null) {
            dropWeapon();
            equipWeapon(selectedWeapon);
            getLocation().removeItem(selectedWeapon);
        }
    }

    /**
     * Tries to destroy an item from the current location.
     */
    public void destroyItem(String[] words) {
        Item target;
        if (words.length == 1) {
            target = selectFromList(getLocation().getItems());
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

    /**
     * Method that let the player select a Selectable object from a List.
     */
    public static <T extends Selectable> T selectFromList(List<T> list) {
        StringBuilder builder = new StringBuilder("0. Abort\n");
        int index = 1;
        for (Selectable aSelectable : list) {
            builder.append(index).append(". ").append(aSelectable.toSelectionEntry()).append('\n');
            index++;
        }
        IO.writeString(builder.toString());
        int choice = -1;
        while (true) {
            try {
                choice = Integer.parseInt(IO.readString());
            } catch (NumberFormatException exception) {
                IO.writeString(Constants.INVALID_INPUT);
                continue;
            }
            if (choice < 0 || choice > list.size()) {
                IO.writeString(Constants.INVALID_INPUT);
            } else {
                break;
            }
        }
        if (choice == 0) {
            return null;
        }
        return list.get(choice - 1);
    }

    private String getHeroStatusString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.MARGIN).append(String.format("%s (%s)\n", name, this.id));
        builder.append(Constants.MARGIN).append(String.format("%-20s%10d\n", "Level", level));
        builder.append(Constants.MARGIN).append(String.format("%-20s%10s\n", "Experience",
                String.format("%d/%d", experience, getExperienceToNextLevel())));
        builder.append(Constants.MARGIN).append(String.format("%-20s%10d\n", "Gold", gold));
        builder.append(Constants.MARGIN).append(String.format("%-20s%10s\n", "Health",
                String.format("%d/%d", curHealth, maxHealth)));
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
        builder.append(Constants.MARGIN).append(String.format("%-20s%10s", "Integrity",
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
