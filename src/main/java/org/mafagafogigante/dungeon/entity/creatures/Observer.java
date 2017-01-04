package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.entity.Luminosity;
import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.game.Direction;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.Location;
import org.mafagafogigante.dungeon.game.Point;
import org.mafagafogigante.dungeon.game.World;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.stats.ExplorationStatistics;
import org.mafagafogigante.dungeon.util.Percentage;
import org.mafagafogigante.dungeon.util.Utils;
import org.mafagafogigante.dungeon.world.LuminosityVisibilityCriterion;
import org.mafagafogigante.dungeon.world.VisibilityCriteria;
import org.mafagafogigante.dungeon.world.WeatherCondition;
import org.mafagafogigante.dungeon.world.WeatherConditionVisibilityCriterion;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

/**
 * An observer is used to observe from a Creature's viewpoint.
 */
public class Observer implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private static final VisibilityCriteria ADJACENT_LOCATIONS_VISIBILITY;

  static {
    LuminosityVisibilityCriterion luminosity = new LuminosityVisibilityCriterion(new Luminosity(new Percentage(0.4)));
    WeatherCondition minimum = WeatherCondition.CLEAR;
    WeatherCondition maximum = WeatherCondition.RAIN;
    WeatherConditionVisibilityCriterion weather = new WeatherConditionVisibilityCriterion(minimum, maximum);
    ADJACENT_LOCATIONS_VISIBILITY = new VisibilityCriteria(luminosity, weather);
  }

  private final Creature creature;

  public Observer(@NotNull Creature creature) {
    this.creature = creature;
  }

  private static List<Point> listAdjacentPoints(Point point) {
    List<Point> adjacentPoints = new ArrayList<>(4);
    adjacentPoints.add(new Point(point, Direction.NORTH));
    adjacentPoints.add(new Point(point, Direction.EAST));
    adjacentPoints.add(new Point(point, Direction.SOUTH));
    adjacentPoints.add(new Point(point, Direction.WEST));
    return adjacentPoints;
  }

  public Location getObserverLocation() {
    return creature.getLocation();
  }

  /**
   * Appends to a DungeonString the creatures that can be seen.
   */
  public void writeCreatureSight(List<Creature> creatures, DungeonString dungeonString) {
    if (creatures.isEmpty()) {
      dungeonString.append("\nYou don't see anyone here.\n");
    } else {
      dungeonString.append("\nHere you can see ");
      dungeonString.append(Utils.enumerateEntities(creatures));
      dungeonString.append(".\n");
    }
  }

  /**
   * Appends to a DungeonString the items that can be seen.
   */
  public void writeItemSight(List<Item> items, DungeonString dungeonString) {
    if (!items.isEmpty()) {
      dungeonString.append("\nHere you can find ");
      dungeonString.append(Utils.enumerateEntities(items));
      dungeonString.append(".\n");
    }
  }

  /**
   * Prints the name of the player's current location and lists all creatures and items the character sees.
   */
  public void look() {
    DungeonString string = new DungeonString();
    Location location = creature.getLocation(); // Avoid multiple calls to the getter.
    string.append("You are at ");
    string.setColor(location.getDescription().getColor());
    string.append(location.getName().getSingular());
    string.resetColor();
    string.append(". ");
    string.append(location.getDescription().getInfo());
    if (creature.canSeeTheSky()) {
      string.append(" It is ");
      World world = location.getWorld();
      string.append(world.getPartOfDay().toString().toLowerCase(Locale.ENGLISH));
      string.append(" and ");
      string.append(world.getWeather().getCurrentCondition(world.getWorldDate()).toDescriptiveString());
      string.append(".");
      // Could use hearing to detect the weather based on the current condition and how underneath the character is.
      String skyDescription = world.describeTheSky(this);
      if (!skyDescription.isEmpty()) {
        string.append(" ");
        string.append("Looking upwards you see ");
        string.append(skyDescription);
        string.append(".");
      }
    }
    string.append("\n");
    lookLocations(string);
    lookCreatures(string);
    lookItems(string);
    Writer.write(string);
  }

  /**
   * Looks to the Locations adjacent to the one the Hero is in, informing if the Hero cannot see the adjacent
   * Locations.
   */
  private void lookLocations(DungeonString dungeonString) {
    dungeonString.append("\n");
    World world = creature.getLocation().getWorld();
    Point point = creature.getLocation().getPoint();
    lookUpwardsAndDownwards(dungeonString, world, point);
    if (areThereAdjacentLocations()) {
      if (canSeeAdjacentLocations()) {
        lookToTheSides(dungeonString, world, point);
      } else {
        dungeonString.append("You can't clearly see the adjacent locations.\n");
      }
    }
  }

  private void lookToVerticalDirection(DungeonString dungeonString, World world, Point up, String adverb) {
    if (world.alreadyHasLocationAt(up)) {
      dungeonString.append(adverb);
      dungeonString.append(" you see ");
      dungeonString.append(world.getLocation(up).getName().getSingular());
      dungeonString.append(".\n");
    }
  }

  private void lookUpwardsAndDownwards(DungeonString dungeonString, World world, Point point) {
    lookToVerticalDirection(dungeonString, world, new Point(point, Direction.UP), "Upwards");
    lookToVerticalDirection(dungeonString, world, new Point(point, Direction.DOWN), "Downwards");
  }

  /**
   * Evaluates if there is already any location that is adjacent to the one the creature is currently in.
   */
  private boolean areThereAdjacentLocations() {
    for (Point point : listAdjacentPoints(creature.getLocation().getPoint())) {
      if (creature.getLocation().getWorld().alreadyHasLocationAt(point)) {
        return true;
      }
    }
    return false;
  }

  private boolean canSeeAdjacentLocations() {
    return ADJACENT_LOCATIONS_VISIBILITY.isMetBy(this);
  }

  private void lookToTheSides(DungeonString dungeonString, World world, Point point) {
    Map<ColoredString, ArrayList<Direction>> visibleLocations = new HashMap<>();
    // Don't print the Location you just left.
    Collection<Direction> directions = getHorizontalDirections();
    for (Direction dir : directions) {
      Point adjacentPoint = new Point(point, dir);
      if (world.hasLocationAt(adjacentPoint)) {
        Location adjacentLocation = world.getLocation(adjacentPoint);
        ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
        explorationStatistics.createEntryIfNotExists(adjacentPoint, adjacentLocation.getId());
        String name = adjacentLocation.getName().getSingular();
        Color color = adjacentLocation.getDescription().getColor();
        ColoredString locationName = new ColoredString(name, color);
        if (!visibleLocations.containsKey(locationName)) {
          visibleLocations.put(locationName, new ArrayList<Direction>());
        }
        visibleLocations.get(locationName).add(dir);
      }
    }
    if (!visibleLocations.isEmpty()) {
      for (Entry<ColoredString, ArrayList<Direction>> entry : visibleLocations.entrySet()) {
        dungeonString.append(String.format("To %s you see ", Utils.enumerate(entry.getValue())));
        dungeonString.setColor(entry.getKey().getColor());
        dungeonString.append(String.format("%s", entry.getKey().getString()));
        dungeonString.resetColor();
        dungeonString.append(".\n");
      }
    }
  }

  private Collection<Direction> getHorizontalDirections() {
    Collection<Direction> directions = new ArrayList<>();
    directions.addAll(Arrays.asList(Direction.values()));
    directions.remove(Direction.UP);
    directions.remove(Direction.DOWN);
    return directions;
  }

  /**
   * Prints a human-readable description of what Creatures the Hero sees.
   */
  private void lookCreatures(DungeonString builder) {
    List<Creature> creatures = new ArrayList<>(creature.getLocation().getCreatures());
    creatures.remove(creature);
    creatures = creature.filterByVisibility(creatures);
    writeCreatureSight(creatures, builder);
  }

  /**
   * Prints a human-readable description of what the Hero sees on the ground.
   */
  private void lookItems(DungeonString builder) {
    List<Item> items = creature.getLocation().getItemList();
    items = creature.filterByVisibility(items);
    writeItemSight(items, builder);
  }

}
