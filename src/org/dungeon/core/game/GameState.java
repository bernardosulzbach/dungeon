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
import org.dungeon.core.creatures.Hero;
import org.dungeon.core.items.ItemPreset;
import org.dungeon.io.IO;
import org.dungeon.utils.CommandHistory;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Hints;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GameState implements Serializable {

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

    public GameState() {
        commandHistory = new CommandHistory();

        achievements = createDemoAchievements();

        // Set the number of achievements the campaign has.
        setTotalAchievementsCount(achievements.size());

        campaignHero = new Hero("Seth");
        heroPosition = new Point(0, 0);

        // campaignWorld = createDemoWorld();
        campaignWorld = new World();

        // TODO: analyze if this should be moved / refactored or done in a different way.
        campaignWorld.expand(heroPosition);
        campaignWorld.getLocation(heroPosition).addCreature(campaignHero);
    }

    private List<Achievement> createDemoAchievements() {
        List<Achievement> list = new ArrayList<Achievement>();

        // Battle achievements that do not require specific kills.
        list.add(new BattleAchievement("First Blood", "Kill a creature.", 10, 1, 0, null, null, null));
        list.add(new BattleAchievement("Killer", "Kill 10 creatures.", 100, 10, 0, null, null, null));
        list.add(new BattleAchievement("Die hard", "Take 10 turns to kill a creature.", 150, 0, 10, null, null, null));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Battle achievements that rely on the kill count of a specific creature ID.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Bane requires six battles against bats.
        CounterMap<String> baneRequirements = new CounterMap<String>("BAT", 6);
        list.add(new BattleAchievement("Bane", "Kill 6 bats.",
                50, 0, 0, baneRequirements, null, null));
        // Cat requires four battles against rats.
        CounterMap<String> catRequirements = new CounterMap<String>("RAT", 4);
        list.add(new BattleAchievement("Cat", "Kill 4 rats.",
                40, 0, 0, catRequirements, null, null));
        // Evil Bastard requires one battle against a rabbit.
        CounterMap<String> evilBastardRequirements = new CounterMap<String>("RABBIT", 1);
        list.add(new BattleAchievement("Evil Bastard", "Kill an innocent rabbit.",
                5, 0, 0, evilBastardRequirements, null, null));
        // Stay Dead requires two battles against a zombie.
        CounterMap<String> stayDeadRequirements = new CounterMap<String>("ZOMBIE", 2);
        list.add(new BattleAchievement("Stay Dead", "Kill 2 zombies.",
                50, 0, 0, stayDeadRequirements, null, null));

        CounterMap<String> dissectionRequirements = new CounterMap<String>("FROG", 5);
        list.add(new BattleAchievement("Dissection", "Kill 5 frogs.",
                25, 0, 0, dissectionRequirements, null, null));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Battle achievements that rely on the kill count of a specific type.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Professional Coward requires killing 10 critters.
        CounterMap<String> professionalCowardRequirements = new CounterMap<String>("CRITTER", 10);
        list.add(new BattleAchievement("Professional Coward", "Kill 10 critters.",
                100, 0, 0, null, professionalCowardRequirements, null));

        CounterMap<String> hunterRequirements = new CounterMap<String>("BEAST", 10);
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

    int getNextHintIndex() {
        return nextHintIndex;
    }

    void setNextHintIndex(int nextHintIndex) {
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

    int getTotalAchievementsCount() {
        return totalAchievementsCount;
    }

    void setTotalAchievementsCount(int totalAchievementsCount) {
        this.totalAchievementsCount = totalAchievementsCount;
    }

    int getUnlockedAchievementsCount() {
        return unlockedAchievementsCount;
    }

    private void incrementUnlockedAchievementsCount() {
        this.unlockedAchievementsCount++;
    }

    /**
     * Prints all unlocked achievements.
     */
    public void printUnlockedAchievements() {
        IO.writeString("Progress: " + getUnlockedAchievementsCount() + "/" +  getTotalAchievementsCount(), Color.CYAN);
        for (Achievement a : achievements) {
            if (a.isUnlocked()) {
                IO.writeString(a.getName(), Color.ORANGE);
                IO.writeString(" " + a.getInfo(), Color.YELLOW);
            }
        }
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
            IO.writeString(Constants.INVALID_INPUT);
        } else {
            String arg = inputWords[1];
            for (Direction dir : Direction.values()) {
                // (the strings are equal) or (the first characters of each string are equal)
                if (dir.equalsIgnoreCase(arg)) {
                    return heroWalk(dir);
                }
            }
            IO.writeString(Constants.INVALID_INPUT);
        }
        // The user did not walk.
        return 0;
    }

    /**
     * Attempts to move the hero character in a given direction.
     * <p/>
     * Returns the number of seconds the player walk took.
     */
    int heroWalk(Direction dir) {
        Point destinationPoint = new Point(heroPosition, dir);
        if (!getWorld().hasLocation(destinationPoint)) {
            getWorld().expand(destinationPoint);
        }
        getWorld().moveCreature(campaignHero, heroPosition, destinationPoint);
        heroPosition = destinationPoint;
        Location destinationLocation = getWorld().getLocation(destinationPoint);
        campaignHero.setLocation(destinationLocation);
        IO.writeString(String.format("You arrive at %s.", destinationLocation.getName()), Color.ORANGE);
        return TimeConstants.WALK_SUCCESS;
    }

    /**
     * Prints the next hint.
     */
    public void printNextHint() {
        IO.writeString(Hints.hintsArray[getNextHintIndex()]);
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
