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
package org.dungeon.core.game;

import org.dungeon.core.achievements.Achievement;
import org.dungeon.core.counters.CounterMap;
import org.dungeon.core.creatures.Creature;
import org.dungeon.core.creatures.Hero;
import org.dungeon.core.creatures.enums.CreatureID;
import org.dungeon.core.creatures.enums.CreaturePreset;
import org.dungeon.core.creatures.enums.CreatureType;
import org.dungeon.core.items.FoodPreset;
import org.dungeon.core.items.Item;
import org.dungeon.core.items.Weapon;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Hints;
import org.dungeon.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//
// Why is this class marked final?
//
// A quote from Effective Java (2nd)
//
// There are a few more restrictions that a class must obey to allow inheritance.
//
// Constructors must not invoke overridable methods, directly or indirectly. If you violate this rule, program failure will result.
//
// The superclass constructor runs before the subclass constructor, so the overriding method in the subclass will be invoked before the
// subclass constructor has run. If the overriding method depends on any initialization performed by the subclass constructor, the method
// will not behave as expected.
//
// Bernardo Sulzbach (mafagafogigante) [16/09/2014]: although I can avoid the usage of setters in the constructor, I decided to follow the 
// item 17 in the above-mentioned book "Design and document for inheritance, or else prohibit it.";
//
public final class Campaign implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Achievement> achievements;

    private final CounterMap<CreatureID> battleIDCounter;
    private final CounterMap<CreatureType> battleTypeCounter;

    private final World campaignWorld;

    private final Hero campaignHero;
    private Point heroPosition;

    private boolean saved;
    private int nextHintIndex;

    private int totalAchievementsCount;
    private int unlockedAchievementsCount;

    public Campaign() {
        battleIDCounter = new CounterMap<CreatureID>();
        battleTypeCounter = new CounterMap<CreatureType>();

        achievements = createDemoAchievements();

        // Set the number of achievements the campaign has.
        setTotalAchievementsCount(achievements.size());

        campaignHero = new Hero("Seth");
        heroPosition = new Point(0, 0);

        campaignWorld = createDemoWorld();
    }

    private List<Achievement> createDemoAchievements() {
        List<Achievement> demoAchievements = new ArrayList<Achievement>();

        CounterMap<CreatureID> suicideSolutionRequirements = new CounterMap<CreatureID>();
        CounterMap<CreatureID> baneRequirements = new CounterMap<CreatureID>();
        CounterMap<CreatureID> catRequirements = new CounterMap<CreatureID>();
        CounterMap<CreatureID> evilBastardRequirements = new CounterMap<CreatureID>();
        CounterMap<CreatureID> stayDeadRequirements = new CounterMap<CreatureID>();

        // Suicide Solution requires one battle against the Hero himself.
        suicideSolutionRequirements.incrementCounter(CreatureID.HERO);
        // Bane requires six battles against bats.
        baneRequirements.incrementCounter(CreatureID.BAT, 6);
        // Cat requires four battles against rats.
        catRequirements.incrementCounter(CreatureID.RAT, 4);
        // Evil Bastard requires one battle against a rabbit.
        evilBastardRequirements.incrementCounter(CreatureID.RABBIT);
        // Stay Dead requires two battles against a zombie.
        stayDeadRequirements.incrementCounter(CreatureID.ZOMBIE, 2);

        demoAchievements.add(new Achievement("Suicide Solution", "Attempt to kill yourself.", suicideSolutionRequirements));
        demoAchievements.add(new Achievement("Bane", "Kill 6 bats.", baneRequirements));
        demoAchievements.add(new Achievement("Cat", "Kill 4 rats.", catRequirements));
        demoAchievements.add(new Achievement("Evil Bastard", "Kill an innocent rabbit.", evilBastardRequirements));
        demoAchievements.add(new Achievement("Stay Dead", "Kill 2 zombies.", stayDeadRequirements));

        return demoAchievements;
    }

    private World createDemoWorld() {
        World world = new World();

        // Create a location on the hero's position.
        Point forest = new Point(0, 0);
        world.addLocation(new Location("Forest"), forest);

        world.addCreature(campaignHero, forest);
        world.addItem(new Weapon("Stick", 6, 20), forest);

        // Another forest location.
        Point forest2 = new Point(0, 1);
        world.addLocation(new Location("Forest"), forest2);

        // Beasts
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.BAT, 1, 2), forest2);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.RABBIT, 1, 2), forest2);
        world.addCreature(Creature.createCreature(CreaturePreset.RAT, 1), forest2);
        world.addCreature(Creature.createCreature(CreaturePreset.SPIDER, 1), forest2);

        // Items
        world.addItem(new Weapon("Spear", 10, 12), forest2);

        // A clearing.
        Point clearing = new Point(0, 2);
        world.addLocation(new Location("Clearing"), clearing);

        // Beasts
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.RABBIT, 1, 3), clearing);
        world.addCreature(Creature.createCreature(CreaturePreset.RAT, 1), clearing);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.SPIDER, 1, 2), clearing);
        world.addCreature(Creature.createCreature(CreaturePreset.WOLF, 1), clearing);
        // Items
        world.addItem(new Weapon("Dagger", 13, 15), clearing);
        // Food
        world.addItem(Item.createItem(FoodPreset.CHERRY), clearing);

        Point roadToTheFort = new Point(1, 2);
        world.addLocation(new Location("Road to The Fort"), roadToTheFort);

        // Beasts
        world.addCreature(Creature.createCreature(CreaturePreset.BEAR, 2), roadToTheFort);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.RABBIT, 1, 2), roadToTheFort);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.WOLF, 1, 2), roadToTheFort);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.ZOMBIE, 1, 3), roadToTheFort);

        // Items
        world.addItem(new Weapon("Mace", 15, 20), roadToTheFort);

        Point cave = new Point(-1, 2);
        world.addLocation(new Location("Cave"), cave);

        // Beasts
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.BAT, 1, 4), cave);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.BEAR, 1, 3), cave);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.RAT, 1, 2), cave);
        world.addCreature(Creature.createCreature(CreaturePreset.SPIDER, 1), cave);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.WOLF, 1, 2), cave);
        world.addCreature(Creature.createCreature(CreaturePreset.ZOMBIE, 2), cave);

        // Items
        world.addItem(new Weapon("Longsword", 18, 25), cave);

        return world;
    }

    public World getWorld() {
        return campaignWorld;
    }

    public Hero getHero() {
        return campaignHero;
    }

    public Point getHeroPosition() {
        return heroPosition;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public int getNextHintIndex() {
        return nextHintIndex;
    }

    public void setNextHintIndex(int nextHintIndex) {
        this.nextHintIndex = nextHintIndex;
    }

    private void incrementNextHintIndex() {
        int newIndex = getNextHintIndex() + 1;
        if (newIndex == Hints.hintsArray.length) {
            setNextHintIndex(0);
        } else {
            setNextHintIndex(newIndex);
        }
    }

    public int getTotalAchievementsCount() {
        return totalAchievementsCount;
    }

    public void setTotalAchievementsCount(int totalAchievementsCount) {
        this.totalAchievementsCount = totalAchievementsCount;
    }

    public int getUnlockedAchievementsCount() {
        return unlockedAchievementsCount;
    }

    private void incrementUnlockedAchievementsCount() {
        this.unlockedAchievementsCount++;
    }

    /**
     * Prints all unlocked achievements.
     */
    public void printUnlockedAchievements() {
        StringBuilder builder = new StringBuilder();
        builder.append("Progress: ").append(getUnlockedAchievementsCount()).append('/').append(getTotalAchievementsCount());
        for (Achievement a : achievements) {
            if (a.isUnlocked()) {
                builder.append("\n").append(a.toOneLineString());
            }
        }
        IO.writeString(builder.toString());
    }

    /**
     * Refreshes the campaign. Should be called after the player plays a turn.
     */
    public void refresh() {
        refreshAchievements();
    }

    private void refreshAchievements() {
        for (Achievement a : achievements) {
            if (a.update(battleIDCounter)) {
                incrementUnlockedAchievementsCount();
            }
        }
    }

    public void parseHeroWalk(String[] inputWords) {
        if (inputWords.length == 1) {
            Direction walkDirection = Utils.selectFromList(Arrays.asList(Direction.values()));
            if (walkDirection != null) {
                heroWalk(walkDirection);
            }
            return;
        } else {
            String secondWord = inputWords[1];
            for (Direction dir : Direction.values()) {
                if (dir.toString().equalsIgnoreCase(secondWord)) {
                    heroWalk(dir);
                    return;
                }
            }
        }
        IO.writeString(Constants.INVALID_INPUT);
    }

    public void heroWalk(Direction dir) {
        Point destination = new Point(heroPosition, dir);
        if (getWorld().hasLocation(destination)) {
            getWorld().moveCreature(campaignHero, heroPosition, destination);
            heroPosition = destination;
            campaignHero.setLocation(getWorld().getLocation(destination));
        } else {
            IO.writeString(Constants.WALK_BLOCKED);
        }
    }

    public void addBattle(Creature target) {
        this.battleIDCounter.incrementCounter(target.getId());
        this.battleTypeCounter.incrementCounter(target.getType());
    }

    /**
     * Prints the next hint.
     */
    public void printNextHint() {
        IO.writeString(Hints.hintsArray[getNextHintIndex()]);
        incrementNextHintIndex();
    }

    /**
     * Returns some basic information about the hero.
     * Used by the 'whoami' command.
     */
    public String getHeroInfo() {
        // TODO: consider returning something a bit more substantial.
        return getHero().getName();
    }
}
