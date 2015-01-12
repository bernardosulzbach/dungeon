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
import org.dungeon.date.Date;
import org.dungeon.date.Period;
import org.dungeon.io.DLogger;
import org.dungeon.io.IO;
import org.dungeon.stats.Statistics;
import org.dungeon.util.CommandHistory;

import java.awt.Color;
import java.io.Serializable;

public class GameState implements Serializable {

  private final CommandHistory commandHistory;
  private final World world;

  private final Statistics statistics = new Statistics();

  private Hero hero;
  private Point heroPosition;

  // Controls if the text displayed is bold or not. False by default. Toggled with the "config bold" command.
  private boolean bold;

  transient private boolean saved;
  private int nextHintIndex;
  private int nextPoemIndex;

  public GameState() {
    commandHistory = new CommandHistory();
    world = new World(statistics.getWorldStatistics());
    createHeroAndStartingLocation();
    saved = true;
  }

  /**
   * Creates the Hero and the starting Location.
   */
  private void createHeroAndStartingLocation() {
    hero = new Hero("Seth");
    heroPosition = new Point(0, 0);
    world.getLocation(heroPosition).addCreature(hero);
    getStatistics().getExplorationStatistics().addVisit(heroPosition, world.getLocation(heroPosition).getID());
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
    if (newIndex == GameData.getHintLibrary().getHintCount()) {
      setNextHintIndex(0);
    } else {
      setNextHintIndex(newIndex);
    }
  }

  /**
   * Prints the next hint.
   */
  public void printNextHint() {
    if (GameData.getHintLibrary().getHintCount() == 0) {
      IO.writeString("No hints were loaded.");
    } else {
      IO.writeString(GameData.getHintLibrary().getHint(getNextHintIndex()));
      incrementNextHintIndex();
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
    if (newIndex == GameData.getPoetryLibrary().getPoemCount()) {
      setNextPoemIndex(0);
    } else {
      setNextPoemIndex(newIndex);
    }
  }

  /**
   * Prints a poem based on the issued command.
   * <p/>
   * If the command has arguments, the game attempts to use the first one as the poem's index (one-based).
   * <p/>
   * Otherwise, the next poem is based on a behind-the-scenes poem index.
   *
   * @param command the issued command.
   */
  public void printPoem(IssuedCommand command) {
    if (GameData.getPoetryLibrary().getPoemCount() == 0) {
      IO.writeString("No poems were loaded.");
    } else {
      if (command.hasArguments()) {
        try {
          // Indexing is zero-based to the implementation, but one-based to the player.
          int index = Integer.parseInt(command.getFirstArgument()) - 1;
          if (index >= 0 && index < GameData.getPoetryLibrary().getPoemCount()) {
            IO.writePoem(GameData.getPoetryLibrary().getPoem(index));
            return;
          }
        } catch (NumberFormatException ignore) {
          // This exception reproduces the same error message an invalid index does.
        }
        IO.writeString("Invalid poem index.");
      } else {
        IO.writePoem(GameData.getPoetryLibrary().getPoem(nextPoemIndex));
        incrementNextPoemIndex();
      }
    }
  }

  /**
   * Prints all unlocked achievements.
   */
  public void printUnlockedAchievements() {
    String dateDifference;
    Achievement achievement;
    Date now = world.getWorldDate();
    AchievementTracker tracker = hero.getAchievementTracker();
    IO.writeString("Progress: " + tracker.getUnlockedCount() + "/" + GameData.ACHIEVEMENTS.size(), Color.CYAN);
    for (UnlockedAchievement ua : tracker.getUnlockedAchievementArray()) {
      achievement = GameData.ACHIEVEMENTS.get(ua.id);
      if (achievement != null) {
        dateDifference = new Period(ua.date, now).toString();
        IO.writeString(achievement.getName() + " (" + dateDifference + " ago)", Color.ORANGE);
        IO.writeString(" " + achievement.getInfo(), Color.YELLOW);
      } else {
        DLogger.warning("Unlocked achievement ID not found in GameData.");
      }
    }
  }

  /**
   * Prints the game statistics.
   */
  public void printGameStatistics() {
    statistics.printAllStatistics();
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
