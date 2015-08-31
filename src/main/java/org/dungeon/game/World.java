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

import org.dungeon.date.Date;
import org.dungeon.date.DungeonTimeUnit;
import org.dungeon.entity.creatures.Hero;
import org.dungeon.logging.DungeonLogger;
import org.dungeon.stats.WorldStatistics;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class World implements Serializable {

  private final WorldGenerator generator;

  private final Map<Point, Location> locations;

  private final WorldStatistics worldStatistics;
  private final Date worldCreationDate = new Date(1, 1, 1);
  private Date worldDate = new Date(2055, 6, 2, 6, 10, 0);

  /**
   * Creates a new World.
   *
   * @param statistics a WorldStatistics object on which this World will record its status
   */
  public World(WorldStatistics statistics) {
    worldStatistics = statistics;
    locations = new HashMap<Point, Location>();
    generator = new WorldGenerator(this);
  }

  public Date getWorldCreationDate() {
    return worldCreationDate;
  }

  public Date getWorldDate() {
    return worldDate;
  }

  public void addLocation(Location locationObject, Point coordinates) {
    locations.put(coordinates, locationObject);
    worldStatistics.addLocation(locationObject.getName().getSingular());
  }

  /**
   * Moves the hero from a location to another.
   *
   * @param dir the Direction in which the hero should be moved
   * @return the Location the hero arrives to
   */
  public Location moveHero(Direction dir) {
    Hero hero = Game.getGameState().getHero();
    Point heroOldPosition = Game.getGameState().getHeroPosition();
    Point heroNewPosition = new Point(heroOldPosition, dir);
    Game.getGameState().setHeroPosition(heroNewPosition);
    locations.get(heroOldPosition).removeCreature(hero);
    Location heroNewLocation = locations.get(heroNewPosition);
    heroNewLocation.addCreature(hero);
    return heroNewLocation;
  }

  public boolean hasLocation(Point point) {
    return locations.containsKey(point);
  }

  /**
   * Gets the Location in the specified Point. If the Location in the Point has not yet been created, the world
   * generator will do it.
   *
   * @param point a Point object
   * @return a Location
   */
  @NotNull
  public Location getLocation(@NotNull Point point) {
    if (!hasLocation(point)) {
      generator.expand(point);
    }
    return locations.get(point);
  }

  /**
   * Returns the PartOfDay constant that represents the current part of the day.
   */
  public PartOfDay getPartOfDay() {
    return PartOfDay.getCorrespondingConstant(worldDate);
  }

  /**
   * Rolls the world date a given amount of seconds forward.
   */
  public void rollDate(int seconds) {
    if (seconds <= 0) {
      DungeonLogger.warning("Cannot roll the World's Date back.");
    } else {
      worldDate = worldDate.plus(seconds, DungeonTimeUnit.SECOND);
    }
  }

}
