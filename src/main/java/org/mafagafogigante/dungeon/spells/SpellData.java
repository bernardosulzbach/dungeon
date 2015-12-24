/*
 * Copyright (C) 2015 Bernardo Sulzbach
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

package org.mafagafogigante.dungeon.spells;

import org.mafagafogigante.dungeon.entity.creatures.Creature;
import org.mafagafogigante.dungeon.entity.creatures.Hero;
import org.mafagafogigante.dungeon.entity.creatures.HeroUtils;
import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Engine;
import org.mafagafogigante.dungeon.game.Id;
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
        Item selectedItem;
        if (targetMatcher.length == 0) {
          selectedItem = hero.getWeapon();
          if (selectedItem == null) {
            Writer.write("You are not equipping anything.");
          }
        } else {
          selectedItem = HeroUtils.findItem(hero.getInventory().getItems(), targetMatcher);
        }
        if (selectedItem != null) {
          effectivelyOperate(hero, selectedItem);
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

      public static final int SECONDS_TO_CAST_UNVEIL = 10;

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
