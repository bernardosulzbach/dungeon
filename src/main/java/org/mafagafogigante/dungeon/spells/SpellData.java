package org.mafagafogigante.dungeon.spells;

import org.mafagafogigante.dungeon.entity.creatures.Creature;
import org.mafagafogigante.dungeon.entity.creatures.Hero;
import org.mafagafogigante.dungeon.entity.creatures.HeroUtils;
import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.game.BlockedEntrances;
import org.mafagafogigante.dungeon.game.Direction;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Engine;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.Location;
import org.mafagafogigante.dungeon.game.Point;
import org.mafagafogigante.dungeon.game.Random;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.stats.CauseOfDeath;
import org.mafagafogigante.dungeon.stats.TypeOfCauseOfDeath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SpellData {

  private static final Map<Id, Spell> spellMap = new HashMap<>();

  static {
    putSpell(new Spell("HEAL", "Heal") {
      private static final int HEALING_VALUE = 10;
      private static final int SECONDS_TO_CAST_HEAL = 25;

      @Override
      public void operate(Hero hero, String[] targetMatcher) {
        if (targetMatcher.length == 0) {
          Engine.rollDateAndRefresh(SECONDS_TO_CAST_HEAL);
          hero.getHealth().incrementBy(HEALING_VALUE);
          writeHealCastOnSelf(hero);
        } else {
          Creature target = hero.findCreature(targetMatcher);
          if (target != null) {
            Engine.rollDateAndRefresh(SECONDS_TO_CAST_HEAL);
            if (hero == target) { // The player used cast ... on <character name>.
              writeHealCastOnSelf(hero);
            } else {
              writeHealCastOnTarget(target);
            }
            target.getHealth().incrementBy(HEALING_VALUE);
          }
        }
      }

      private void writeHealCastOnSelf(Hero hero) {
        Writer.write("You casted " + getName() + " on yourself.");
        if (hero.getHealth().isFull()) {
          Writer.write("You are completely healed.");
        }
      }

      private void writeHealCastOnTarget(Creature target) {
        Writer.write("You casted " + getName() + " on " + target.getName().getSingular() + ".");
        if (target.getHealth().isFull()) {
          Writer.write(target.getName() + " is completely healed.");
        }
      }
    });
    putSpell(new Spell("REPAIR", "Repair") {
      private static final int REPAIR_VALUE = 50;
      private static final int SECONDS_TO_CAST_REPAIR = 10;

      @Override
      public void operate(Hero hero, String[] targetMatcher) {
        List<Item> selectedItems = new ArrayList<>();
        if (targetMatcher.length == 0) {
          if (hero.getWeapon() == null) {
            Writer.write("You are not equipping anything.");
          } else {
            selectedItems.add(hero.getWeapon());
          }
        } else {
          selectedItems.addAll(HeroUtils.findItems(hero.getInventory().getItems(), targetMatcher));
        }
        for (Item item : selectedItems) {
          effectivelyOperate(hero, item);
        }
      }

      private void effectivelyOperate(Hero hero, Item item) {
        if (!item.hasTag(Item.Tag.REPAIRABLE)) {
          Writer.write(item.getName().getSingular() + " is not repairable.");
        } else {
          Engine.rollDateAndRefresh(SECONDS_TO_CAST_REPAIR); // Time passes before casting.
          if (!hero.getInventory().hasItem(item)) { // If the item disappeared.
            Writer.write(item.getName().getSingular() + " disappeared before you finished casting.");
          } else {
            boolean wasCompletelyRepaired = item.getIntegrity().isPerfect();
            item.getIntegrity().incrementBy(REPAIR_VALUE);
            Writer.write("You casted " + getName() + " on " + item.getName().getSingular() + ".");
            if (wasCompletelyRepaired) {
              Writer.write(item.getName().getSingular() + " was already completely repaired.");
            } else {
              if (item.getIntegrity().isPerfect()) { // The item became completely repaired.
                Writer.write(item.getName().getSingular() + " is now completely repaired.");
              }
            }
          }
        }
      }
    });
    putSpell(new Spell("PERCEIVE", "Perceive") {
      private static final int SECONDS_TO_CAST_PERCEIVE = 15;

      @Override
      public void operate(Hero hero, String[] targetMatcher) {
        Engine.rollDateAndRefresh(SECONDS_TO_CAST_PERCEIVE);
        List<Creature> creatureList = new ArrayList<>(hero.getLocation().getCreatures());
        creatureList.remove(hero);
        DungeonString string = new DungeonString();
        string.append("You concentrate and allow your spells to show you what your eyes may have missed...\n");
        hero.getObserver().writeCreatureSight(creatureList, string);
        hero.getObserver().writeItemSight(hero.getLocation().getItemList(), string);
        Writer.write(string);
      }
    });
    putSpell(new Spell("FINGER_OF_DEATH", "Finger of Death") {
      private static final int SECONDS_TO_CAST_FINGER_OF_DEATH = 10;

      @Override
      public void operate(Hero hero, String[] targetMatcher) {
        if (targetMatcher.length == 0) {
          Writer.write("Provide a target.");
        } else {
          Creature target = hero.findCreature(targetMatcher);
          if (target != null) {
            Engine.rollDateAndRefresh(SECONDS_TO_CAST_FINGER_OF_DEATH);
            DungeonString string = new DungeonString();
            string.append("You casted ");
            string.append(getName().getSingular());
            string.append(" on ");
            string.append(target.getName().getSingular());
            string.append(".");
            target.getHealth().decrementBy(target.getHealth().getCurrent());
            if (target.getHealth().isDead()) {
              string.append("\nAnd it died.");
              target.setCauseOfDeath(new CauseOfDeath(TypeOfCauseOfDeath.SPELL, new Id("FINGER_OF_DEATH")));
            } else {
              string.append("\nBut it is still alive.");
            }
            Writer.write(string);
          }
        }
      }
    });
    putSpell(new Spell("VEIL_OF_DARKNESS", "Veil Of Darkness") {
      private static final int SECONDS_TO_CAST_VEIL_OF_DARKNESS = 60;

      @Override
      public void operate(Hero hero, String[] targetMatcher) {
        if (targetMatcher.length == 0) {
          Writer.write("Provide a target.");
        } else {
          Creature target = hero.findCreature(targetMatcher);
          if (target != null) {
            Engine.rollDateAndRefresh(SECONDS_TO_CAST_VEIL_OF_DARKNESS);
            target.getLightSource().disable();
            Writer.write("You casted " + getName() + " on " + target.getName().getSingular() + ".");
          }
        }
      }
    });
    putSpell(new Spell("UNVEIL", "Unveil") {

      static final int SECONDS_TO_CAST_UNVEIL = 10;

      @Override
      public void operate(Hero hero, String[] targetMatcher) {
        if (targetMatcher.length == 0) {
          Writer.write("Provide a target.");
        } else {
          Creature target = hero.findCreature(targetMatcher);
          if (target != null) {
            Engine.rollDateAndRefresh(SECONDS_TO_CAST_UNVEIL);
            target.getLightSource().enable();
            Writer.write("You casted " + getName() + " on " + target.getName().getSingular() + ".");
          }
        }
      }
    });
    putSpell(new Spell("DISPLACE", "Displace") {

      static final int SECONDS_TO_CAST_DISPLACE = 15;
      static final double DISPLACE_PROBABILITY = 0.5;

      @Override
      public void operate(Hero hero, String[] targetMatcher) {
        if (targetMatcher.length == 0) {
          Writer.write("Provide a target.");
        } else {
          Creature target = hero.findCreature(targetMatcher);
          if (target != null) {
            Engine.rollDateAndRefresh(SECONDS_TO_CAST_DISPLACE);
            if (Random.roll(DISPLACE_PROBABILITY)) {
              Location targetLocation = target.getLocation();
              BlockedEntrances blockedEntrances = targetLocation.getBlockedEntrances();
              List<Direction> unblockedDirections = new ArrayList<>();
              for (Direction direction : Direction.values()) {
                if (!blockedEntrances.isBlocked(direction)) {
                  unblockedDirections.add(direction);
                }
              }
              String prefix = "You casted " + getName() + " on " + target.getName().getSingular() + " successfully ";
              if (unblockedDirections.isEmpty()) {
                Writer.write(prefix + "but it could not be displaced as all directions are blocked.");
              } else {
                Direction direction = Random.select(unblockedDirections);
                Point destinationPoint = new Point(targetLocation.getPoint(), direction);
                Location destinationLocation = targetLocation.getWorld().getLocation(destinationPoint);
                targetLocation.removeCreature(target);
                destinationLocation.addCreature(target);
                Writer.write(prefix + "and it was displaced " + direction.toString() + ".");
              }
            } else {
              Writer.write("You failed to cast " + getName() + " on " + target.getName().getSingular() + ".");
            }
          }
        }
      }
    });
  }

  private SpellData() {
  }

  public static Map<Id, Spell> getSpellMap() {
    return spellMap;
  }

  private static void putSpell(Spell spell) {
    spellMap.put(spell.getId(), spell);
  }

}
