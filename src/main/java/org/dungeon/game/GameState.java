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
package org.dungeon.game;

import org.dungeon.achievements.Achievement;
import org.dungeon.achievements.AchievementTracker;
import org.dungeon.achievements.UnlockedAchievement;
import org.dungeon.creatures.Hero;
import org.dungeon.io.IO;
import org.dungeon.utils.CommandHistory;
import org.dungeon.utils.Hints;
import org.dungeon.utils.Statistics;
import org.dungeon.utils.Utils;
import org.joda.time.DateTime;

import java.awt.*;
import java.io.Serializable;


public class GameState implements Serializable {

    private final CommandHistory commandHistory;
    private final World world;

    private final Statistics statistics;

    private final Hero hero;
    private Point heroPosition;

    // Controls if the text displayed is bold or not. False by default. Toggled with the "config bold" command.
    private boolean bold;
    // If true, bars will be used instead of fractions wherever possible.
    private boolean usingBars;

    transient private boolean saved;
    private int nextHintIndex;
    private int nextPoemIndex;

    public GameState() {
        commandHistory = new CommandHistory();
        world = new World();

        statistics = new Statistics();

        hero = new Hero("Seth");
        heroPosition = new Point(0, 0);

        // TODO: analyze if this should be moved / refactored or done in a different way.
        world.getLocation(heroPosition).addCreature(hero);
        hero.getExplorationLog().addVisit(heroPosition);

        saved = true;
    }

    public CommandHistory getCommandHistory() {
        return commandHistory;
    }

    public World getWorld() {
        return world;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public Hero getHero() {
        return hero;
    }

    public Point getHeroPosition() {
        return heroPosition;
    }

    public void setHeroPosition(Point heroPosition) {
        this.heroPosition = heroPosition;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isUsingBars() {
        return usingBars;
    }

    public void setUsingBars(boolean usingBars) {
        this.usingBars = usingBars;
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

    private void setNextHintIndex(int nextHintIndex) {
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

    int getNextPoemIndex() {
        return nextPoemIndex;
    }

    private void setNextPoemIndex(int nextPoemIndex) {
        this.nextPoemIndex = nextPoemIndex;
    }

    private void incrementNextPoemIndex() {
        int newIndex = getNextPoemIndex() + 1;
        if (newIndex == GameData.POEMS.size()) {
            setNextPoemIndex(0);
        } else {
            setNextPoemIndex(newIndex);
        }
    }

    /**
     * Prints all unlocked achievements.
     */
    public void printUnlockedAchievements() {
        DateTime now = Game.getGameState().getWorld().getWorldDate();
        String dateDifference;
        UnlockedAchievement ua;
        AchievementTracker tracker = hero.getAchievementTracker();
        IO.writeString("Progress: " + tracker.getUnlockedCount() + "/" + GameData.ACHIEVEMENTS.size(), Color.CYAN);
        for (Achievement a : GameData.ACHIEVEMENTS) {
            ua = tracker.getUnlockedAchievement(a);
            if (ua != null) {
                dateDifference = Utils.dateDifferenceToString(ua.date, now);
                IO.writeString(a.getName() + " (" + dateDifference + " ago)", Color.ORANGE);
                IO.writeString(" " + a.getInfo(), Color.YELLOW);
            }
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
     * Prints the next poem.
     */
    public void printNextPoem() {
        if (GameData.POEMS.isEmpty()) {
            IO.writeString("No poems were loaded.", Color.RED);
        } else {
            IO.writePoem(GameData.POEMS.get(nextPoemIndex));
            incrementNextPoemIndex();
        }
    }

    public void printGameStatistics() {
        statistics.print();
        // TODO: fix the spawn statistics and add them here.
    }

    /**
     * Retrieves the Location object that contains the hero.
     *
     * @return a Location object.
     */
    public Location getHeroLocation() {
        return world.getLocation(heroPosition);
    }

}
