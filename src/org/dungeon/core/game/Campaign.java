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
import org.dungeon.core.achievements.BattleAchievement;
import org.dungeon.core.counters.CounterMap;
import org.dungeon.core.creatures.Creature;
import org.dungeon.core.creatures.Hero;
import org.dungeon.core.creatures.enums.CreatureID;
import org.dungeon.core.creatures.enums.CreaturePreset;
import org.dungeon.core.creatures.enums.CreatureType;
import org.dungeon.core.items.Item;
import org.dungeon.core.items.ItemPreset;
import org.dungeon.io.IO;
import org.dungeon.utils.*;

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
// Constructors must not invoke overridable methods, directly or indirectly. If you violate this rule, program failure
// will result.
//
// The superclass constructor runs before the subclass constructor, so the overriding method in the subclass will be
// invoked before the subclass constructor has run. If the overriding method depends on any initialization performed by
// the subclass constructor, the method will not behave as expected.
//
// Bernardo Sulzbach (mafagafogigante) [16/09/2014]: although I can avoid the usage of setters in the constructor, I
// will follow the item 17 in the above-mentioned book "Design and document for inheritance, or else prohibit it.";
//
// Bernardo Sulzbach (mafagafogigante) [20/09/2014]: there is no reason to still mark this class final. As I do not plan
// to ever inherit from it and changing the final modifier is something ridiculously simple, I will just leave it the
// way it is.
//
public final class Campaign implements Serializable {

    private static final long serialVersionUID = 1L;

    private final CommandHistory commandHistory;

    private final List<Achievement> achievements;

    private final World campaignWorld;

    private final Hero campaignHero;
    private Point heroPosition;

    private boolean saved;
    private int nextHintIndex;

    private int totalAchievementsCount;
    private int unlockedAchievementsCount;

    public Campaign() {
        commandHistory = new CommandHistory();

        achievements = createDemoAchievements();

        // Set the number of achievements the campaign has.
        setTotalAchievementsCount(achievements.size());

        campaignHero = new Hero("Seth");
        heroPosition = new Point(0, 0);

        campaignWorld = createDemoWorld();
    }

    private List<Achievement> createDemoAchievements() {
        List<Achievement> list = new ArrayList<Achievement>();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Battle achievements that do not require specific kills.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        list.add(new BattleAchievement("First Blood", "Kill a creature.", 10, 1, 0, null, null, null));
        list.add(new BattleAchievement("Killer", "Kill 10 creatures.", 100, 10, 0, null, null, null));
        list.add(new BattleAchievement("Die hard", "Take 10 turns to kill a creature.", 150, 0, 10, null, null, null));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Battle achievements that rely on the number of kill with a specific weapon.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        CounterMap<String> onTheStickReqs = new CounterMap<String>();
        onTheStickReqs.incrementCounter(ItemPreset.STICK.getId(), 2);
        list.add(new BattleAchievement("On the Stick!", "Kill 2 creatures with the Stick.", 20, 0, 0, null, null, onTheStickReqs));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Battle achievements that rely on the kill count of a specific creature ID.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Bane requires six battles against bats.
        CounterMap<CreatureID> baneRequirements = new CounterMap<CreatureID>();
        baneRequirements.incrementCounter(CreatureID.BAT, 6);
        list.add(new BattleAchievement("Bane", "Kill 6 bats.", 50, 0, 0, baneRequirements, null, null));
        // Cat requires four battles against rats.
        CounterMap<CreatureID> catRequirements = new CounterMap<CreatureID>();
        catRequirements.incrementCounter(CreatureID.RAT, 4);
        list.add(new BattleAchievement("Cat", "Kill 4 rats.", 40, 0, 0, catRequirements, null, null));
        // Evil Bastard requires one battle against a rabbit.
        CounterMap<CreatureID> evilBastardRequirements = new CounterMap<CreatureID>();
        evilBastardRequirements.incrementCounter(CreatureID.RABBIT);
        list.add(new BattleAchievement("Evil Bastard", "Kill an innocent rabbit.", 5, 0, 0, evilBastardRequirements, null, null));
        // Stay Dead requires two battles against a zombie.
        CounterMap<CreatureID> stayDeadRequirements = new CounterMap<CreatureID>();
        stayDeadRequirements.incrementCounter(CreatureID.ZOMBIE, 2);
        list.add(new BattleAchievement("Stay Dead", "Kill 2 zombies.", 50, 0, 0, stayDeadRequirements, null, null));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Battle achievements that rely on the kill count of a specific type.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Professional Coward requires killing 10 critters.
        CounterMap<CreatureType> professionalCowardRequirements = new CounterMap<CreatureType>();
        professionalCowardRequirements.incrementCounter(CreatureType.CRITTER, 10);
        list.add(new BattleAchievement("Professional Coward", "Kill 10 critters.", 100, 0, 0, null, professionalCowardRequirements, null));

        return list;
    }

    private World createDemoWorld() {
        World world = new World();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Forest (two locations)
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Create a location on the hero's position.
        Point forest = new Point(0, 0);
        world.addLocation(new Location("Forest"), forest);

        world.addCreature(campaignHero, forest);
        world.addItem(Item.createItem(ItemPreset.STICK), forest);

        // Another forest location.
        Point forest2 = new Point(0, 1);
        world.addLocation(new Location("Forest"), forest2);

        // Beasts
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.BAT, 1, 2), forest2);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.RABBIT, 1, 2), forest2);
        world.addCreature(Creature.createCreature(CreaturePreset.RAT, 1), forest2);
        world.addCreature(Creature.createCreature(CreaturePreset.SPIDER, 1), forest2);

        // Weapons
        world.addItem(Item.createItem(ItemPreset.SPEAR), forest2);
        // Food
        world.addItem(Item.createItem(ItemPreset.APPLE), forest2);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // A clearing.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Point clearing = new Point(0, 2);
        world.addLocation(new Location("Clearing"), clearing);

        // Beasts
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.RABBIT, 1, 3), clearing);
        world.addCreature(Creature.createCreature(CreaturePreset.RAT, 1), clearing);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.SPIDER, 1, 2), clearing);
        world.addCreature(Creature.createCreature(CreaturePreset.WOLF, 1), clearing);

        // Weapons
        world.addItem(Item.createItem(ItemPreset.DAGGER), clearing);
        // Food
        world.addItem(Item.createItem(ItemPreset.CHERRY), clearing);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Road to The Fort
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Point roadToTheFort = new Point(1, 2);
        world.addLocation(new Location("Road to The Fort"), roadToTheFort);

        // Beasts
        world.addCreature(Creature.createCreature(CreaturePreset.BEAR, 2), roadToTheFort);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.RABBIT, 1, 2), roadToTheFort);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.WOLF, 1, 2), roadToTheFort);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.ZOMBIE, 1, 3), roadToTheFort);

        // Weapons
        world.addItem(Item.createItem(ItemPreset.MACE), roadToTheFort);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Cave entrance
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Point caveEntrance = new Point(-1, 2);
        world.addLocation(new Location("Cave Entrance"), caveEntrance);

        // Beasts
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.BAT, 1, 2), caveEntrance);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.BEAR, 1, 1), caveEntrance);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.FROG, 2, 4), roadToTheFort);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.RAT, 1, 2), caveEntrance);
        world.addCreature(Creature.createCreature(CreaturePreset.SPIDER, 1), caveEntrance);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.WOLF, 1, 2), caveEntrance);
        world.addCreature(Creature.createCreature(CreaturePreset.ZOMBIE, 2), caveEntrance);

        // Weapons
        world.addItem(Item.createItem(ItemPreset.LONGSWORD), caveEntrance);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Cave
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Point cave = new Point(-2, 2);
        world.addLocation(new Location("Cave"), cave);

        // Beasts
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.BAT, 2, 6), cave);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.BEAR, 1, 2), cave);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.RAT, 2, 4), cave);
        world.addCreature(Creature.createCreature(CreaturePreset.SPIDER, 2), cave);
        world.addCreature(Creature.createCreature(CreaturePreset.ZOMBIE, 3), cave);

        // Weapons
        world.addItem(Item.createItem(ItemPreset.STAFF), cave);
        // Food
        world.addItem(Item.createItem(ItemPreset.WATERMELON), cave);

        return world;
    }

    public CommandHistory getCommandHistory() {
        return commandHistory;
    }

    public void printCommandCount() {
        IO.writeString("Commands issued: " + getCommandHistory().getCommandCount());
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
            if (a.update(getHero())) {
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
            String arg = inputWords[1];
            for (Direction dir : Direction.values()) {
                // (the strings are equal) or (the first characters of each string are equal)
                if (dir.toString().equalsIgnoreCase(arg) || StringUtils.firstEqualsIgnoreCase(dir.toString(), arg)) {
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
