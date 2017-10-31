package org.mafagafogigante.dungeon.spells;

import org.mafagafogigante.dungeon.entity.creatures.Creature;
import org.mafagafogigante.dungeon.entity.creatures.HealthState;
import org.mafagafogigante.dungeon.entity.creatures.Hero;
import org.mafagafogigante.dungeon.entity.creatures.HeroUtils;
import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.game.BlockedEntrances;
import org.mafagafogigante.dungeon.game.Direction;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.Location;
import org.mafagafogigante.dungeon.game.Point;
import org.mafagafogigante.dungeon.game.Random;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.stats.CauseOfDeath;
import org.mafagafogigante.dungeon.stats.TypeOfCauseOfDeath;
import org.mafagafogigante.dungeon.util.RichText;
import org.mafagafogigante.dungeon.util.StandardRichTextBuilder;

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
          Game.getGameState().getEngine().rollDateAndRefresh(SECONDS_TO_CAST_HEAL);
          hero.getHealth().incrementBy(HEALING_VALUE);
          writeHealCastOnSelf(hero);
        } else {
          Creature target = hero.findCreature(targetMatcher);
          if (target != null) {
            Game.getGameState().getEngine().rollDateAndRefresh(SECONDS_TO_CAST_HEAL);
            if (hero == target) {
              writeHealCastOnSelf(hero);
            } else {
              writeHealCastOnTarget(target);
            }
            target.getHealth().incrementBy(HEALING_VALUE);
          }
        }
      }

      private void writeHealCastOnSelf(Hero hero) {
        StandardRichTextBuilder builder = new StandardRichTextBuilder();
        builder.append("You casted ");
        builder.append(getName().toString());
        builder.append(" on yourself.");
        if (hero.getHealth().isFull()) {
          builder.append("\n");
          builder.append("You are completely healed.");
        }
        Writer.getDefaultWriter().write(builder.toRichText());
      }

      private void writeHealCastOnTarget(Creature target) {
        StandardRichTextBuilder builder = new StandardRichTextBuilder();
        builder.append("You casted ");
        builder.append(getName().toString());
        builder.append(" on ");
        builder.append(target.getName().getSingular());
        builder.append(".");
        if (target.getHealth().isFull()) {
          builder.append("\n");
          builder.append(target.getName().toString());
          builder.append(" is completely healed.");
        }
        Writer.getDefaultWriter().write(builder.toRichText());
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
            RichText text = new StandardRichTextBuilder().append("You are not equipping anything.").toRichText();
            Writer.getDefaultWriter().write(text);
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
        StandardRichTextBuilder builder = new StandardRichTextBuilder();
        if (!item.hasTag(Item.Tag.REPAIRABLE)) {
          builder.append(item.getName().getSingular()).append(" is not repairable.");
        } else {
          Game.getGameState().getEngine().rollDateAndRefresh(SECONDS_TO_CAST_REPAIR); // Time passes before casting.
          if (!hero.getInventory().hasItem(item)) { // If the item disappeared.
            builder.append(item.getName().getSingular());
            builder.append(" disappeared before you finished casting.");
          } else {
            builder.append("You casted " + getName() + " on " + item.getName().getSingular() + ".");
            builder.append("\n");
            boolean wasCompletelyRepaired = item.getIntegrity().isPerfect();
            item.getIntegrity().incrementBy(REPAIR_VALUE);
            if (wasCompletelyRepaired) {
              builder.append(item.getName().getSingular() + " was already completely repaired.");
            } else {
              if (item.getIntegrity().isPerfect()) { // The item became completely repaired.
                builder.append(item.getName().getSingular() + " is now completely repaired.");
              }
            }
          }
        }
        Writer.getDefaultWriter().write(builder.toRichText());
      }
    });
    putSpell(new Spell("PERCEIVE", "Perceive") {
      private static final int SECONDS_TO_CAST_PERCEIVE = 15;

      @Override
      public void operate(Hero hero, String[] targetMatcher) {
        Game.getGameState().getEngine().rollDateAndRefresh(SECONDS_TO_CAST_PERCEIVE);
        List<Creature> creatureList = new ArrayList<>(hero.getLocation().getCreatures());
        creatureList.remove(hero);
        StandardRichTextBuilder builder = new StandardRichTextBuilder();
        builder.append("You concentrate and allow your spells to show you what your eyes may have missed...\n");
        hero.getObserver().writeCreatureSight(creatureList, builder);
        hero.getObserver().writeItemSight(hero.getLocation().getItemList(), builder);
        Writer.getDefaultWriter().write(builder.toRichText());
      }
    });
    putSpell(new Spell("FINGER_OF_DEATH", "Finger of Death") {
      private static final int SECONDS_TO_CAST_FINGER_OF_DEATH = 10;

      @Override
      public void operate(Hero hero, String[] targetMatcher) {
        if (targetMatcher.length == 0) {
          Writer.getDefaultWriter().write(new StandardRichTextBuilder().append("Provide a target.").toRichText());
        } else {
          Creature target = hero.findCreature(targetMatcher);
          if (target != null) {
            Game.getGameState().getEngine().rollDateAndRefresh(SECONDS_TO_CAST_FINGER_OF_DEATH);
            StandardRichTextBuilder builder = new StandardRichTextBuilder();
            builder.append("You casted ");
            builder.append(getName().getSingular());
            builder.append(" on ");
            builder.append(target.getName().getSingular());
            builder.append(".");
            target.getHealth().decrementBy(target.getHealth().getCurrent());
            if (target.getHealth().isDead()) {
              builder.append("\nAnd it died.");
              target.setCauseOfDeath(new CauseOfDeath(TypeOfCauseOfDeath.SPELL, new Id("FINGER_OF_DEATH")));
            } else {
              builder.append("\nBut it is still alive.");
            }
            Writer.getDefaultWriter().write(builder.toRichText());
          }
        }
      }
    });
    putSpell(new Spell("VEIL_OF_DARKNESS", "Veil Of Darkness") {
      private static final int SECONDS_TO_CAST_VEIL_OF_DARKNESS = 60;

      @Override
      public void operate(Hero hero, String[] targetMatcher) {
        if (targetMatcher.length == 0) {
          Writer.getDefaultWriter().write(new StandardRichTextBuilder().append("Provide a target.").toRichText());
        } else {
          Creature target = hero.findCreature(targetMatcher);
          if (target != null) {
            Game.getGameState().getEngine().rollDateAndRefresh(SECONDS_TO_CAST_VEIL_OF_DARKNESS);
            target.getLightSource().disable();
            StandardRichTextBuilder builder = new StandardRichTextBuilder();
            builder.append("You casted ");
            builder.append(getName().toString());
            builder.append(" on ");
            builder.append(target.getName().getSingular());
            builder.append(".");
            Writer.getDefaultWriter().write(builder.toRichText());
          }
        }
      }
    });
    putSpell(new Spell("UNVEIL", "Unveil") {

      static final int SECONDS_TO_CAST_UNVEIL = 10;

      @Override
      public void operate(Hero hero, String[] targetMatcher) {
        if (targetMatcher.length == 0) {
          Writer.getDefaultWriter().write(new StandardRichTextBuilder().append("Provide a target.").toRichText());
        } else {
          Creature target = hero.findCreature(targetMatcher);
          if (target != null) {
            Game.getGameState().getEngine().rollDateAndRefresh(SECONDS_TO_CAST_UNVEIL);
            target.getLightSource().enable();
            StandardRichTextBuilder builder = new StandardRichTextBuilder();
            builder.append("You casted ");
            builder.append(getName().toString());
            builder.append(" on ");
            builder.append(target.getName().getSingular());
            builder.append(".");
            Writer.getDefaultWriter().write(builder.toRichText());
          }
        }
      }
    });
    putSpell(new Spell("BANISH", "Banish") {

      static final int SECONDS_TO_CAST = 10;

      @Override
      public void operate(Hero hero, String[] targetMatcher) {
        if (targetMatcher.length == 0) {
          Writer.getDefaultWriter().write(new StandardRichTextBuilder().append("Provide a target.").toRichText());
        } else {
          Creature target = hero.findCreature(targetMatcher);
          if (target != null) {
            StandardRichTextBuilder builder = new StandardRichTextBuilder();
            if (target == hero) {
              RichText text = builder.append("You cannot cast banish on yourself.").toRichText();
              Writer.getDefaultWriter().write(text);
              return;
            }
            Game.getGameState().getEngine().rollDateAndRefresh(SECONDS_TO_CAST);
            builder.append("You casted ");
            builder.append(getName().toString());
            builder.append(" on ");
            builder.append(target.getName().getSingular());
            builder.append(".");
            builder.append("\n");
            Writer.getDefaultWriter().write(builder.toRichText());
            if (target.getHealth().getHealthState() == HealthState.DEAD) {
              builder.append("Your target seems to be already dead.");
            } else {
              double missingRatio = 1.0 - target.getHealth().getCurrent() / (double) target.getHealth().getMaximum();
              double successProbability = 0.1 + Math.pow(Math.sqrt(0.9) * missingRatio, 2);
              if (Random.roll(successProbability)) {
                hero.getLocation().removeCreature(target);
                builder.append("You banished " + target.getName().getSingular() + "!");
              } else {
                builder.append("You failed to banish " + target.getName().getSingular() + ".");
                builder.append("\n");
                builder.append("Try damaging " + target.getName().getSingular() + " so it is less resistant.");
              }
            }
            Writer.getDefaultWriter().write(builder.toRichText());
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
          Writer.getDefaultWriter().write(new StandardRichTextBuilder().append("Provide a target.").toRichText());
        } else {
          Creature target = hero.findCreature(targetMatcher);
          if (target != null) {
            Game.getGameState().getEngine().rollDateAndRefresh(SECONDS_TO_CAST_DISPLACE);
            if (Random.roll(DISPLACE_PROBABILITY)) {
              Location targetLocation = target.getLocation();
              BlockedEntrances blockedEntrances = targetLocation.getBlockedEntrances();
              List<Direction> unblockedDirections = new ArrayList<>();
              for (Direction direction : Direction.values()) {
                if (!blockedEntrances.isBlocked(direction)) {
                  unblockedDirections.add(direction);
                }
              }
              StandardRichTextBuilder builder = new StandardRichTextBuilder();
              builder.append("You casted ");
              builder.append(getName().toString());
              builder.append(" on ");
              builder.append(target.getName().getSingular());
              builder.append(" successfully ");
              if (unblockedDirections.isEmpty()) {
                builder.append("but it could not be displaced as all directions are blocked.");
                Writer.getDefaultWriter().write(builder.toRichText());
              } else {
                Direction direction = Random.select(unblockedDirections);
                Point destinationPoint = new Point(targetLocation.getPoint(), direction);
                Location destinationLocation = targetLocation.getWorld().getLocation(destinationPoint);
                targetLocation.removeCreature(target);
                destinationLocation.addCreature(target);
                builder.append("and it was displaced " + direction.toString() + ".");
                Writer.getDefaultWriter().write(builder.toRichText());
              }
            } else {
              String message = "You failed to cast " + getName() + " on " + target.getName().getSingular() + ".";
              Writer.getDefaultWriter().write(new StandardRichTextBuilder().append(message).toRichText());
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
