package org.mafagafogigante.dungeon.entity.creatures;

import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.HOUR;
import static org.mafagafogigante.dungeon.date.DungeonTimeUnit.SECOND;

import org.mafagafogigante.dungeon.achievements.AchievementTracker;
import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.date.Duration;
import org.mafagafogigante.dungeon.entity.Enchantment;
import org.mafagafogigante.dungeon.entity.Entity;
import org.mafagafogigante.dungeon.entity.items.BaseInventory;
import org.mafagafogigante.dungeon.entity.items.BookComponent;
import org.mafagafogigante.dungeon.entity.items.CreatureInventory.SimulationResult;
import org.mafagafogigante.dungeon.entity.items.DrinkableComponent;
import org.mafagafogigante.dungeon.entity.items.FoodComponent;
import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Engine;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.Location;
import org.mafagafogigante.dungeon.game.Name;
import org.mafagafogigante.dungeon.game.NameFactory;
import org.mafagafogigante.dungeon.game.PartOfDay;
import org.mafagafogigante.dungeon.game.QuantificationMode;
import org.mafagafogigante.dungeon.game.Random;
import org.mafagafogigante.dungeon.game.World;
import org.mafagafogigante.dungeon.io.Sleeper;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.spells.Spell;
import org.mafagafogigante.dungeon.spells.SpellData;
import org.mafagafogigante.dungeon.stats.Statistics;
import org.mafagafogigante.dungeon.util.DungeonMath;
import org.mafagafogigante.dungeon.util.Matches;
import org.mafagafogigante.dungeon.util.Messenger;
import org.mafagafogigante.dungeon.util.Utils;
import org.mafagafogigante.dungeon.util.library.Libraries;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Hero class that defines the creature that the player controls.
 */
public class Hero extends Creature {

  private static final long serialVersionUID = Version.MAJOR;
  // The longest possible sleep starts at 19:00 and ends at 05:15 (takes 10 hours and 15 minutes).
  // It seems a good idea to let the Hero have one dream every 4 hours.
  private static final int DREAM_DURATION_IN_SECONDS = 4 * DungeonMath.safeCastLongToInteger(HOUR.as(SECOND));
  private static final int MILLISECONDS_TO_SLEEP_AN_HOUR = 500;
  private static final int MILLISECONDS_TO_FISH = 250;
  private static final int SECONDS_TO_PICK_UP_AN_ITEM = 10;
  private static final int SECONDS_TO_HIT_AN_ITEM = 4;
  private static final int SECONDS_TO_EAT_AN_ITEM = 30;
  private static final int SECONDS_TO_DRINK_AN_ITEM = 10;
  private static final int SECONDS_TO_DROP_AN_ITEM = 2;
  private static final int SECONDS_TO_UNEQUIP = 4;
  private static final int SECONDS_TO_EQUIP = 6;
  private static final int SECONDS_TO_FISH = 60;
  private static final int SECONDS_TO_MILK_A_CREATURE = 45;
  private static final int SECONDS_TO_READ_EQUIPPED_CLOCK = 4;
  private static final int SECONDS_TO_READ_UNEQUIPPED_CLOCK = 10;
  private static final double MAXIMUM_HEALTH_THROUGH_REST = 0.6;
  private static final int SECONDS_TO_REGENERATE_FULL_HEALTH = 30000; // 500 minutes (or 8 hours and 20 minutes).
  private static final int MILK_NUTRITION = 12;
  private static final Id FISH_ID = new Id("FISH");
  private static final Id WELL_FED_ID = new Id("WELL_FED");
  private static final List<String> WELL_FED_LIST = Collections.emptyList();
  private static final Effect WELL_FED_EFFECT = EffectFactory.getDefaultFactory().getEffect(WELL_FED_ID, WELL_FED_LIST);
  private final Walker walker = new Walker();
  private final Observer observer = new Observer(this);
  private final Spellcaster spellcaster = new HeroSpellcaster(this);
  private final AchievementTracker achievementTracker;
  private final Statistics statistics;
  private final Date dateOfBirth;

  Hero(CreaturePreset preset, Statistics statistics, Date dateOfBirth) {
    super(preset);
    this.statistics = statistics;
    this.achievementTracker = new AchievementTracker(statistics);
    this.dateOfBirth = dateOfBirth;
    this.battleLog = new SimpleBattleLog();
  }

  private static int nextRandomTimeChunk() {
    return Random.nextInteger(15 * 60 + 1);
  }

  public Observer getObserver() {
    return observer;
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
    statistics.getHeroStatistics().incrementHealingThroughEating(amount);
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
      int timeResting = Math.max(1, healthRecovered * SECONDS_TO_REGENERATE_FULL_HEALTH / getHealth().getMaximum());
      // Add a randomizing factor to make this more realistic.
      timeResting += nextRandomTimeChunk();
      statistics.getHeroStatistics().incrementRestingTime(timeResting);
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
      // In order to increase realism, add some time for the time it would take to wake up exactly at dawn.
      seconds += nextRandomTimeChunk();
      statistics.getHeroStatistics().incrementSleepingTime(seconds);
      while (seconds > 0) {
        final int cycleDuration = Math.min(DREAM_DURATION_IN_SECONDS, seconds);
        Engine.rollDateAndRefresh(cycleDuration);
        // Cast to long because it is considered best practice. We are going to end with a long anyway, so start doing
        // long arithmetic at the first multiplication. Reported by ICAST_INTEGER_MULTIPLY_CAST_TO_LONG in FindBugs.
        long timeForSleep = (long) MILLISECONDS_TO_SLEEP_AN_HOUR * cycleDuration / HOUR.as(SECOND);
        Sleeper.sleep(timeForSleep);
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

  private <T extends Entity> Matches<T> filterByVisibility(Matches<T> matches) {
    return Matches.fromCollection(filterByVisibility(matches.toList()));
  }

  /**
   * Prints the name of the player's current location and lists all creatures and items the character sees.
   */
  public void look() {
    observer.look();
  }

  /**
   * Selects multiple items from the inventory.
   */
  private List<Item> selectInventoryItems(String[] arguments) {
    if (getInventory().getItemCount() == 0) {
      Writer.write("Your inventory is empty.");
      return Collections.emptyList();
    }
    return selectItems(arguments, getInventory(), false);
  }

  /**
   * Selects a single item from the inventory.
   */
  private Item selectInventoryItem(String[] arguments) {
    List<Item> selectedItems = selectInventoryItems(arguments);
    if (selectedItems.size() == 1) {
      return selectedItems.get(0);
    }
    if (selectedItems.size() > 1) {
      Writer.write("The query matched multiple items.");
    }
    return null;
  }

  /**
   * Select a list of items of the current location based on the arguments of a command.
   */
  private List<Item> selectLocationItems(String[] arguments) {
    if (filterByVisibility(getLocation().getItemList()).isEmpty()) {
      Writer.write("You don't see any items here.");
      return Collections.emptyList();
    } else {
      return selectItems(arguments, getLocation().getInventory(), true);
    }
  }

  /**
   * Selects items of the specified {@code BaseInventory} based on the arguments of a command.
   *
   * @param arguments an array of arguments that will determine the item search
   * @param inventory an object of a subclass of {@code BaseInventory}
   * @param checkForVisibility true if only visible items should be selectable
   * @return a List of items
   */
  private List<Item> selectItems(String[] arguments, BaseInventory inventory, boolean checkForVisibility) {
    List<Item> visibleItems;
    if (checkForVisibility) {
      visibleItems = filterByVisibility(inventory.getItems());
    } else {
      visibleItems = inventory.getItems();
    }
    if (arguments.length != 0 || HeroUtils.checkIfAllEntitiesHaveTheSameName(visibleItems)) {
      return HeroUtils.findItems(visibleItems, arguments);
    } else {
      Writer.write("You must specify an item.");
      return Collections.emptyList();
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
    Matches<Creature> result = Matches.findBestCompleteMatches(getLocation().getCreatures(), tokens);
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
   * Attempts to pick up items from the current location.
   */
  public void pickItems(String[] arguments) {
    if (canSeeAnItem()) {
      List<Item> selectedItems = selectLocationItems(arguments);
      for (Item item : selectedItems) {
        final SimulationResult result = getInventory().simulateItemAddition(item);
        // We stop adding items as soon as we hit the first one which would exceed the amount or weight limit.
        if (result == SimulationResult.AMOUNT_LIMIT) {
          Writer.write("Your inventory is full.");
          break;
        } else if (result == SimulationResult.WEIGHT_LIMIT) {
          Writer.write("You can't carry more weight.");
          // This may not be ideal, as there may be a selection which has lighter items after this item.
          break;
        } else if (result == SimulationResult.SUCCESSFUL) {
          Engine.rollDateAndRefresh(SECONDS_TO_PICK_UP_AN_ITEM);
          if (getLocation().getInventory().hasItem(item)) {
            getLocation().removeItem(item);
            addItem(item);
          } else {
            HeroUtils.writeNoLongerInLocationMessage(item);
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
   * Attempts to drop items from the inventory.
   */
  public void dropItems(String[] arguments) {
    List<Item> selectedItems = selectInventoryItems(arguments);
    for (Item item : selectedItems) {
      if (item == getWeapon()) {
        unsetWeapon(); // Just unset the weapon, it does not need to be moved to the inventory before being dropped.
      }
      // Take the time to drop the item.
      Engine.rollDateAndRefresh(SECONDS_TO_DROP_AN_ITEM);
      if (getInventory().hasItem(item)) { // The item may have disappeared while dropping.
        dropItem(item); // Just drop it if has not disappeared.
      }
      // The character "dropped" the item even if it disappeared while doing it, so write about it.
      Writer.write(String.format("Dropped %s.", item.getQualifiedName()));
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
    DungeonString text = new DungeonString("You are carrying:");
    text.append("\n");
    for (Item item : getInventory().getItems()) {
      text.setColor(item.getRarity().getColor());
      if (hasWeapon() && getWeapon() == item) {
        text.append(" [Equipped]");
      }
      text.append(String.format(" %s (%s)", item.getQualifiedName(), item.getWeight()));
      text.append("\n");
    }
    Writer.write(text);
  }

  /**
   * Attempts to eat an item.
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
          if (healthChange > 0) {
            WELL_FED_EFFECT.affect(this);
          }
        } else {
          HeroUtils.writeNoLongerInInventoryMessage(selectedItem);
        }
      } else {
        Writer.write("You can only eat food.");
      }
    }
  }

  /**
   * Attempts to fish at the current location.
   */
  public void fish() {
    if (getLocation().getTagSet().hasTag(Location.Tag.FISHABLE)) {
      Writer.write("You started trying to fish.");
      Engine.rollDateAndRefresh(SECONDS_TO_FISH);
      Sleeper.sleep(MILLISECONDS_TO_FISH);
      if (Random.roll(getFishingProficiency())) {
        Writer.write("You caught a fish!");
        World world = getLocation().getWorld();
        Item item = world.getItemFactory().makeItem(FISH_ID, world.getWorldDate());
        SimulationResult result = getInventory().simulateItemAddition(item);
        if (result == SimulationResult.AMOUNT_LIMIT) {
          Writer.write("Your inventory is full, you decide to drop the fish in the ground.");
          getLocation().getInventory().addItem(item);
        } else if (result == SimulationResult.WEIGHT_LIMIT) {
          Writer.write("You can't carry more weight, you decide to drop the fish in the ground.");
          getLocation().getInventory().addItem(item);
        } else if (result == SimulationResult.SUCCESSFUL) {
          getInventory().addItem(item);
        }
      } else {
        Writer.write("You did not catch anything.");
      }
    } else {
      Writer.write("You cannot fish in this location.");
    }
  }

  /**
   * Attempts to drink an item.
   */
  public void drinkItem(String[] arguments) {
    Item selectedItem = selectInventoryItem(arguments);
    if (selectedItem != null) {
      if (selectedItem.hasTag(Item.Tag.DRINKABLE)) {
        Engine.rollDateAndRefresh(SECONDS_TO_DRINK_AN_ITEM);
        if (getInventory().hasItem(selectedItem)) {
          DrinkableComponent component = selectedItem.getDrinkableComponent();
          if (!component.isDepleted()) {
            component.affect(this);
            if (component.isDepleted()) {
              Writer.write("You drank the last dose of " + selectedItem.getName() + ".");
            } else {
              Writer.write("You drank a dose of " + selectedItem.getName() + ".");
            }
            selectedItem.decrementIntegrityByDrinking();
          } else {
            Writer.write("This item is depleted.");
          }
        } else {
          HeroUtils.writeNoLongerInInventoryMessage(selectedItem);
        }
      } else {
        Writer.write("This item is not drinkable.");
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

  /**
   * Examines an item.
   */
  public void examineItem(String[] arguments) {
    Item selectedItem = selectInventoryItem(arguments);
    if (selectedItem != null) {
      DungeonString text = new DungeonString();
      text.setColor(selectedItem.getRarity().getColor());
      text.append(selectedItem.getQualifiedName());
      text.append(" ");
      text.append("(");
      text.append(selectedItem.getRarity().getName());
      text.append(")");
      text.append("\n");
      text.append("\n");
      text.resetColor();
      if (selectedItem.getWeaponComponent() != null) {
        List<Enchantment> enchantments = selectedItem.getWeaponComponent().getEnchantments();
        if (!enchantments.isEmpty()) {
          text.append("This item has the following enchantments:");
          text.append("\n");
          for (Enchantment enchantment : enchantments) {
            text.append("  ");
            text.append(enchantment.getName());
            text.append(" ");
            text.append("(");
            text.append(enchantment.getDescription());
            text.append(")");
            text.append("\n");
          }
          text.append("\n");
        }
      }
      text.append("The damage of this item is ");
      text.append(String.valueOf(selectedItem.getWeaponComponent().getDamage()));
      text.append(".");
      text.append("\n");
      text.append("The base hit rate of this item is ");
      text.append(String.valueOf(selectedItem.getWeaponComponent().getHitRate()));
      text.append(".");
      text.append("\n");
      Writer.write(text);
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

  private void destroyItem(@NotNull Item target) {
    String nameBeforeAction = target.getName().getSingular();
    if (target.isBroken()) {
      Writer.write(nameBeforeAction + " is already crashed.");
    } else {
      while (getLocation().getInventory().hasItem(target) && !target.isBroken()) {
        // Simulate item-on-item damage by decrementing an item's integrity by its own hit decrement.
        target.decrementIntegrityByHit();
        if (hasWeapon() && !getWeapon().isBroken()) {
          getWeapon().decrementIntegrityByHit();
        }
        Engine.rollDateAndRefresh(SECONDS_TO_HIT_AN_ITEM);
      }
      String verb = target.hasTag(Item.Tag.REPAIRABLE) ? "crashed" : "destroyed";
      Writer.write(getName() + " " + verb + " " + nameBeforeAction + ".");
    }
  }

  /**
   * Tries to destroy an item from the current location.
   */
  public void destroyItems(String[] arguments) {
    final List<Item> selectedItems = selectLocationItems(arguments);
    for (Item target : selectedItems) {
      if (target != null) {
        destroyItem(target);
      }
    }
  }

  private void equipWeapon(Item weapon) {
    if (hasWeapon()) {
      if (getWeapon() == weapon) {
        Writer.write(getName().getSingular() + " is already equipping " + weapon.getName().getSingular() + ".");
        return;
      } else {
        unequipWeapon();
      }
    }
    Engine.rollDateAndRefresh(SECONDS_TO_EQUIP);
    if (getInventory().hasItem(weapon)) {
      setWeapon(weapon);
      DungeonString string = new DungeonString();
      string.append(getName() + " equipped " + weapon.getQualifiedName() + ".");
      string.append(" " + "Your total damage is now " + getTotalDamage() + ".");
      Writer.write(string);
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
    DungeonString string = new DungeonString();
    string.append("Your name is ");
    string.append(getName().getSingular());
    string.append(".");
    string.append(" ");
    string.append("You are now ");
    string.append(getAgeString());
    string.append(" old");
    string.append(".\n");
    string.append("You are ");
    string.append(getHealth().getHealthState().toString().toLowerCase(Locale.ENGLISH));
    string.append(".\n");
    string.append("Your base attack is ");
    string.append(String.valueOf(getAttack()));
    string.append(".\n");
    if (hasWeapon()) {
      string.append("You are currently equipping ");
      string.append(getWeapon().getQualifiedName());
      string.append(", whose base damage is ");
      string.append(String.valueOf(getWeapon().getWeaponComponent().getDamage()));
      string.append(". This makes your total damage ");
      string.append(String.valueOf(getTotalDamage()));
      string.append(".\n");
    } else {
      string.append("You are fighting bare-handed.\n");
    }
    Writer.write(string);
  }

  private int getTotalDamage() {
    return getAttack() + getWeapon().getWeaponComponent().getDamage();
  }

  /**
   * Prints the Hero's age.
   */
  public void printAge() {
    Writer.write(new DungeonString("You are " + getAgeString() + " old.", Color.CYAN));
  }

  private String getAgeString() {
    return new Duration(dateOfBirth, Game.getGameState().getWorld().getWorldDate()).toString();
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
      Writer.write("You can see that it is " + world.getPartOfDay().toString().toLowerCase(Locale.ENGLISH) + ".");
    } else {
      Writer.write("You can't see the sky.");
    }
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
   * Writes a list of all the Spells that the Hero knows.
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

  /**
   * Writes a list of all the active conditions.
   */
  public void writeConditions() {
    Date worldDate = getLocation().getWorld().getWorldDate();
    List<Condition> conditions = getConditions();
    DungeonString string = new DungeonString();
    if (conditions.isEmpty()) {
      string.append("There are no active conditions.");
    } else {
      for (Condition condition : conditions) {
        string.append(condition.getDescription());
        string.append(" ");
        string.append("(");
        string.append("expires in ");
        Duration duration = new Duration(worldDate, condition.getExpirationDate());
        string.append(duration.toStringWithMostSignificantNonZeroFieldsOnly(2));
        string.append(")");
        string.append("\n");
      }
    }
    Writer.write(string);
  }

}
