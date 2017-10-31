package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.entity.Luminosity;
import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.game.Direction;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.Location;
import org.mafagafogigante.dungeon.game.Point;
import org.mafagafogigante.dungeon.game.World;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.stats.ExplorationStatistics;
import org.mafagafogigante.dungeon.util.Percentage;
import org.mafagafogigante.dungeon.util.RichString;
import org.mafagafogigante.dungeon.util.StandardRichString;
import org.mafagafogigante.dungeon.util.StandardRichTextBuilder;
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
   * Appends to a StandardRichTextBuilder the creatures that can be seen.
   */
  public void writeCreatureSight(List<Creature> creatures, StandardRichTextBuilder standardRichText) {
    if (creatures.isEmpty()) {
      standardRichText.append("\nYou don't see anyone here.\n");
    } else {
      standardRichText.append("\nHere you can see ");
      standardRichText.append(Utils.enumerateEntities(creatures));
      standardRichText.append(".\n");
    }
  }

  /**
   * Appends to a StandardRichTextBuilder the items that can be seen.
   */
  public void writeItemSight(List<Item> items, StandardRichTextBuilder standardRichText) {
    if (!items.isEmpty()) {
      standardRichText.append("\nHere you can find ");
      standardRichText.append(Utils.enumerateEntities(items));
      standardRichText.append(".\n");
    }
  }

  /**
   * Prints the name of the player's current location and lists all creatures and items the character sees.
   */
  public void look() {
    StandardRichTextBuilder builder = new StandardRichTextBuilder();
    Location location = creature.getLocation(); // Avoid multiple calls to the getter.
    builder.append("You are at ");
    builder.setColor(location.getDescription().getColor());
    builder.append(location.getName().getSingular());
    builder.resetColor();
    builder.append(". ");
    builder.append(location.getDescription().getInfo());
    if (creature.canSeeTheSky()) {
      builder.append(" It is ");
      World world = location.getWorld();
      builder.append(world.getPartOfDay().toString().toLowerCase(Locale.ENGLISH));
      builder.append(" and ");
      builder.append(world.getWeather().getCurrentCondition(world.getWorldDate()).toDescriptiveString());
      builder.append(".");
      // Could use hearing to detect the weather based on the current condition and how underneath the character is.
      String skyDescription = world.describeTheSky(this);
      if (!skyDescription.isEmpty()) {
        builder.append(" ");
        builder.append("Looking upwards you see ");
        builder.append(skyDescription);
        builder.append(".");
      }
    }
    builder.append("\n");
    lookLocations(builder);
    lookCreatures(builder);
    lookItems(builder);
    Writer.getDefaultWriter().write(builder.toRichText());
  }

  /**
   * Looks to the Locations adjacent to the one the Hero is in, informing if the Hero cannot see the adjacent
   * Locations.
   */
  private void lookLocations(StandardRichTextBuilder standardRichText) {
    standardRichText.append("\n");
    World world = creature.getLocation().getWorld();
    Point point = creature.getLocation().getPoint();
    lookUpwardsAndDownwards(standardRichText, world, point);
    if (areThereAdjacentLocations()) {
      if (canSeeAdjacentLocations()) {
        lookToTheSides(standardRichText, world, point);
      } else {
        standardRichText.append("You can't clearly see the adjacent locations.\n");
      }
    }
  }

  private void lookToVerticalDirection(StandardRichTextBuilder standardRichText, World world, Point up, String adverb) {
    if (world.alreadyHasLocationAt(up)) {
      standardRichText.append(adverb);
      standardRichText.append(" you see ");
      standardRichText.append(world.getLocation(up).getName().getSingular());
      standardRichText.append(".\n");
    }
  }

  private void lookUpwardsAndDownwards(StandardRichTextBuilder standardRichText, World world, Point point) {
    lookToVerticalDirection(standardRichText, world, new Point(point, Direction.UP), "Upwards");
    lookToVerticalDirection(standardRichText, world, new Point(point, Direction.DOWN), "Downwards");
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

  private void lookToTheSides(StandardRichTextBuilder standardRichText, World world, Point point) {
    Map<RichString, ArrayList<Direction>> visibleLocations = new HashMap<>();
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
        RichString locationName = new StandardRichString(name, color);
        if (!visibleLocations.containsKey(locationName)) {
          visibleLocations.put(locationName, new ArrayList<Direction>());
        }
        visibleLocations.get(locationName).add(dir);
      }
    }
    if (!visibleLocations.isEmpty()) {
      for (Entry<RichString, ArrayList<Direction>> entry : visibleLocations.entrySet()) {
        standardRichText.append(String.format("To %s you see ", Utils.enumerate(entry.getValue())));
        standardRichText.setColor(entry.getKey().getColor());
        standardRichText.append(String.format("%s", entry.getKey().getString()));
        standardRichText.resetColor();
        standardRichText.append(".\n");
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
  private void lookCreatures(StandardRichTextBuilder builder) {
    List<Creature> creatures = new ArrayList<>(creature.getLocation().getCreatures());
    creatures.remove(creature);
    creatures = creature.filterByVisibility(creatures);
    writeCreatureSight(creatures, builder);
  }

  /**
   * Prints a human-readable description of what the Hero sees on the ground.
   */
  private void lookItems(StandardRichTextBuilder builder) {
    List<Item> items = creature.getLocation().getItemList();
    items = creature.filterByVisibility(items);
    writeItemSight(items, builder);
  }

}
