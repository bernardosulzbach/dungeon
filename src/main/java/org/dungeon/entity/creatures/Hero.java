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

package org.dungeon.entity.creatures;

import static org.dungeon.date.DungeonTimeUnit.HOUR;
import static org.dungeon.date.DungeonTimeUnit.SECOND;

import org.dungeon.achievements.AchievementTracker;
import org.dungeon.date.Date;
import org.dungeon.date.Duration;
import org.dungeon.entity.Entity;
import org.dungeon.entity.Visibility;
import org.dungeon.entity.items.BaseInventory;
import org.dungeon.entity.items.BookComponent;
import org.dungeon.entity.items.CreatureInventory.SimulationResult;
import org.dungeon.entity.items.FoodComponent;
import org.dungeon.entity.items.Item;
import org.dungeon.game.Direction;
import org.dungeon.game.DungeonString;
import org.dungeon.game.Engine;
import org.dungeon.game.Game;
import org.dungeon.game.Location;
import org.dungeon.game.Name;
import org.dungeon.game.NameFactory;
import org.dungeon.game.PartOfDay;
import org.dungeon.game.Point;
import org.dungeon.game.QuantificationMode;
import org.dungeon.game.Random;
import org.dungeon.game.World;
import org.dungeon.io.Sleeper;
import org.dungeon.io.Writer;
import org.dungeon.spells.Spell;
import org.dungeon.spells.SpellData;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.DungeonMath;
import org.dungeon.util.Matches;
import org.dungeon.util.Messenger;
import org.dungeon.util.Percentage;
import org.dungeon.util.Utils;
import org.dungeon.util.library.Libraries;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Hero class that defines the creature that the player controls.
 */
public class Hero extends Creature {

  // The longest possible sleep starts at 19:00 and ends at 05:15 (takes 10 hours and 15 minutes).
  // It seems a good idea to let the Hero have one dream every 4 hours.
  private static final int DREAM_DURATION_IN_SECONDS = 4 * DungeonMath.safeCastLongToInteger(HOUR.as(SECOND));
  private static final int MILLISECONDS_TO_SLEEP_AN_HOUR = 500;
  private static final int SECONDS_TO_PICK_UP_AN_ITEM = 10;
  private static final int SECONDS_TO_DESTROY_AN_ITEM = 120;
  private static final int SECONDS_TO_EAT_AN_ITEM = 30;
  private static final int SECONDS_TO_DROP_AN_ITEM = 2;
  private static final int SECONDS_TO_UNEQUIP = 4;
  private static final int SECONDS_TO_EQUIP = 6;
  private static final int SECONDS_TO_MILK_A_CREATURE = 45;
  private static final int SECONDS_TO_READ_EQUIPPED_CLOCK = 4;
  private static final int SECONDS_TO_READ_UNEQUIPPED_CLOCK = 10;
  private static final double MAXIMUM_HEALTH_THROUGH_REST = 0.6;
  private static final Visibility ADJACENT_LOCATIONS_VISIBILITY = new Visibility(new Percentage(0.6));
  private static final int SECONDS_TO_REGENERATE_FULL_HEALTH = 30000; // 500 minutes (or 8 hours and 20 minutes).
  private static final int MILK_NUTRITION = 12;
  private final Walker walker = new Walker();
  private final Spellcaster spellcaster = new HeroSpellcaster(this);
  private final AchievementTracker achievementTracker = new AchievementTracker();
  private final Date dateOfBirth;

  Hero(CreaturePreset preset) {
    super(preset);
    dateOfBirth = new Date(2035, 6, 4, 8, 30, 0);
  }

  /**
   * Appends to a DungeonString the creatures the Hero can see.
   */
  public static void writeCreatureSight(List<Creature> creatures, DungeonString dungeonString) {
    if (creatures.isEmpty()) {
      dungeonString.append("\nYou don't see anyone here.\n");
    } else {
      dungeonString.append("\nHere you can see ");
      dungeonString.append(Utils.enumerateEntities(creatures));
      dungeonString.append(".\n");
    }
  }

  /**
   * Appends to a DungeonString the items the Hero can see.
   */
  public static void writeItemSight(List<Item> items, DungeonString dungeonString) {
    if (!items.isEmpty()) {
      dungeonString.append("\nOn the ground you see ");
      dungeonString.append(Utils.enumerateEntities(items));
      dungeonString.append(".\n");
    }
  }

  public Spellcaster getSpellcaster() {
    return spellcaster;
  }

  public AchievementTracker getAchievementTracker() {
    return achievementTracker;
  }

  /**
   * Increments the Hero's health by a certain amount, without exceeding its maximum health. If at the end the Hero is
   * completely healed, a messaging about this is written.
   */
  private void addHealth(int amount) {
    getHealth().incrementBy(amount);
    if (getHealth().isFull()) {
      Writer.write("You are completely healed.");
    }
  }

  /**
   * Rests until the hero is considered to be rested.
   */
  public void rest() {
    int maximumHealthFromRest = (int) (MAXIMUM_HEALTH_THROUGH_REST * getHealth().getMaximum());
    if (getHealth().getCurrent() >= maximumHealthFromRest) {
      Writer.write("You are already rested.");
    } else {
      int healthRecovered = maximumHealthFromRest - getHealth().getCurrent(); // A positive integer.
      // The fraction SECONDS_TO_REGENERATE_FULL_HEALTH / getHealth().getMaximum() may be smaller than 1.
      // Therefore, the following expression may evaluate to 0 if we do not use Math.max to secure the call to
      // Engine.rollDateAndRefresh.
      int timeResting = Math.max(1, healthRecovered * SECONDS_TO_REGENERATE_FULL_HEALTH / getHealth().getMaximum());
      Engine.rollDateAndRefresh(timeResting);
      Writer.write("Resting...");
      getHealth().incrementBy(healthRecovered);
      Writer.write("You feel rested.");
    }
  }

  /**
   * Sleep until the sun rises.
   *
   * <p>Depending on how much the Hero will sleep, this method may print a few dreams.
   */
  public void sleep() {
    int seconds;
    World world = getLocation().getWorld();
    PartOfDay pod = world.getPartOfDay();
    if (pod == PartOfDay.EVENING || pod == PartOfDay.MIDNIGHT || pod == PartOfDay.NIGHT) {
      Writer.write("You fall asleep.");
      seconds = PartOfDay.getSecondsToNext(world.getWorldDate(), PartOfDay.DAWN);
      // In order to increase realism, add up to 15 minutes to the time it would take to wake up exactly at dawn.
      seconds += Random.nextInteger(15 * 60 + 1);
      while (seconds > 0) {
        final int cycleDuration = Math.min(DREAM_DURATION_IN_SECONDS, seconds);
        Engine.rollDateAndRefresh(cycleDuration);
        Sleeper.sleep(MILLISECONDS_TO_SLEEP_AN_HOUR * cycleDuration / HOUR.as(SECOND));
        if (cycleDuration == DREAM_DURATION_IN_SECONDS) {
          Writer.write(Libraries.getDreamLibrary().next());
        }
        seconds -= cycleDuration;
        if (!getHealth().isFull()) {
          int healing = getHealth().getMaximum() * cycleDuration / SECONDS_TO_REGENERATE_FULL_HEALTH;
          getHealth().incrementBy(healing);
        }
      }
      Writer.write("You wake up.");
    } else {
      Writer.write("You can only sleep at night.");
    }
  }

  /**
   * Checks if the Hero can see a given Entity based on the luminosity of the Location the Hero is in and on the
   * visibility of the specified Entity.
   */
  private boolean canSee(Entity entity) {
    // The Hero is always able to find himself.
    return entity == this || entity.getVisibility().visibleUnder(getLocation().getLuminosity());
  }

  /**
   * Returns whether any Item of the current Location is visible to the Hero.
   */
  private boolean canSeeAnItem() {
    for (Item item : getLocation().getItemList()) {
      if (canSee(item)) {
        return true;
      }
    }
    return false;
  }

  private boolean canSeeAdjacentLocations() {
    return ADJACENT_LOCATIONS_VISIBILITY.visibleUnder(getLocation().getLuminosity());
  }

  private <T extends Entity> List<T> filterByVisibility(List<T> list) {
    List<T> visible = new ArrayList<T>();
    for (T entity : list) {
      if (canSee(entity)) {
        visible.add(entity);
      }
    }
    return visible;
  }

  private <T extends Entity> Matches<T> filterByVisibility(Matches<T> matches) {
    return Matches.fromCollection(filterByVisibility(matches.toList()));
  }

  /**
   * Prints the name of the player's current location and lists all creatures and items the character sees.
   *
   * @param walkedInFrom the Direction from which the Hero walked in. {@code null} if the Hero did not walk.
   */
  public void look(Direction walkedInFrom) {
    DungeonString string = new DungeonString();
    Location location = getLocation(); // Avoid multiple calls to the getter.
    string.append(walkedInFrom != null ? "You arrive at " : "You are at ");
    string.setColor(location.getDescription().getColor());
    string.append(location.getName().getSingular());
    string.resetColor();
    string.append(". ");
    string.append(location.getDescription().getInfo());
    if (canSeeTheSky()) {
      string.append(" It is ");
      string.append(location.getWorld().getPartOfDay().toString().toLowerCase());
      string.append(".");
    }
    string.append("\n");
    lookAdjacentLocations(walkedInFrom, string);
    lookCreatures(string);
    lookItems(string);
    Writer.write(string);
  }

  /**
   * Looks to the Locations adjacent to the one the Hero is in, informing if the Hero cannot see the adjacent
   * Locations.
   *
   * @param walkedInFrom the Direction from which the Hero walked in. {@code null} if the Hero did not walk.
   */
  private void lookAdjacentLocations(Direction walkedInFrom, DungeonString builder) {
    if (canSeeAdjacentLocations()) {
      World world = Game.getGameState().getWorld();
      Point pos = Game.getGameState().getHeroPosition();
      Map<ColoredString, ArrayList<Direction>> visibleLocations = new HashMap<ColoredString, ArrayList<Direction>>();
      // Don't print the Location you just left.
      Collection<Direction> directions = Direction.getAllExcept(walkedInFrom);
      for (Direction dir : directions) {
        Point adjacentPoint = new Point(pos, dir);
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
        builder.append("\n");
        for (Entry<ColoredString, ArrayList<Direction>> entry : visibleLocations.entrySet()) {
          builder.append(String.format("To %s you see ", Utils.enumerate(entry.getValue())));
          builder.setColor(entry.getKey().getColor());
          builder.append(String.format("%s", entry.getKey().getString()));
          builder.resetColor();
          builder.append(".\n");
        }
      }
    } else {
      builder.append("\nYou can't clearly see the surrounding locations.\n");
    }
  }

  /**
   * Prints a human-readable description of what Creatures the Hero sees.
   */
  private void lookCreatures(DungeonString builder) {
    List<Creature> creatures = new ArrayList<Creature>(getLocation().getCreatures());
    creatures.remove(this);
    creatures = filterByVisibility(creatures);
    writeCreatureSight(creatures, builder);
  }

  /**
   * Prints a human-readable description of what the Hero sees on the ground.
   */
  private void lookItems(DungeonString builder) {
    List<Item> items = getLocation().getItemList();
    items = filterByVisibility(items);
    writeItemSight(items, builder);
  }

  private Item selectInventoryItem(String[] arguments) {
    if (getInventory().getItemCount() == 0) {
      Writer.write("Your inventory is empty.");
      return null;
    } else {
      return selectItem(arguments, getInventory(), false);
    }
  }

  /**
   * Select an item of the current location based on the arguments of a command.
   *
   * @param arguments an array of arguments that will determine the item search
   * @return an Item or {@code null}
   */
  private Item selectLocationItem(String[] arguments) {
    if (filterByVisibility(getLocation().getItemList()).isEmpty()) {
      Writer.write("You don't see any items here.");
      return null;
    } else {
      return selectItem(arguments, getLocation().getInventory(), true);
    }
  }

  /**
   * Selects an item of the specified {@code BaseInventory} based on the arguments of a command.
   *
   * @param arguments an array of arguments that will determine the item search
   * @param inventory an object of a subclass of {@code BaseInventory}
   * @param checkForVisibility true if only visible items should be selectable
   * @return an Item or {@code null}
   */
  private Item selectItem(String[] arguments, BaseInventory inventory, boolean checkForVisibility) {
    List<Item> visibleItems;
    if (checkForVisibility) {
      visibleItems = filterByVisibility(inventory.getItems());
    } else {
      visibleItems = inventory.getItems();
    }
    if (arguments.length != 0 || HeroUtils.checkIfAllEntitiesHaveTheSameName(visibleItems)) {
      return HeroUtils.findItem(visibleItems, arguments);
    } else {
      Writer.write("You must specify an item.");
      return null;
    }
  }

  /**
   * Issues this Hero to attack a target.
   */
  public void attackTarget(String[] arguments) {
    Creature target = selectTarget(arguments);
    if (target != null) {
      Engine.battle(this, target);
    }
  }

  /**
   * Attempts to select a target from the current location using the player input.
   *
   * @return a target Creature or {@code null}
   */
  private Creature selectTarget(String[] arguments) {
    List<Creature> visibleCreatures = filterByVisibility(getLocation().getCreatures());
    if (arguments.length != 0 || HeroUtils.checkIfAllEntitiesHaveTheSameName(visibleCreatures, this)) {
      return findCreature(arguments);
    } else {
      Writer.write("You must specify a target.");
      return null;
    }
  }

  /**
   * Attempts to find a creature in the current location comparing its name to an array of string tokens.
   *
   * <p>If there are no matches, {@code null} is returned.
   *
   * <p>If there is one match, it is returned.
   *
   * <p>If there are multiple matches but all have the same name, the first one is returned.
   *
   * <p>If there are multiple matches with only two different names and one of these names is the Hero's name, the first
   * creature match is returned.
   *
   * <p>Lastly, if there are multiple matches that do not fall in one of the two categories above, {@code null} is
   * returned.
   *
   * @param tokens an array of string tokens.
   * @return a Creature or null.
   */
  public Creature findCreature(String[] tokens) {
    Matches<Creature> result = Utils.findBestCompleteMatches(getLocation().getCreatures(), tokens);
    result = filterByVisibility(result);
    if (result.size() == 0) {
      Writer.write("Creature not found.");
    } else if (result.size() == 1 || result.getDifferentNames() == 1) {
      return result.getMatch(0);
    } else if (result.getDifferentNames() == 2 && result.hasMatchWithName(getName())) {
      return result.getMatch(0).getName().equals(getName()) ? result.getMatch(1) : result.getMatch(0);
    } else {
      Messenger.printAmbiguousSelectionMessage();
    }
    return null;
  }

  /**
   * Attempts to pick an Item and add it to the inventory.
   */
  public void pickItem(String[] arguments) {
    if (canSeeAnItem()) {
      Item selectedItem = selectLocationItem(arguments);
      if (selectedItem != null) {
        SimulationResult result = getInventory().simulateItemAddition(selectedItem);
        if (result == SimulationResult.AMOUNT_LIMIT) {
          Writer.write("Your inventory is full.");
        } else if (result == SimulationResult.WEIGHT_LIMIT) {
          Writer.write("You can't carry more weight.");
        } else if (result == SimulationResult.SUCCESSFUL) {
          Engine.rollDateAndRefresh(SECONDS_TO_PICK_UP_AN_ITEM);
          if (getLocation().getInventory().hasItem(selectedItem)) {
            getLocation().removeItem(selectedItem);
            addItem(selectedItem);
          } else {
            HeroUtils.writeNoLongerInLocationMessage(selectedItem);
          }
        }
      }
    } else {
      Writer.write("You do not see any item you could pick up.");
    }
  }

  /**
   * Adds an Item object to the inventory. As a precondition, simulateItemAddition(Item) should return SUCCESSFUL.
   *
   * <p>Writes a message about this to the screen.
   *
   * @param item the Item to be added, not null
   */
  public void addItem(Item item) {
    if (getInventory().simulateItemAddition(item) == SimulationResult.SUCCESSFUL) {
      getInventory().addItem(item);
      Writer.write(String.format("Added %s to the inventory.", item.getQualifiedName()));
    } else {
      throw new IllegalStateException("simulateItemAddition did not return SUCCESSFUL.");
    }
  }

  /**
   * Tries to equip an item from the inventory.
   */
  public void parseEquip(String[] arguments) {
    Item selectedItem = selectInventoryItem(arguments);
    if (selectedItem != null) {
      if (selectedItem.hasTag(Item.Tag.WEAPON)) {
        equipWeapon(selectedItem);
      } else {
        Writer.write("You cannot equip that.");
      }
    }
  }

  /**
   * Attempts to drop an item from the hero's inventory.
   */
  public void dropItem(String[] arguments) {
    Item selectedItem = selectInventoryItem(arguments);
    if (selectedItem != null) {
      if (selectedItem == getWeapon()) {
        unsetWeapon(); // Just unset the weapon, it does not need to be moved to the inventory before being dropped.
      }
      // Take the time to drop the item.
      Engine.rollDateAndRefresh(SECONDS_TO_DROP_AN_ITEM);
      if (getInventory().hasItem(selectedItem)) { // The item may have disappeared while dropping.
        dropItem(selectedItem); // Just drop it if has not disappeared.
      }
      // The character "dropped" the item even if it disappeared while doing it, so write about it.
      Writer.write(String.format("Dropped %s.", selectedItem.getQualifiedName()));
    }
  }

  /**
   * Writes the Hero's inventory to the screen.
   */
  public void writeInventory() {
    Name item = NameFactory.newInstance("item");
    String firstLine;
    if (getInventory().getItemCount() == 0) {
      firstLine = "Your inventory is empty.";
    } else {
      String itemCount = item.getQuantifiedName(getInventory().getItemCount(), QuantificationMode.NUMBER);
      firstLine = "You are carrying " + itemCount + ". Your inventory weights " + getInventory().getWeight() + ".";
    }
    Writer.write(firstLine);
    // Local variable to improve readability.
    String itemLimit = item.getQuantifiedName(getInventory().getItemLimit(), QuantificationMode.NUMBER);
    Writer.write("Your maximum carrying capacity is " + itemLimit + " and " + getInventory().getWeightLimit() + ".");
    if (getInventory().getItemCount() != 0) {
      printItems();
    }
  }

  /**
   * Prints all items in the Hero's inventory. This function should only be called if the inventory is not empty.
   */
  private void printItems() {
    if (getInventory().getItemCount() == 0) {
      throw new IllegalStateException("inventory item count is 0.");
    }
    Writer.write("You are carrying:");
    for (Item item : getInventory().getItems()) {
      String name = String.format("%s (%s)", item.getQualifiedName(), item.getWeight());
      if (hasWeapon() && getWeapon() == item) {
        Writer.write(" [Equipped] " + name);
      } else {
        Writer.write(" " + name);
      }
    }
  }

  /**
   * Attempts to eat an item from the ground.
   */
  public void eatItem(String[] arguments) {
    Item selectedItem = selectInventoryItem(arguments);
    if (selectedItem != null) {
      if (selectedItem.hasTag(Item.Tag.FOOD)) {
        Engine.rollDateAndRefresh(SECONDS_TO_EAT_AN_ITEM);
        if (getInventory().hasItem(selectedItem)) {
          FoodComponent food = selectedItem.getFoodComponent();
          double remainingBites = selectedItem.getIntegrity().getCurrent() / (double) food.getIntegrityDecrementOnEat();
          int healthChange;
          if (remainingBites >= 1.0) {
            healthChange = food.getNutrition();
          } else {
            // The absolute value of the healthChange will never be equal to nutrition, only smaller.
            healthChange = (int) (food.getNutrition() * remainingBites);
          }
          selectedItem.decrementIntegrityByEat();
          if (selectedItem.isBroken() && !selectedItem.hasTag(Item.Tag.REPAIRABLE)) {
            Writer.write("You ate " + selectedItem.getName() + ".");
          } else {
            Writer.write("You ate a bit of " + selectedItem.getName() + ".");
          }
          addHealth(healthChange);
        } else {
          HeroUtils.writeNoLongerInInventoryMessage(selectedItem);
        }
      } else {
        Writer.write("You can only eat food.");
      }
    }
  }

  /**
   * The method that enables a Hero to drink milk from a Creature.
   */
  public void parseMilk(String[] arguments) {
    if (arguments.length != 0) { // Specified which creature to milk from.
      Creature selectedCreature = selectTarget(arguments); // Finds the best match for the specified arguments.
      if (selectedCreature != null) {
        if (selectedCreature.hasTag(Creature.Tag.MILKABLE)) {
          milk(selectedCreature);
        } else {
          Writer.write("This creature is not milkable.");
        }
      }
    } else { // Filter milkable creatures.
      List<Creature> visibleCreatures = filterByVisibility(getLocation().getCreatures());
      List<Creature> milkableCreatures = HeroUtils.filterByTag(visibleCreatures, Tag.MILKABLE);
      if (milkableCreatures.isEmpty()) {
        Writer.write("You can't find a milkable creature.");
      } else {
        if (Matches.fromCollection(milkableCreatures).getDifferentNames() == 1) {
          milk(milkableCreatures.get(0));
        } else {
          Writer.write("You need to be more specific.");
        }
      }
    }
  }

  private void milk(Creature creature) {
    Engine.rollDateAndRefresh(SECONDS_TO_MILK_A_CREATURE);
    Writer.write("You drink milk directly from " + creature.getName().getSingular() + ".");
    addHealth(MILK_NUTRITION);
  }

  /**
   * Attempts to read an Item.
   */
  public void readItem(String[] arguments) {
    Item selectedItem = selectInventoryItem(arguments);
    if (selectedItem != null) {
      BookComponent book = selectedItem.getBookComponent();
      if (book != null) {
        Engine.rollDateAndRefresh(book.getTimeToRead());
        if (getInventory().hasItem(selectedItem)) { // Just in case if a readable item eventually decomposes.
          DungeonString string = new DungeonString(book.getText());
          string.append("\n\n");
          Writer.write(string);
          if (book.isDidactic()) {
            learnSpell(book);
          }
        } else {
          HeroUtils.writeNoLongerInInventoryMessage(selectedItem);
        }
      } else {
        Writer.write("You can only read books.");
      }
    }
  }

  /**
   * Attempts to learn a spell from a BookComponent object. As a precondition, book must be didactic (teach a spell).
   *
   * @param book a BookComponent that returns true to isDidactic, not null
   */
  private void learnSpell(@NotNull BookComponent book) {
    if (!book.isDidactic()) {
      throw new IllegalArgumentException("book should be didactic.");
    }
    Spell spell = SpellData.getSpellMap().get(book.getSpellId());
    if (getSpellcaster().knowsSpell(spell)) {
      Writer.write("You already knew " + spell.getName().getSingular() + ".");
    } else {
      getSpellcaster().learnSpell(spell);
      Writer.write("You learned " + spell.getName().getSingular() + ".");
    }
  }

  /**
   * Tries to destroy an item from the current location.
   */
  public void destroyItem(String[] arguments) {
    Item target = selectLocationItem(arguments);
    if (target != null) {
      if (target.isBroken()) {
        Writer.write(target.getName() + " is already crashed.");
      } else {
        Engine.rollDateAndRefresh(SECONDS_TO_DESTROY_AN_ITEM); // Time passes before destroying the item.
        if (getLocation().getInventory().hasItem(target)) {
          target.decrementIntegrityToZero();
          String verb = target.hasTag(Item.Tag.REPAIRABLE) ? "crashed" : "destroyed";
          Writer.write(getName() + " " + verb + " " + target.getName() + ".");
        } else {
          HeroUtils.writeNoLongerInLocationMessage(target);
        }
      }
    }
  }

  private void equipWeapon(Item weapon) {
    if (hasWeapon()) {
      if (getWeapon() == weapon) {
        Writer.write(getName() + " is already equipping " + weapon.getName() + ".");
        return;
      } else {
        unequipWeapon();
      }
    }
    Engine.rollDateAndRefresh(SECONDS_TO_EQUIP);
    if (getInventory().hasItem(weapon)) {
      setWeapon(weapon);
      Writer.write(getName() + " equipped " + weapon.getQualifiedName() + ".");
    } else {
      HeroUtils.writeNoLongerInInventoryMessage(weapon);
    }
  }

  /**
   * Unequips the currently equipped weapon.
   */
  public void unequipWeapon() {
    if (hasWeapon()) {
      Engine.rollDateAndRefresh(SECONDS_TO_UNEQUIP);
    }
    if (hasWeapon()) { // The weapon may have disappeared.
      Item equippedWeapon = getWeapon();
      unsetWeapon();
      Writer.write(getName() + " unequipped " + equippedWeapon.getName() + ".");
    } else {
      Writer.write("You are not equipping a weapon.");
    }
  }

  /**
   * Prints a message with the current status of the Hero.
   */
  public void printAllStatus() {
    DungeonString builder = new DungeonString();
    builder.append(getName().getSingular());
    builder.append("\n");
    builder.append("You are ");
    builder.append(getHealth().getHealthState().toString().toLowerCase());
    builder.append(".\n");
    builder.append("Your base attack is ");
    builder.append(String.valueOf(getAttack()));
    builder.append(".\n");
    if (hasWeapon()) {
      builder.append("You are currently equipping ");
      builder.append(getWeapon().getQualifiedName());
      builder.append(", whose base damage is ");
      builder.append(String.valueOf(getWeapon().getWeaponComponent().getDamage()));
      builder.append(". This makes your total damage ");
      builder.append(String.valueOf(getAttack() + getWeapon().getWeaponComponent().getDamage()));
      builder.append(".\n");
    } else {
      builder.append("You are fighting bare-handed.\n");
    }
    Writer.write(builder);
  }

  /**
   * Prints the Hero's age.
   */
  public void printAge() {
    String age = new Duration(dateOfBirth, Game.getGameState().getWorld().getWorldDate()).toString();
    Writer.write(new DungeonString("You are " + age + " old.", Color.CYAN));
  }

  /**
   * Makes the Hero read the current date and time as well as he can.
   */
  public void readTime() {
    Item clock = getBestClock();
    if (clock != null) {
      Writer.write(clock.getClockComponent().getTimeString());
      // Assume that the hero takes the same time to read the clock and to put it back where it was.
      Engine.rollDateAndRefresh(getTimeToReadFromClock(clock));
    }
    World world = getLocation().getWorld();
    Date worldDate = getLocation().getWorld().getWorldDate();
    Writer.write("You think it is " + worldDate.toDateString() + ".");
    if (worldDate.getMonth() == dateOfBirth.getMonth() && worldDate.getDay() == dateOfBirth.getDay()) {
      Writer.write("Today is your birthday.");
    }
    if (canSeeTheSky()) {
      Writer.write("You can see that it is " + world.getPartOfDay().toString().toLowerCase() + ".");
    } else {
      Writer.write("You can't see the sky.");
    }
  }

  private boolean canSeeTheSky() {
    return Game.getGameState().getHeroPosition().getZ() >= 0;
  }

  /**
   * Attempts to walk according to the provided arguments.
   *
   * @param arguments an array of string arguments
   */
  public void walk(String[] arguments) {
    walker.parseHeroWalk(arguments);
  }

  /**
   * Gets the easiest-to-access unbroken clock of the Hero. If the Hero has no unbroken clock, the easiest-to-access
   * broken clock. Lastly, if the Hero does not have a clock at all, null is returned.
   *
   * @return an Item object of the clock Item (or null)
   */
  @Nullable
  private Item getBestClock() {
    Item clock = null;
    if (hasWeapon() && getWeapon().hasTag(Item.Tag.CLOCK)) {
      if (!getWeapon().isBroken()) {
        clock = getWeapon();
      } else { // The Hero is equipping a broken clock: check if he has a working one in his inventory.
        for (Item item : getInventory().getItems()) {
          if (item.hasTag(Item.Tag.CLOCK) && !item.isBroken()) {
            clock = item;
            break;
          }
        }
        if (clock == null) {
          clock = getWeapon(); // The Hero does not have a working clock in his inventory: use the equipped one.
        }
      }
    } else { // The Hero is not equipping a clock.
      Item brokenClock = null;
      for (Item item : getInventory().getItems()) {
        if (item.hasTag(Item.Tag.CLOCK)) {
          if (item.isBroken() && brokenClock == null) {
            brokenClock = item;
          } else {
            clock = item;
            break;
          }
        }
      }
      if (brokenClock != null) {
        clock = brokenClock;
      }
    }
    if (clock != null) {
      Engine.rollDateAndRefresh(getTimeToReadFromClock(clock));
    }
    return clock;
  }

  private int getTimeToReadFromClock(@NotNull Item clock) {
    return clock == getWeapon() ? SECONDS_TO_READ_EQUIPPED_CLOCK : SECONDS_TO_READ_UNEQUIPPED_CLOCK;
  }

  /**
   * Writes a list with all the Spells that the Hero knows.
   */
  public void writeSpellList() {
    DungeonString string = new DungeonString();
    if (getSpellcaster().getSpellList().isEmpty()) {
      string.append("You have not learned any spells yet.");
    } else {
      string.append("You know ");
      string.append(Utils.enumerate(getSpellcaster().getSpellList()));
      string.append(".");
    }
    Writer.write(string);
  }

}
