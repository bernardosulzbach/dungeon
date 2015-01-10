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

package org.dungeon.creatures;

import org.dungeon.achievements.AchievementTracker;
import org.dungeon.counters.BattleStatistics;
import org.dungeon.counters.ExplorationLog;
import org.dungeon.date.Date;
import org.dungeon.date.Period;
import org.dungeon.game.Direction;
import org.dungeon.game.Engine;
import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.IssuedCommand;
import org.dungeon.game.Location;
import org.dungeon.game.Pair;
import org.dungeon.game.PartOfDay;
import org.dungeon.game.Point;
import org.dungeon.game.Selectable;
import org.dungeon.game.TimeConstants;
import org.dungeon.game.World;
import org.dungeon.io.IO;
import org.dungeon.io.Sleeper;
import org.dungeon.items.BookComponent;
import org.dungeon.items.CreatureInventory;
import org.dungeon.items.FoodComponent;
import org.dungeon.items.Item;
import org.dungeon.skill.Skill;
import org.dungeon.util.Constants;
import org.dungeon.util.Percentage;
import org.dungeon.util.SelectionResult;
import org.dungeon.util.Utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Hero class that defines the creature that the player controls.
 */
public class Hero extends Creature {

  private final static int MILLISECONDS_TO_SLEEP_AN_HOUR = 500;
  private static final String ROTATION_SKILL_SEPARATOR = ">";
  private final double minimumLuminosity = 0.3;
  private final Date dateOfBirth;
  private final ExplorationLog explorationLog;
  private final BattleStatistics battleStatistics;
  private final AchievementTracker achievementTracker;

  public Hero(String name) {
    super(makeHeroBlueprint(name));
    setInventory(new CreatureInventory(this, 3));
    dateOfBirth = new Date(432, 6, 4, 8, 30, 0);
    explorationLog = new ExplorationLog();
    battleStatistics = new BattleStatistics();
    achievementTracker = new AchievementTracker();
  }

  private static CreatureBlueprint makeHeroBlueprint(String name) {
    CreatureBlueprint heroBlueprint = new CreatureBlueprint();
    heroBlueprint.setID(Constants.HERO_ID);
    heroBlueprint.setName(name);
    heroBlueprint.setType("Hero");
    heroBlueprint.setAttack(4);
    heroBlueprint.setAttackAlgorithmID("HERO");
    heroBlueprint.setMaxHealth(50);
    heroBlueprint.setCurHealth(50);
    return heroBlueprint;
  }

  public BattleStatistics getBattleStatistics() {
    return battleStatistics;
  }

  public ExplorationLog getExplorationLog() {
    return explorationLog;
  }

  public AchievementTracker getAchievementTracker() {
    return achievementTracker;
  }

  private Date getDateOfBirth() {
    return dateOfBirth;
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
   *
   * @return the number of seconds the hero rested.
   */
  public int rest() {
    final double healthFractionThroughRest = 0.6;
    if (getCurHealth() >= (int) (healthFractionThroughRest * getMaxHealth())) {
      IO.writeString("You are already rested.");
      return 0;
    } else {
      double fractionHealed = healthFractionThroughRest - (double) getCurHealth() / (double) getMaxHealth();
      IO.writeString("Resting...");
      setCurHealth((int) (healthFractionThroughRest * getMaxHealth()));
      IO.writeString("You feel rested.");
      return (int) (TimeConstants.HEAL_TEN_PERCENT * fractionHealed * 10);
    }
  }

  /**
   * Sleep until the sun rises.
   * <p/>
   *
   * @return the number of seconds the hero slept.
   */
  public int sleep() {
    int seconds = 0;
    World world = getLocation().getWorld();
    PartOfDay pod = world.getPartOfDay();
    if (pod == PartOfDay.EVENING || pod == PartOfDay.MIDNIGHT || pod == PartOfDay.NIGHT) {
      IO.writeString("You fall asleep.");
      seconds = PartOfDay.getSecondsToNext(world.getWorldDate(), PartOfDay.DAWN);
      int healing = getMaxHealth() * seconds / TimeConstants.HEAL_TEN_PERCENT / 10;
      if (!isCompletelyHealed()) {
        int health = getCurHealth() + healing;
        if (health < getMaxHealth()) {
          setCurHealth(health);
        } else {
          setCurHealth(getMaxHealth());
        }
      }
      Sleeper.sleep(MILLISECONDS_TO_SLEEP_AN_HOUR * seconds / 3600);
      IO.writeString("You wake up.");
    } else {
      IO.writeString("You can only sleep at night.");
    }
    return seconds;
  }

  /**
   * Prints the name of the player's current location and lists all creatures and items the character sees.
   *
   * @param walkedInFrom the Direction from which the Hero walked in. {@code null} if the Hero did not walk.
   */
  public void look(Direction walkedInFrom) {
    Location location = getLocation(); // Avoid multiple calls to the getter.
    if (walkedInFrom != null) {
      IO.writeString("You arrive at " + location.getName() + ".");
    } else {
      IO.writeString("You are at " + location.getName() + ".");
    }
    IO.writeNewLine();
    if (canSee()) {
      lookAdjacentLocations(walkedInFrom);
      if (location.getCreatureCount() == 1) {
        if (Engine.RANDOM.nextBoolean()) {
          IO.writeString("You do not see anyone here.");
        } else {
          IO.writeString("Only you are in this location.");
        }
      } else {
        String curName;
        int curCount;
        ArrayList<String> alreadyListedCreatures = new ArrayList<String>();
        alreadyListedCreatures.add(getName()); // Avoid listing the Hero.
        for (Creature creature : location.getCreatures()) {
          curName = creature.getName();
          if (!alreadyListedCreatures.contains(curName)) {
            alreadyListedCreatures.add(curName);
            curCount = location.getCreatureCount(creature.getID());
            if (curCount > 1) {
              IO.writeKeyValueString(curName, Integer.toString(curCount));
            } else {
              IO.writeString(curName);
            }
          }
        }
      }
      if (getLocation().getItemCount() != 0) {
        IO.writeNewLine();
        for (Item curItem : getLocation().getItemList()) {
          IO.writeString(curItem.toListEntry());
        }
      }
    } else {
      IO.writeString(Constants.CANT_SEE_ANYTHING);
    }
  }

  /**
   * Looks to the Locations adjacent to the one the Hero is in.
   *
   * @param walkedInFrom the Direction from which the Hero walked in. {@code null} if the Hero did not walk.
   */
  private void lookAdjacentLocations(Direction walkedInFrom) {
    World world = Game.getGameState().getWorld();
    Point pos = Game.getGameState().getHeroPosition();
    // An ArrayList of Pairs of String (Location name) and ArrayLists of Directions.
    // The elements of the first List relate Location names to all Directions where there is a Location with that name.
    // TODO: substitute this by a HashMap of String, ArrayList<Direction>.
    ArrayList<Pair<String, ArrayList<Direction>>> pairs = new ArrayList<Pair<String, ArrayList<Direction>>>();
    for (Direction dir : Direction.values()) {
      // Avoids printing the name of the Location you just left.
      if (walkedInFrom == null || !dir.equals(walkedInFrom)) {
        // True if a pair with this Location name was not found. False otherwise.
        boolean pairWithNameNotFound = true;
        String locationName = world.getLocation(new Point(pos, dir)).getName();
        for (Pair<String, ArrayList<Direction>> pair : pairs) {
          if (pair.a.equals(locationName)) {
            pair.b.add(dir);
            pairWithNameNotFound = false;
            // There will not be another positive match in the ArrayList, therefore it is good to break this loop.
            break;
          }
        }
        if (pairWithNameNotFound) {
          ArrayList<Direction> directionList = new ArrayList<Direction>();
          directionList.add(dir);
          pairs.add(new Pair<String, ArrayList<Direction>>(locationName, directionList));
        }
      }
    }
    // "To North you see Graveyard.\n" four times takes 112 characters, therefore 140 should be enough for anything.
    StringBuilder stringBuilder = new StringBuilder(140);
    for (Pair<String, ArrayList<Direction>> pair : pairs) {
      stringBuilder.append("To ");
      stringBuilder.append(enumerateDirections(pair.b));
      stringBuilder.append(" you see ");
      stringBuilder.append(pair.a);
      stringBuilder.append(".\n");
    }
    IO.writeString(stringBuilder.toString());
    IO.writeNewLine();
  }

  /**
   * Enumerates in an human-readable fashion a List of Directions.
   *
   * @param directions the List of directions.
   * @return a String.
   */
  private String enumerateDirections(List<Direction> directions) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < directions.size(); i++) {
      stringBuilder.append(directions.get(i).toString().toLowerCase());
      if (i < directions.size() - 2) {
        stringBuilder.append(", ");
      } else if (i == directions.size() - 2) {
        stringBuilder.append(" and ");
      }
    }
    return stringBuilder.toString();
  }

  /**
   * @return a boolean indicating if the Hero can see other creatures or items in the current location.
   */
  boolean canSee() {
    return getLocation().getLuminosity() >= minimumLuminosity;
  }

  Item selectInventoryItem(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      return getInventory().findItem(issuedCommand.getArguments());
    } else {
      Utils.printMissingArgumentsMessage();
      return null;
    }
  }

  /**
   * Select an item of the current location based on the arguments of a command.
   *
   * @param issuedCommand the command whose arguments will determine the item search.
   * @return an Item or {@code null}.
   */
  Item selectLocationItem(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      return getLocation().getInventory().findItem(issuedCommand.getArguments());
    } else {
      Utils.printMissingArgumentsMessage();
      return null;
    }
  }

  /**
   * The method that lets the hero attack a target.
   *
   * @param issuedCommand the command entered by the player.
   * @return an integer representing how many seconds the battle lasted.
   */
  public int attackTarget(IssuedCommand issuedCommand) {
    if (canSee()) {
      Creature target = selectTarget(issuedCommand);
      if (target != null) {
        return Engine.battle(this, target) * TimeConstants.BATTLE_TURN_DURATION;
      }
    } else {
      IO.writeString("It is too dark to find your target.");
    }
    return 0;
  }

  /**
   * Attempts to select a target from the current location using the player input.
   *
   * @param issuedCommand the command entered by the player.
   * @return a target Creature or {@code null}.
   */
  Creature selectTarget(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      return findCreature(issuedCommand.getArguments());
    } else {
      if (Engine.RANDOM.nextBoolean()) {
        IO.writeString("Attack what?", Color.ORANGE);
      } else {
        IO.writeString("You must specify a target.", Color.ORANGE);
      }
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
  Creature findCreature(String[] tokens) {
    SelectionResult<Creature> result = Utils.selectFromList(getLocation().getCreatures(), tokens);
    if (result.size() == 0) {
      IO.writeString("Creature not found.");
    } else if (result.size() == 1 || result.getDifferentNames() == 1) {
      return result.getMatch(0);
    } else if (result.getDifferentNames() == 2 && result.hasName(getName())) {
      return result.getMatch(0).getName().equals(getName()) ? result.getMatch(1) : result.getMatch(0);
    } else {
      Utils.printAmbiguousSelectionMessage();
    }
    return null;
  }

  /**
   * Attempts to pick and item and add it to the inventory.
   */
  public void pickItem(IssuedCommand issuedCommand) {
    if (canSee()) {
      Item selectedItem = selectLocationItem(issuedCommand);
      if (selectedItem != null) {
        if (getInventory().isFull()) {
          IO.writeString(Constants.INVENTORY_FULL);
        } else {
          getInventory().addItem(selectedItem);
          getLocation().removeItem(selectedItem);
        }
      }
    } else {
      IO.writeString("It is too dark for you too see anything.");
    }
  }

  /**
   * Tries to equip an item from the inventory.
   */
  public void parseEquip(IssuedCommand issuedCommand) {
    Item selectedItem = selectInventoryItem(issuedCommand);
    if (selectedItem != null) {
      if (selectedItem.isWeapon()) {
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
        unequipWeapon();
      }
      getInventory().removeItem(selectedItem);
      getLocation().addItem(selectedItem);
      IO.writeString("Dropped " + selectedItem.getName() + ".");
    }
  }

  public void printInventory() {
    IO.writeString("Items: " + getInventory().getItemCount() + "/" + getInventory().getLimit(), Color.CYAN);
    getInventory().printItems();
  }

  /**
   * Attempts to eat an item from the ground.
   */
  public void eatItem(IssuedCommand issuedCommand) {
    Item selectedItem = selectInventoryItem(issuedCommand);
    if (selectedItem != null) {
      if (selectedItem.isFood()) {
        FoodComponent food = selectedItem.getFoodComponent();
        double remainingBites = selectedItem.getCurIntegrity() / (double) food.getIntegrityDecrementOnEat();
        if (remainingBites >= 1.0) {
          addHealth(food.getNutrition());
        } else {
          // The healing may vary from 0 up to (nutrition - 1) if there is not enough for a bite.
          addHealth((int) (food.getNutrition() * remainingBites));
        }
        selectedItem.decrementIntegrity(food.getIntegrityDecrementOnEat());
        if (selectedItem.isBroken() && !selectedItem.isRepairable()) {
          IO.writeString("You ate " + selectedItem.getName() + ".");
          getInventory().removeItem(selectedItem);
        } else {
          IO.writeString("You ate a bit of " + selectedItem.getName() + ".");
        }
        if (isCompletelyHealed()) {
          IO.writeString("You are completely healed.");
        }
      } else {
        IO.writeString("You can only eat food.");
      }
    }
  }

  public int readItem(IssuedCommand issuedCommand) {
    Item selectedItem = selectInventoryItem(issuedCommand);
    if (selectedItem != null) {
      BookComponent book = selectedItem.getBookComponent();
      if (book != null) {
        Skill skill = new Skill(GameData.SKILL_DEFINITIONS.get(book.getSkillID()));
        if (getSkillList().hasSkill(skill.getID())) {
          IO.writeString("You already know " + skill.getName() + ".");
          // It takes some time to look to the cover of the book.
          return 6;
        } else {
          getSkillList().addSkill(skill);
          IO.writeString("You learned " + skill.getName() + ".");
          return 60;
        }
      } else {
        IO.writeString("You can only read books.");
      }
    }
    return 0;
  }

  /**
   * Tries to destroy an item from the current location.
   */
  public void destroyItem(IssuedCommand issuedCommand) {
    Item target;
    if (issuedCommand.hasArguments()) {
      target = getLocation().getInventory().findItem(issuedCommand.getArguments());
    } else {
      Utils.printMissingArgumentsMessage();
      target = null;
    }
    if (target != null) {
      if (target.isRepairable()) {
        if (!target.isBroken()) {

          target.setCurIntegrity(0);
          IO.writeString(getName() + " crashed " + target.getName() + ".");
        }
      } else {
        getLocation().removeItem(target);
        IO.writeString(getName() + " destroyed " + target.getName() + ".");
      }
    }
  }

  boolean hasClock() {
    for (Item item : getInventory().getItems()) {
      if (item.isClock()) {
        return true;
      }
    }
    return false;
  }

  Item getClock() {
    for (Item item : getInventory().getItems()) {
      if (item.isClock()) {
        return item;
      }
    }
    return null;
  }

  void equipWeapon(Item weapon) {
    if (hasWeapon()) {
      if (getWeapon() == weapon) {
        IO.writeString(getName() + " is already equipping " + weapon.getName() + ".");
        return;
      } else {
        unequipWeapon();
      }
    }
    this.setWeapon(weapon);
    IO.writeString(getName() + " equipped " + weapon.getName() + ".");
  }

  public void unequipWeapon() {
    if (hasWeapon()) {
      IO.writeString(getName() + " unequipped " + getWeapon().getName() + ".");
      setWeapon(null);
    } else {
      IO.writeString(Constants.NOT_EQUIPPING_A_WEAPON);
    }
  }

  public void printHeroStatus() {
    IO.writeString(getName());
    IO.writeNamedBar("Health", new Percentage(getCurHealth() / (double) getMaxHealth()), Constants.HEALTH_BAR_COLOR);
    IO.writeKeyValueString("Attack", Integer.toString(getAttack()));
  }

  public void printWeaponStatus() {
    if (hasWeapon()) {
      Item heroWeapon = getWeapon();
      IO.writeString(heroWeapon.getQualifiedName());
      IO.writeKeyValueString("Damage", Integer.toString(heroWeapon.getDamage()));
    } else {
      IO.writeString(Constants.NOT_EQUIPPING_A_WEAPON);
    }
  }

  /**
   * Output a table with both the hero's status and his weapon's status.
   */
  public void printAllStatus() {
    printHeroStatus();
    if (getWeapon() != null) {
      printWeaponStatus();
    }
  }

  /**
   * Prints the hero's age.
   */
  public void printAge() {
    String age = new Period(getDateOfBirth(), Game.getGameState().getWorld().getWorldDate()).toString();
    IO.writeString(String.format("You are %s old.", age), Color.CYAN);
  }

  /**
   * Makes the hero read the current date and time as well as he can.
   *
   * @return how many seconds the action lasted.
   */
  public int printDateAndTime() {
    World world = getLocation().getWorld();
    int timeSpent = 2;
    if (hasClock()) {
      if (hasWeapon() && getWeapon().isClock() && !getWeapon().isBroken()) {
        // Reading the time from an equipped clock is the fastest possible action.
        timeSpent += 2;
      } else {
        // The hero needed to pick up a watch or something from his inventory, consuming more time.
        timeSpent += 8;
      }
      // Prints whatever the clock shows.
      IO.writeString(getClock().getClockComponent().getTimeString());
    }

    Date worldDate = world.getWorldDate();
    IO.writeString("You think it is " + worldDate.toDateString() + ".");

    Date dob = getDateOfBirth();
    if (worldDate.getMonth() == dob.getMonth() && worldDate.getDay() == dob.getDay()) {
      IO.writeString("Today is your birthday.");
    }

    IO.writeString("You can see that it is " + world.getPartOfDay().toString().toLowerCase() + ".");
    return timeSpent;
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
          SelectionResult<Selectable> result = Utils.selectFromList(skillsList, skillName);
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

}
