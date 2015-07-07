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
import org.dungeon.commands.IssuedCommand;
import org.dungeon.date.Date;
import org.dungeon.date.Period;
import org.dungeon.entity.Entity;
import org.dungeon.entity.Visibility;
import org.dungeon.entity.items.BaseInventory;
import org.dungeon.entity.items.BookComponent;
import org.dungeon.entity.items.CreatureInventory.SimulationResult;
import org.dungeon.entity.items.FoodComponent;
import org.dungeon.entity.items.Item;
import org.dungeon.game.Direction;
import org.dungeon.game.Engine;
import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.ID;
import org.dungeon.game.Location;
import org.dungeon.game.Name;
import org.dungeon.game.PartOfDay;
import org.dungeon.game.Point;
import org.dungeon.game.QuantificationMode;
import org.dungeon.game.Random;
import org.dungeon.game.World;
import org.dungeon.io.IO;
import org.dungeon.io.Sleeper;
import org.dungeon.skill.Skill;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.Constants;
import org.dungeon.util.DungeonMath;
import org.dungeon.util.Matches;
import org.dungeon.util.Messenger;
import org.dungeon.util.Percentage;
import org.dungeon.util.Selectable;
import org.dungeon.util.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
  private static final int SECONDS_TO_CAST_REPAIR_ON_ITEM = 10;
  private static final double MAXIMUM_HEALTH_THROUGH_REST = 0.6;
  private static final String ROTATION_SKILL_SEPARATOR = ">";
  private static final Visibility ADJACENT_LOCATIONS_VISIBILITY = new Visibility(new Percentage(0.6));
  private static final int SECONDS_TO_REGENERATE_FULL_HEALTH = 30000; // 500 minutes (or 8 hours and 20 minutes).
  private static final int MILK_NUTRITION = 12;
  private final AchievementTracker achievementTracker = new AchievementTracker();
  private final Date dateOfBirth;

  Hero(CreaturePreset preset) {
    super(preset);
    dateOfBirth = new Date(432, 6, 4, 8, 30, 0);
  }

  public AchievementTracker getAchievementTracker() {
    return achievementTracker;
  }

  /**
   * Increments the Hero's health by a certain amount, without exceeding its maximum health.
   * If at the end the Hero is completely healed, a messaging about this is written.
   */
  private void addHealth(int amount) {
    int sum = amount + getCurHealth();
    if (sum >= getMaxHealth()) {
      setCurHealth(getMaxHealth());
      IO.writeString("You are completely healed.");
    } else {
      setCurHealth(sum);
    }
  }

  /**
   * Checks if the Hero is completely healed.
   */
  private boolean isCompletelyHealed() {
    return getMaxHealth() == getCurHealth();
  }

  /**
   * Rest until the creature is healed to 60% of its health points.
   * <p/>
   */
  public void rest() {
    if (getCurHealth() >= (int) (MAXIMUM_HEALTH_THROUGH_REST * getMaxHealth())) {
      IO.writeString("You are already rested.");
    } else {
      double fractionHealed = MAXIMUM_HEALTH_THROUGH_REST - (double) getCurHealth() / (double) getMaxHealth();
      Engine.rollDateAndRefresh((int) (SECONDS_TO_REGENERATE_FULL_HEALTH * fractionHealed));
      IO.writeString("Resting...");
      setCurHealth((int) (MAXIMUM_HEALTH_THROUGH_REST * getMaxHealth()));
      IO.writeString("You feel rested.");
    }
  }

  /**
   * Sleep until the sun rises.
   * <p/>
   * Depending on how much the Hero will sleep, this method may print a few dreams.
   */
  public void sleep() {
    int seconds;
    World world = getLocation().getWorld();
    PartOfDay pod = world.getPartOfDay();
    if (pod == PartOfDay.EVENING || pod == PartOfDay.MIDNIGHT || pod == PartOfDay.NIGHT) {
      IO.writeString("You fall asleep.");
      seconds = PartOfDay.getSecondsToNext(world.getWorldDate(), PartOfDay.DAWN);
      // In order to increase realism, add up to 15 minutes to the time it would take to wake up exactly at dawn.
      seconds += Random.nextInteger(15 * 60 + 1);
      while (seconds > 0) {
        final int cycleDuration = Math.min(DREAM_DURATION_IN_SECONDS, seconds);
        Engine.rollDateAndRefresh(cycleDuration);
        Sleeper.sleep(MILLISECONDS_TO_SLEEP_AN_HOUR * cycleDuration / HOUR.as(SECOND));
        if (cycleDuration == DREAM_DURATION_IN_SECONDS) {
          IO.writeString(GameData.getDreamLibrary().getNextDream());
        }
        seconds -= cycleDuration;
        if (!isCompletelyHealed()) {
          int healing = getMaxHealth() * cycleDuration / SECONDS_TO_REGENERATE_FULL_HEALTH;
          setCurHealth(Math.min(getCurHealth() + healing, getMaxHealth()));
        }
      }
      IO.writeString("You wake up.");
    } else {
      IO.writeString("You can only sleep at night.");
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
    Location location = getLocation(); // Avoid multiple calls to the getter.
    IO.writeString(walkedInFrom != null ? "You arrive at " : "You are at ", Constants.FORE_COLOR_NORMAL, false);
    IO.writeString(location.getName().getSingular(), location.getDescription().getColor(), false);
    IO.writeString(".", Constants.FORE_COLOR_NORMAL, false);
    String description = " " + location.getDescription().getInfo();
    description += " " + "It is " + location.getWorld().getPartOfDay().toString().toLowerCase() + ".";
    IO.writeString(description);
    lookAdjacentLocations(walkedInFrom);
    lookCreatures();
    lookItems();
  }

  /**
   * Looks to the Locations adjacent to the one the Hero is in, informing if the Hero cannot see the adjacent Locations.
   *
   * @param walkedInFrom the Direction from which the Hero walked in. {@code null} if the Hero did not walk.
   */
  private void lookAdjacentLocations(Direction walkedInFrom) {
    IO.writeNewLine();
    if (!canSeeAdjacentLocations()) {
      IO.writeString("You can't clearly see the surrounding locations.");
      return;
    }
    World world = Game.getGameState().getWorld();
    Point pos = Game.getGameState().getHeroPosition();
    HashMap<ColoredString, ArrayList<Direction>> visibleLocations = new HashMap<ColoredString, ArrayList<Direction>>();
    Collection<Direction> directions = Direction.getAllExcept(walkedInFrom); // Don't print the Location you just left.
    for (Direction dir : directions) {
      Point adjacentPoint = new Point(pos, dir);
      Location adjacentLocation = world.getLocation(adjacentPoint);
      ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
      explorationStatistics.createEntryIfNotExists(adjacentPoint, adjacentLocation.getID());
      String name = adjacentLocation.getName().getSingular();
      Color color = adjacentLocation.getDescription().getColor();
      ColoredString locationName = new ColoredString(name, color);
      if (!visibleLocations.containsKey(locationName)) {
        visibleLocations.put(locationName, new ArrayList<Direction>());
      }
      visibleLocations.get(locationName).add(dir);
    }
    for (Entry<ColoredString, ArrayList<Direction>> entry : visibleLocations.entrySet()) {
      String text = String.format("To %s you see ", Utils.enumerate(entry.getValue()));
      IO.writeString(text, Constants.FORE_COLOR_NORMAL, false);
      IO.writeString(String.format("%s", entry.getKey().getString()), entry.getKey().getColor(), false);
      IO.writeString(".");
    }
  }

  /**
   * Prints a human-readable description of what Creatures the Hero sees.
   */
  private void lookCreatures() {
    List<Creature> creatures = new ArrayList<Creature>(getLocation().getCreatures());
    creatures.remove(this);
    creatures = filterByVisibility(creatures);
    IO.writeNewLine();
    if (creatures.isEmpty()) {
      IO.writeString("You don't see anyone here.");
    } else {
      IO.writeString("Here you can see " + HeroUtils.enumerateEntities(creatures) + ".");
    }
  }

  /**
   * Prints a human-readable description of what the Hero sees on the ground.
   */
  private void lookItems() {
    List<Item> items = getLocation().getItemList();
    items = filterByVisibility(items);
    if (!items.isEmpty()) {
      IO.writeNewLine();
      IO.writeString("On the ground you see " + HeroUtils.enumerateEntities(items) + ".");
    }
  }

  private Item selectInventoryItem(IssuedCommand issuedCommand) {
    if (getInventory().getItemCount() == 0) {
      IO.writeString("Your inventory is empty.");
      return null;
    } else {
      return selectItem(issuedCommand, getInventory(), false);
    }
  }

  /**
   * Select an item of the current location based on the arguments of a command.
   *
   * @param issuedCommand an IssuedCommand object whose arguments will determine the item search
   * @return an Item or {@code null}
   */
  private Item selectLocationItem(IssuedCommand issuedCommand) {
    if (filterByVisibility(getLocation().getItemList()).isEmpty()) {
      IO.writeString("You don't see any items here.");
      return null;
    } else {
      return selectItem(issuedCommand, getLocation().getInventory(), true);
    }
  }

  /**
   * Selects an item of the specified {@code BaseInventory} based on the arguments of a command.
   *
   * @param issuedCommand      an IssuedCommand object whose arguments will determine the item search
   * @param inventory          an object of a subclass of {@code BaseInventory}
   * @param checkForVisibility true if only visible items should be selectable
   * @return an Item or {@code null}
   */
  private Item selectItem(IssuedCommand issuedCommand, BaseInventory inventory, boolean checkForVisibility) {
    List<Item> visibleItems;
    if (checkForVisibility) {
      visibleItems = filterByVisibility(inventory.getItems());
    } else {
      visibleItems = inventory.getItems();
    }
    if (issuedCommand.hasArguments() || HeroUtils.checkIfAllEntitiesHaveTheSameName(visibleItems)) {
      return HeroUtils.findItem(visibleItems, issuedCommand.getArguments());
    } else {
      IO.writeString("You must specify an item.");
      return null;
    }
  }

  /**
   * Issues this Hero to attack a target.
   *
   * @param issuedCommand the command issued by the player
   */
  public void attackTarget(IssuedCommand issuedCommand) {
    Creature target = selectTarget(issuedCommand);
    if (target != null) {
      Engine.battle(this, target);
    }
  }

  /**
   * Attempts to select a target from the current location using the player input.
   *
   * @param issuedCommand the command entered by the player
   * @return a target Creature or {@code null}
   */
  private Creature selectTarget(IssuedCommand issuedCommand) {
    List<Creature> visibleCreatures = filterByVisibility(getLocation().getCreatures());
    if (issuedCommand.hasArguments() || HeroUtils.checkIfAllEntitiesHaveTheSameName(visibleCreatures, this)) {
      return findCreature(issuedCommand.getArguments());
    } else {
      IO.writeString("You must specify a target.");
      return null;
    }
  }

  /**
   * Attempts to find a creature in the current location comparing its name to an array of string tokens.
   * <p/>
   * If there are no matches, {@code null} is returned.
   * <p/>
   * If there is one match, it is returned.
   * <p/>
   * If there are multiple matches but all have the same name, the first one is returned.
   * <p/>
   * If there are multiple matches with only two different names and one of these names is the Hero's name, the first
   * creature match is returned.
   * <p/>
   * Lastly, if there are multiple matches that do not fall in one of the two categories above, {@code null} is
   * returned.
   *
   * @param tokens an array of string tokens.
   * @return a Creature or null.
   */
  private Creature findCreature(String[] tokens) {
    Matches<Creature> result = Utils.findBestCompleteMatches(getLocation().getCreatures(), tokens);
    result = filterByVisibility(result);
    if (result.size() == 0) {
      IO.writeString("Creature not found.");
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
  public void pickItem(IssuedCommand issuedCommand) {
    if (canSeeAnItem()) {
      Item selectedItem = selectLocationItem(issuedCommand);
      if (selectedItem != null) {
        SimulationResult result = getInventory().simulateItemAddition(selectedItem);
        if (result == SimulationResult.AMOUNT_LIMIT) {
          IO.writeString("Your inventory is full.");
        } else if (result == SimulationResult.WEIGHT_LIMIT) {
          IO.writeString("You can't carry more weight.");
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
      IO.writeString("You do not see any item you could pick up.");
    }
  }

  /**
   * Adds an Item object to the inventory. As a precondition, simulateItemAddition(Item) should return SUCCESSFUL.
   * <p/>
   * Writes a message about this to the screen.
   *
   * @param item the Item to be added, not null
   */
  public void addItem(Item item) {
    if (getInventory().simulateItemAddition(item) == SimulationResult.SUCCESSFUL) {
      getInventory().addItem(item);
      IO.writeString(String.format("Added %s to the inventory.", item.getQualifiedName()));
    } else {
      throw new IllegalStateException("simulateItemAddition did not return SUCCESSFUL.");
    }
  }

  /**
   * Tries to equip an item from the inventory.
   */
  public void parseEquip(IssuedCommand issuedCommand) {
    Item selectedItem = selectInventoryItem(issuedCommand);
    if (selectedItem != null) {
      if (selectedItem.hasTag(Item.Tag.WEAPON)) {
        equipWeapon(selectedItem);
      } else {
        IO.writeString("You cannot equip that.");
      }
    }
  }

  /**
   * Attempts to drop an item from the hero's inventory.
   */
  public void dropItem(IssuedCommand issuedCommand) {
    Item selectedItem = selectInventoryItem(issuedCommand);
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
      IO.writeString(String.format("Dropped %s.", selectedItem.getQualifiedName()));
    }
  }

  public void printInventory() {
    Name item = Name.newInstance("item");
    String firstLine;
    if (getInventory().getItemCount() == 0) {
      firstLine = "Your inventory is empty.";
    } else {
      String itemCount = item.getQuantifiedName(getInventory().getItemCount(), QuantificationMode.NUMBER);
      firstLine = "You are carrying " + itemCount + ". Your inventory weights " + getInventory().getWeight() + ".";
    }
    IO.writeString(firstLine);
    // Local variable to improve readability.
    String itemLimit = item.getQuantifiedName(getInventory().getItemLimit(), QuantificationMode.NUMBER);
    IO.writeString("Your maximum carrying capacity is " + itemLimit + " and " + getInventory().getWeightLimit() + ".");
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
    IO.writeString("You are carrying:");
    for (Item item : getInventory().getItems()) {
      String name = String.format("%s (%s)", item.getQualifiedName(), item.getWeight());
      if (hasWeapon() && getWeapon() == item) {
        IO.writeString(" [Equipped] " + name);
      } else {
        IO.writeString(" " + name);
      }
    }
  }

  /**
   * Attempts to eat an item from the ground.
   */
  public void eatItem(IssuedCommand issuedCommand) {
    Item selectedItem = selectInventoryItem(issuedCommand);
    if (selectedItem != null) {
      if (selectedItem.hasTag(Item.Tag.FOOD)) {
        Engine.rollDateAndRefresh(SECONDS_TO_EAT_AN_ITEM);
        if (getInventory().hasItem(selectedItem)) {
          FoodComponent food = selectedItem.getFoodComponent();
          double remainingBites = selectedItem.getCurIntegrity() / (double) food.getIntegrityDecrementOnEat();
          int healthIncrement;
          if (remainingBites >= 1.0) {
            healthIncrement = food.getNutrition();
          } else {
            // The healing may vary from 0 up to (nutrition - 1) if there is not enough for a bite.
            healthIncrement = (int) (food.getNutrition() * remainingBites);
          }
          selectedItem.decrementIntegrityByEat();
          if (selectedItem.isBroken() && !selectedItem.hasTag(Item.Tag.REPAIRABLE)) {
            IO.writeString("You ate " + selectedItem.getName() + ".");
          } else {
            IO.writeString("You ate a bit of " + selectedItem.getName() + ".");
          }
          addHealth(healthIncrement);
        } else {
          HeroUtils.writeNoLongerInInventoryMessage(selectedItem);
        }
      } else {
        IO.writeString("You can only eat food.");
      }
    }
  }

  /**
   * The method that enables a Hero to drink milk from a Creature.
   *
   * @param issuedCommand the command entered by the player
   */
  public void parseMilk(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) { // Specified which creature to milk from.
      Creature selectedCreature = selectTarget(issuedCommand); // Finds the best match for the specified arguments.
      if (selectedCreature != null) {
        if (selectedCreature.hasTag(Creature.Tag.MILKABLE)) {
          milk(selectedCreature);
        } else {
          IO.writeString("This creature is not milkable.");
        }
      }
    } else { // Filter milkable creatures.
      List<Creature> visibleCreatures = filterByVisibility(getLocation().getCreatures());
      List<Creature> milkableCreatures = HeroUtils.filterByTag(visibleCreatures, Tag.MILKABLE);
      if (milkableCreatures.isEmpty()) {
        IO.writeString("You can't find a milkable creature.");
      } else {
        if (Matches.fromCollection(milkableCreatures).getDifferentNames() == 1) {
          milk(milkableCreatures.get(0));
        } else {
          IO.writeString("You need to be more specific.");
        }
      }
    }
  }

  private void milk(Creature creature) {
    Engine.rollDateAndRefresh(SECONDS_TO_MILK_A_CREATURE);
    IO.writeString("You drink milk directly from " + creature.getName().getSingular() + ".");
    addHealth(MILK_NUTRITION);
  }

  public void readItem(IssuedCommand issuedCommand) {
    Item selectedItem = selectInventoryItem(issuedCommand);
    if (selectedItem != null) {
      BookComponent book = selectedItem.getBookComponent();
      if (book != null) {
        Engine.rollDateAndRefresh(book.getTimeToRead());
        if (getInventory().hasItem(selectedItem)) { // Just in case if a readable item eventually decomposes.
          IO.writeString(book.getText());
          IO.writeNewLine();
          if (book.isDidactic()) {
            learnSkill(book);
          }
        } else {
          HeroUtils.writeNoLongerInInventoryMessage(selectedItem);
        }
      } else {
        IO.writeString("You can only read books.");
      }
    }
  }

  /**
   * Attempts to learn a skill from a BookComponent object. As a precondition, book must be didactic (teach a skill).
   *
   * @param book a BookComponent that returns true to isDidactic, not null
   */
  private void learnSkill(@NotNull BookComponent book) {
    if (!book.isDidactic()) {
      throw new IllegalArgumentException("book should be didactic.");
    }
    Skill skill = new Skill(GameData.getSkillDefinitions().get(book.getSkillID()));
    if (getSkillList().hasSkill(skill.getID())) {
      IO.writeString("You already knew " + skill.getName() + ".");
    } else {
      getSkillList().addSkill(skill);
      IO.writeString("You learned " + skill.getName() + ".");
    }
  }

  /**
   * Tries to destroy an item from the current location.
   */
  public void destroyItem(IssuedCommand issuedCommand) {
    Item target = selectLocationItem(issuedCommand);
    if (target != null) {
      if (target.isBroken()) {
        IO.writeString(target.getName() + " is already crashed.");
      } else {
        Engine.rollDateAndRefresh(SECONDS_TO_DESTROY_AN_ITEM); // Time passes before destroying the item.
        if (getLocation().getInventory().hasItem(target)) {
          target.setCurIntegrity(0);
          String verb = target.hasTag(Item.Tag.REPAIRABLE) ? "crashed" : "destroyed";
          IO.writeString(getName() + " " + verb + " " + target.getName() + ".");
        } else {
          HeroUtils.writeNoLongerInLocationMessage(target);
        }
      }
    }
  }

  private void equipWeapon(Item weapon) {
    if (hasWeapon()) {
      if (getWeapon() == weapon) {
        IO.writeString(getName() + " is already equipping " + weapon.getName() + ".");
        return;
      } else {
        unequipWeapon();
      }
    }
    Engine.rollDateAndRefresh(SECONDS_TO_EQUIP);
    if (getInventory().hasItem(weapon)) {
      setWeapon(weapon);
      IO.writeString(getName() + " equipped " + weapon.getQualifiedName() + ".");
    } else {
      HeroUtils.writeNoLongerInInventoryMessage(weapon);
    }
  }

  public void unequipWeapon() {
    if (hasWeapon()) {
      Engine.rollDateAndRefresh(SECONDS_TO_UNEQUIP);
    }
    if (hasWeapon()) { // The weapon may have disappeared.
      Item equippedWeapon = getWeapon();
      unsetWeapon();
      IO.writeString(getName() + " unequipped " + equippedWeapon.getName() + ".");
    } else {
      IO.writeString("You are not equipping a weapon.");
    }
  }

  /**
   * Prints a message with the current status of the Hero.
   */
  public void printAllStatus() {
    StringBuilder builder = new StringBuilder();
    builder.append(getName()).append("\n");
    builder.append("You are ");
    builder.append(HealthState.getHealthState(getCurHealth(), getMaxHealth()).toString().toLowerCase()).append(".\n");
    builder.append("Your base attack is ").append(String.valueOf(getAttack())).append(".\n");
    if (hasWeapon()) {
      String format = "You are currently equipping %s, whose base damage is %d. This makes your total damage %d.\n";
      int weaponBaseDamage = getWeapon().getWeaponComponent().getDamage();
      int totalDamage = getAttack() + weaponBaseDamage;
      builder.append(String.format(format, getWeapon().getQualifiedName(), weaponBaseDamage, totalDamage));
    } else {
      builder.append("You are fighting bare-handed.\n");
    }
    IO.writeString(builder.toString());
  }

  /**
   * Prints the Hero's age.
   */
  public void printAge() {
    String age = new Period(dateOfBirth, Game.getGameState().getWorld().getWorldDate()).toString();
    IO.writeString(String.format("You are %s old.", age), Color.CYAN);
  }

  /**
   * Makes the Hero read the current date and time as well as he can.
   */
  public void readTime() {
    Item clock = getBestClock();
    if (clock != null) {
      IO.writeString(clock.getClockComponent().getTimeString());
      // Assume that the hero takes the same time to read the clock and to put it back where it was.
      if (getWeapon() == clock) {
        Engine.rollDateAndRefresh(SECONDS_TO_READ_EQUIPPED_CLOCK);
      } else {
        Engine.rollDateAndRefresh(SECONDS_TO_READ_UNEQUIPPED_CLOCK);
      }
    }
    World world = getLocation().getWorld();
    Date worldDate = getLocation().getWorld().getWorldDate();
    IO.writeString("You think it is " + worldDate.toDateString() + ".");
    if (worldDate.getMonth() == dateOfBirth.getMonth() && worldDate.getDay() == dateOfBirth.getDay()) {
      IO.writeString("Today is your birthday.");
    }
    IO.writeString("You can see that it is " + world.getPartOfDay().toString().toLowerCase() + ".");
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
    int timeSpent;
    if (clock == null) {
      timeSpent = 0;
    } else if (clock == getWeapon()) {
      timeSpent = SECONDS_TO_READ_EQUIPPED_CLOCK;
    } else {
      timeSpent = SECONDS_TO_READ_UNEQUIPPED_CLOCK;
    }
    Engine.rollDateAndRefresh(timeSpent);
    return clock;
  }

  /**
   * Prints all the Skills that the Hero knows.
   */
  public void printSkills() {
    if (getSkillList().getSize() == 0) {
      IO.writeString("You have not learned any skills yet.");
    } else {
      IO.writeString("You know the following skills:");
      getSkillList().printSkillList();
    }
  }

  /**
   * Based on the arguments of the last issued command, makes a new SkillRotation for the Hero.
   *
   * @param issuedCommand the last command issued by the player.
   */
  public void editRotation(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      List<String[]> skillNames = new ArrayList<String[]>();
      List<String> currentSkillName = new ArrayList<String>();
      for (String argument : issuedCommand.getArguments()) {
        if (ROTATION_SKILL_SEPARATOR.equals(argument)) {
          if (!currentSkillName.isEmpty()) {
            String[] stringArray = new String[currentSkillName.size()];
            currentSkillName.toArray(stringArray);
            skillNames.add(stringArray);
            currentSkillName.clear();
          }
        } else {
          currentSkillName.add(argument);
        }
      }
      if (!currentSkillName.isEmpty()) {
        String[] stringArray = new String[currentSkillName.size()];
        currentSkillName.toArray(stringArray);
        skillNames.add(stringArray);
        currentSkillName.clear();
      }
      if (skillNames.isEmpty()) {
        IO.writeString("Provide skills arguments separated by '" + ROTATION_SKILL_SEPARATOR + "'.");
      } else {
        getSkillRotation().resetRotation();
        ArrayList<Selectable> skillsList = new ArrayList<Selectable>(getSkillList().toListOfSelectable());
        for (String[] skillName : skillNames) {
          Matches<Selectable> result = Utils.findBestCompleteMatches(skillsList, skillName);
          if (result.size() == 0) {
            IO.writeString(Utils.stringArrayToString(skillName, " ") + " did not match any skill!");
          } else {
            if (result.getDifferentNames() == 1) {
              getSkillRotation().addSkill((Skill) result.getMatch(0));
            } else {
              IO.writeString(Utils.stringArrayToString(skillName, " ") + " matched multiple skills!");
            }
          }
        }
        if (getSkillRotation().isEmpty()) {
          IO.writeString("Failed to create a new skill rotation.");
        } else {
          IO.writeString("Created new skill rotation.");
        }
      }
    } else {
      if (getSkillRotation().isEmpty()) {
        IO.writeString("You don't have a skill rotation.");
      } else {
        IO.writeString("This is your current skill rotation:");
        getSkillRotation().printSkillRotation();
      }
    }
  }

  public void castRepairOnEquippedItem() {
    ID repairID = new ID("REPAIR");
    if (getSkillList().hasSkill(repairID)) {
      if (hasWeapon()) {
        if (getWeapon().hasTag(Item.Tag.REPAIRABLE)) {
          Engine.rollDateAndRefresh(SECONDS_TO_CAST_REPAIR_ON_ITEM); // Ten seconds to cast. Time passes before casting.
          if (hasWeapon()) { // If the item did not disappear.
            getWeapon().incrementIntegrity(GameData.getSkillDefinitions().get(repairID).repair);
            IO.writeString("You casted Repair on " + getWeapon().getName() + ".");
          } else {
            IO.writeString("Your weapon disappeared before you finished casting.");
          }
        } else {
          IO.writeString("The equipped item is not repairable.");
        }
      } else {
        IO.writeString("You are not equipping anything.");
      }
    } else {
      IO.writeString("You don't know how to cast Repair.");
    }
  }

}
