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

import java.awt.Color;
import org.dungeon.core.achievements.Achievement;
import org.dungeon.core.creatures.Hero;
import org.dungeon.io.IO;
import org.dungeon.utils.Constants;

/**
 * Engine class that contains most static methods that need to be called to
 * alter a GameState object.
 *
 * @author Bernardo Sulzbach
 */
public class Engine {

    /**
     * Refreshes the game, should be called after every turn.
     */
    public static void refresh() {
        refreshAchievements();
        refreshSpawners();
    }

    /**
     * Iterates over all achievements in GameData, trying to unlock yet to be
     * unlocked achievements.
     */
    private static void refreshAchievements() {
        Hero hero = Game.getGameState().getHero();
        for (Achievement a : GameData.ACHIEVEMENTS) {
            a.update(hero);
        }
    }

    /**
     * Refreshes all relevant Spawners in the world, currently, that is the
     * spawner of the location the Hero is at.
     */
    private static void refreshSpawners() {
        Game.getGameState().getHeroLocation().refreshSpawners();
    }

    /**
     * Parses an array of words to move the hero to another location.
     *
     * @param inputWords the tokens provided by the player.
     * @return how many seconds the player walk took.
     */
    public static int parseHeroWalk(String[] inputWords) {
        if (inputWords.length == 1) {
            IO.writeString("To where?", Color.ORANGE);
        } else {
            String arg = inputWords[1];
            for (Direction dir : Direction.values()) {
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
     *
     * @return the number of seconds the player walk took as an integer.
     */
    static int heroWalk(Direction dir) {
        GameState gameState = Game.getGameState();
        Hero hero = gameState.getHero();
        Point destinationPoint = new Point(gameState.getHeroPosition(), dir);
        if (!gameState.getWorld().hasLocation(destinationPoint)) {
            gameState.getWorld().expand(destinationPoint);
        }
        Location destination = gameState.getWorld().moveHero(dir);
        hero.setLocation(destination);
        String arrivalMessage = "You arrive at " + destination.getName() + ".";
        IO.writeString(arrivalMessage, Color.ORANGE);
        hero.getExplorationLog().addVisit(destinationPoint);
        return TimeConstants.WALK_SUCCESS;
    }

}
