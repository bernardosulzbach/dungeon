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
import org.dungeon.io.WriteStyle;
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

        list.add(new BattleAchievement("First Blood", "Kill a creature.",
                10, 1, 0, null, null, null));
        list.add(new BattleAchievement("Killer", "Kill 10 creatures.",
                100, 10, 0, null, null, null));
        list.add(new BattleAchievement("Die hard", "Take 10 turns to kill a creature.",
                150, 0, 10, null, null, null));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Battle achievements that rely on the kill count of a specific creature ID.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Bane requires six battles against bats.
        CounterMap<CreatureID> baneRequirements = new CounterMap<CreatureID>(CreatureID.BAT, 6);
        list.add(new BattleAchievement("Bane", "Kill 6 bats.",
                50, 0, 0, baneRequirements, null, null));
        // Cat requires four battles against rats.
        CounterMap<CreatureID> catRequirements = new CounterMap<CreatureID>(CreatureID.RAT, 4);
        list.add(new BattleAchievement("Cat", "Kill 4 rats.",
                40, 0, 0, catRequirements, null, null));
        // Evil Bastard requires one battle against a rabbit.
        CounterMap<CreatureID> evilBastardRequirements = new CounterMap<CreatureID>(CreatureID.RABBIT, 1);
        list.add(new BattleAchievement("Evil Bastard", "Kill an innocent rabbit.",
                5, 0, 0, evilBastardRequirements, null, null));
        // Stay Dead requires two battles against a zombie.
        CounterMap<CreatureID> stayDeadRequirements = new CounterMap<CreatureID>(CreatureID.ZOMBIE, 2);
        list.add(new BattleAchievement("Stay Dead", "Kill 2 zombies.",
                50, 0, 0, stayDeadRequirements, null, null));

        CounterMap<CreatureID> dissectionRequirements = new CounterMap<CreatureID>(CreatureID.FROG, 5);
        list.add(new BattleAchievement("Dissection", "Kill 5 frogs.",
                25, 0, 0, dissectionRequirements, null, null));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Battle achievements that rely on the kill count of a specific type.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Professional Coward requires killing 10 critters.
        CounterMap<CreatureType> professionalCowardRequirements = new CounterMap<CreatureType>(CreatureType.CRITTER, 10);
        list.add(new BattleAchievement("Professional Coward", "Kill 10 critters.",
                100, 0, 0, null, professionalCowardRequirements, null));

        CounterMap<CreatureType> hunterRequirements = new CounterMap<CreatureType>(CreatureType.BEAST, 10);
        list.add(new BattleAchievement("Hunter", "Kill 10 beasts.",
                125, 0, 0, null, hunterRequirements, null));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Battle achievements that rely on the number of kills with a specific weapon.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // An empty string is used to to register unarmed kills.
        CounterMap<String> fiveFingerDeathPunchReqs = new CounterMap<String>("", 1);
        list.add(new BattleAchievement("Five Finger Death Punch", "Kill a creature unarmed.",
                10, 0, 0, null, null, fiveFingerDeathPunchReqs));

        CounterMap<String> boxer = new CounterMap<String>("", 10);
        list.add(new BattleAchievement("Boxer", "Kill 10 creatures unarmed.",
                100, 0, 0, null, null, boxer));

        CounterMap<String> onTheStickReqs = new CounterMap<String>(ItemPreset.STICK.getId(), 2);
        list.add(new BattleAchievement("On the Stick!", "Kill 2 creatures with the Stick.",
                20, 0, 0, null, null, onTheStickReqs));

        CounterMap<String> sticksAndStonesReqs = new CounterMap<String>(ItemPreset.STICK.getId(), 5);
        list.add(new BattleAchievement("Sticks and Stones", "Kill 5 creatures with the Stone and 5 with the Stick.",
                40, 0, 0, null, null, sticksAndStonesReqs));
        sticksAndStonesReqs.incrementCounter(ItemPreset.STONE.getId(), 5);

        CounterMap<String> lumberjackReqs = new CounterMap<String>(ItemPreset.AXE.getId(), 10);
        list.add(new BattleAchievement("Lumberjack", "Kill 10 creatures with the Axe.",
                50, 0, 0, null, null, lumberjackReqs));

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
        world.addCreature(Creature.createCreature(CreaturePreset.FROG, 1), forest2);

        // Weapons
        world.addItem(Item.createItem(ItemPreset.SPEAR), forest2);
        // Food
        world.addItem(Item.createItem(ItemPreset.APPLE), forest2);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Clearing (two locations)
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Point clearing = new Point(0, 2);
        world.addLocation(new Location("Clearing"), clearing);

        // Beasts
        world.addCreature(Creature.createCreature(CreaturePreset.RABBIT, 1), clearing);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.FROG, 1, 2), clearing);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.SPIDER, 1, 2), clearing);
        world.addCreature(Creature.createCreature(CreaturePreset.SNAKE, 1), clearing);

        // Weapons
        world.addItem(Item.createItem(ItemPreset.DAGGER), clearing);
        // Food
        world.addItem(Item.createItem(ItemPreset.CHERRY), clearing);

        //Second clearing location.
        Point clearing2 = new Point(1, 2);
        world.addLocation(new Location("Clearing"), clearing2);

        // Beasts
        world.addCreature(Creature.createCreature(CreaturePreset.BEAR, 2), clearing2);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.SNAKE, 1, 2), clearing2);
        world.addCreature(Creature.createCreature(CreaturePreset.WOLF, 1), clearing2);
        world.addCreature(Creature.createCreature(CreaturePreset.SPIDER, 1), clearing2);

        // Weapons
        world.addItem(Item.createItem(ItemPreset.MACE), clearing2);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Road to The Fort (two locations)
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Point roadToTheFort = new Point(2, 2);
        world.addLocation(new Location("Road to The Fort"), roadToTheFort);

        // Beasts
        world.addCreature(Creature.createCreature(CreaturePreset.BEAR, 1), roadToTheFort);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.WOLF, 1, 2), roadToTheFort);
        world.addCreature(Creature.createCreature(CreaturePreset.ZOMBIE, 1), roadToTheFort);

        //Second roadToTheFort location.
        Point roadToTheFort2 = new Point(2, 3);
        world.addLocation(new Location("Road to The Fort"), roadToTheFort2);

        // Beasts
        world.addCreature(Creature.createCreature(CreaturePreset.FROG, 1), roadToTheFort2);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.BEAR, 1, 2), roadToTheFort2);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.WOLF, 2, 2), roadToTheFort2);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Fort
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Point fort = new Point(2, 4);
        world.addLocation(new Location("Fort"), fort);

        // Beasts
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.BEAR, 2, 2), fort);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.SKELETON, 1, 2), fort);
        world.addCreature(Creature.createCreature(CreaturePreset.ZOMBIE, 5), fort);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Cave entrance
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Point caveEntrance = new Point(-1, 2);
        world.addLocation(new Location("Cave Entrance"), caveEntrance);

        // Beasts
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.BAT, 2, 2), caveEntrance);
        world.addCreature(Creature.createCreature(CreaturePreset.SPIDER, 1), caveEntrance);
        world.addCreature(Creature.createCreature(CreaturePreset.RAT, 1), caveEntrance);
        world.addCreature(Creature.createCreature(CreaturePreset.SKELETON, 1), caveEntrance);

        // Weapons
        world.addItem(Item.createItem(ItemPreset.LONGSWORD), caveEntrance);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Cave
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Point cave = new Point(-2, 2);
        world.addLocation(new Location("Cave"), cave);

        // Beasts
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.BAT, 3, 2), cave);
        world.addCreature(Creature.createCreature(CreaturePreset.BEAR, 2), cave);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.RAT, 3, 3), cave);
        world.addCreature(Creature.createCreature(CreaturePreset.SPIDER, 2), cave);
        world.addCreature(Creature.createCreature(CreaturePreset.ZOMBIE, 3), cave);
        world.addCreature(Creature.createCreature(CreaturePreset.SKELETON, 3), cave);

        // Weapons
        world.addItem(Item.createItem(ItemPreset.STONE), cave);
        // Food
        world.addItem(Item.createItem(ItemPreset.WATERMELON), cave);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Bridge
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Point bridge = new Point(0, 3);
        world.addLocation(new Location("Bridge"), bridge);

        // Beasts
        world.addCreature(Creature.createCreature(CreaturePreset.SPIDER, 2), bridge);
        world.addCreature(Creature.createCreature(CreaturePreset.ZOMBIE, 2), bridge);
        world.addCreature(Creature.createCreature(CreaturePreset.SKELETON, 2), bridge);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Lake
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Point lake = new Point(0, 4);
        world.addLocation(new Location("Lake"), lake);

        // Beasts
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.FROG, 1, 3), lake);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.SNAKE, 2, 2), lake);
        world.addCreature(Creature.createCreature(CreaturePreset.SPIDER, 3), lake);

        // Weapons
        world.addItem(Item.createItem(ItemPreset.STAFF), lake);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Meadow
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Point meadow = new Point(3, 2);
        world.addLocation(new Location("Meadow"), meadow);

        // Beasts
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.RABBIT, 1, 2), meadow);
        world.addCreature(Creature.createCreature(CreaturePreset.SNAKE, 3), meadow);
        world.addCreature(Creature.createCreature(CreaturePreset.SPIDER, 2), meadow);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Clearing (Tent)
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Point clearing3 = new Point(4, 2);
        world.addLocation(new Location("Clearing"), clearing3);

        //Beasts
        world.addCreature(Creature.createCreature(CreaturePreset.SNAKE, 4), clearing3);
        world.addCreature(Creature.createCreature(CreaturePreset.ZOMBIE, 2), clearing3);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.RAT, 2, 2), clearing3);

        // Weapons
        world.addItem(Item.createItem(ItemPreset.AXE), clearing3);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Graveyard
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Point graveyard = new Point(4, 1);
        world.addLocation(new Location("Graveyard"), graveyard);
        //Beasts
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.SKELETON, 2, 2), graveyard);
        world.addCreatureArray(Creature.createCreatureArray(CreaturePreset.ZOMBIE, 2, 2), graveyard);

        return world;
    }

    public CommandHistory getCommandHistory() {
        return commandHistory;
    }

    public void printCommandCount() {
        IO.writeString("Commands issued: " + getCommandHistory().getCommandCount(), WriteStyle.MARGIN);
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
        builder.append(String.format("Progress: %d/%d", getUnlockedAchievementsCount(), getTotalAchievementsCount()));
        for (Achievement a : achievements) {
            if (a.isUnlocked()) {
                builder.append('\n').append(a.getName());
                builder.append('\n').append(Constants.MARGIN).append(a.getInfo());
            }
        }
        IO.writeString(builder.toString(), WriteStyle.MARGIN);
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

    /**
     * Returns the number of seconds the player walk took.
     */
    public int parseHeroWalk(String[] inputWords) {
        if (inputWords.length == 1) {
            Direction walkDirection = Utils.selectFromList(Arrays.asList(Direction.values()));
            if (walkDirection != null) {
                return heroWalk(walkDirection);
            }
        } else {
            String arg = inputWords[1];
            for (Direction dir : Direction.values()) {
                // (the strings are equal) or (the first characters of each string are equal)
                if (dir.toString().equalsIgnoreCase(arg) || StringUtils.firstEqualsIgnoreCase(dir.toString(), arg)) {
                    return heroWalk(dir);
                }
            }
        }
        IO.writeString(Constants.INVALID_INPUT, WriteStyle.MARGIN);
        // The user entered invalid input, this wastes no time.
        return 0;
    }

    /**
     * Attempts to move the hero character in a given direction.
     * <p/>
     * Returns the number of seconds the player walk took.
     */
    public int heroWalk(Direction dir) {
        Point destination = new Point(heroPosition, dir);
        if (getWorld().hasLocation(destination)) {
            getWorld().moveCreature(campaignHero, heroPosition, destination);
            heroPosition = destination;
            campaignHero.setLocation(getWorld().getLocation(destination));
            IO.writeString("You arrive at " + getWorld().getLocation(destination).getName(), WriteStyle.MARGIN);
            return TimeConstants.WALK_SUCCESS;
        } else {
            IO.writeString(Constants.WALK_BLOCKED, WriteStyle.MARGIN);
            return TimeConstants.WALK_BLOCKED;
        }
    }

    /**
     * Prints the next hint.
     */
    public void printNextHint() {
        IO.writeString(Hints.hintsArray[getNextHintIndex()], WriteStyle.MARGIN);
        incrementNextHintIndex();
    }

    /**
     * Returns the hero's name.
     * <p/>
     * Used by the 'whoami' command.
     */
    public String getHeroInfo() {
        return getHero().getName();
    }
}
