package game;

import java.util.List;
import static utils.Utils.MARGIN;

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
        System.out.println("You are completely rested.");
    }

    /**
     * Print the name of the player's current location and list all creatures and items the player can see.
     */
    public void look() {
        StringBuilder builder = new StringBuilder();
        builder.append(MARGIN).append(getLocation().getName());

        builder.append('\n').append(Game.LINE_1);

        // Creature count must be greater than one in order not to take the hero into account.
        if (getLocation().getCreatureCount() > 1) {
            for (Creature aCreature : getLocation().getCreatures()) {
                if (aCreature.getId() != CreatureID.HERO) {
                    builder.append('\n').append(MARGIN).append(aCreature.toShortString());
                }
            }
        } else {
            builder.append('\n').append("You do not see any creatures here.");
        }

        builder.append('\n').append(Game.LINE_1);

        if (getLocation().getItemCount() > 0) {
            for (Item curItem : getLocation().getItems()) {
                builder.append('\n').append(MARGIN).append(curItem.toShortString());
            }
        } else {
            builder.append('\n').append("You do not see any items here.");
        }

        builder.append('\n').append(Game.LINE_1);

        System.out.println(builder.toString());
    }

    /**
     * Picks a weapon from the ground.
     */
    public void pickWeapon(String[] words) {
        List<Weapon> visibleWeapons = getLocation().getVisibleWeapons();
        int selectedIndex = selectWeapon(visibleWeapons);
        if (selectedIndex != -1) {
            dropWeapon();
            equipWeapon(visibleWeapons.get(selectedIndex));
            getLocation().removeItem(visibleWeapons.get(selectedIndex));
        }

    }

    /**
     * Tries to destroy an item from the current location.
     */
    public void destroyItem(String[] words) {
        if (words.length == 1) {

        } else {
            Item target = getLocation().findItem(words[1]);
            if (target != null) {
                if (target.isDestructible()) {
                    getLocation().removeItem(target);
                    Game.writeString(getName() + " destroyed " + target.getName() + ".");
                } else {
                    Game.writeString(target.getName() + " is indestructible.");
                }
            } else {

            }
        }
    }

    /**
     * Let the user select one item from a list of options.
     *
     * @return the index of the item in the list. (-1 if the user aborted)
     */
    private int selectWeapon(List<Weapon> options) {
        int index;
        StringBuilder builder = new StringBuilder("0. Abort\n");
        for (int i = 1; i - 1 < options.size(); i++) {
            builder.append(i).append(". ").append(options.get(i - 1).toShortString()).append("\n");
        }
        System.out.print(builder.toString());
        while (true) {
            System.out.print("> ");
            try {
                index = Integer.parseInt(Game.SCANNER.nextLine());
            } catch (NumberFormatException exception) {
                System.out.println(Game.INVALID_INPUT);
                continue;
            }
            if (0 <= index && index <= options.size()) {
                break;
            }
            System.out.println(Game.INVALID_INPUT);
        }
        return index - 1;

    }

    public Creature selectTarget(String[] inputWords) {
        if (inputWords.length == 1) {
            return selectTargetFromList();
        } else {
            String targetName = inputWords[1];
            for (Creature possibleTarget : getLocation().getVisibleCreatures(this)) {
                if (targetName.compareToIgnoreCase(possibleTarget.getName()) == 0) {
                    return possibleTarget;
                }
            }
        }
        return null;
    }

    /**
     * Let the player choose a target to attack.
     */
    private Creature selectTargetFromList() {
        StringBuilder builder = new StringBuilder();
        builder.append("0. Abort\n");
        List<Creature> visible = this.getLocation().getVisibleCreatures(this);
        for (int i = 1; i - 1 < visible.size(); i++) {
            builder.append(i).append(". ").append(visible.get(i - 1).getName()).append("\n");
        }
        Game.writeString(builder.toString());
        int index;
        while (true) {
            try {
                index = Integer.parseInt(Game.readString());
            } catch (NumberFormatException exception) {
                Game.writeString(Game.INVALID_INPUT);
                continue;
            }
            if (0 <= index && index <= visible.size()) {
                break;
            }
            Game.writeString(Game.INVALID_INPUT);
        }
        if (index == 0) {
            return null;
        }
        return visible.get(index - 1);
    }

    private String getHeroStatusString() {
        StringBuilder builder = new StringBuilder();
        builder.append(MARGIN).append(String.format("%s (%s)\n", name, this.id));
        builder.append(MARGIN).append(String.format("%-20s%10d\n", "Level", level));
        builder.append(MARGIN).append(String.format("%-20s%10s\n", "Experience",
                String.format("%d/%d", experience, getExperienceToNextLevel())));
        builder.append(MARGIN).append(String.format("%-20s%10d\n", "Gold", gold));
        builder.append(MARGIN).append(String.format("%-20s%10s\n", "Health",
                String.format("%d/%d", curHealth, maxHealth)));
        builder.append(MARGIN).append(String.format("%-20s%10d", "Attack", attack));
        return builder.toString();

    }

    private String getWeaponStatusString() {
        if (weapon == null) {
            return "You are not carrying a weapon.";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(MARGIN).append(String.format("%-20s%10s\n", "Name", getWeapon().getName()));
        builder.append(MARGIN).append(String.format("%-20s%10s\n", "Damage", getWeapon().getDamage()));
        builder.append(MARGIN).append(String.format("%-20s%10s", "Integrity",
                String.format("%d/%d", getWeapon().getCurIntegrity(), getWeapon().getMaxIntegrity())));
        return builder.toString();
    }

    public void printHeroStatus() {
        Game.writeString(getHeroStatusString());
    }

    public void printWeaponStatus() {
        Game.writeString(getWeaponStatusString());
    }

    /**
     * Output a table with both the hero's status and his weapon's status.
     */
    public void printAllStatus() {
        Game.writeString(getHeroStatusString() + "\n" + getWeaponStatusString());
    }

}
