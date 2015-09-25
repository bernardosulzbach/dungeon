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

package org.dungeon.entity.creatures;

import org.dungeon.io.Writer;
import org.dungeon.logging.DungeonLogger;
import org.dungeon.spells.Spell;
import org.dungeon.util.Matches;
import org.dungeon.util.ParsingUtils;
import org.dungeon.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Spellcaster implementation the Hero uses.
 */
public class HeroSpellcaster implements Serializable, Spellcaster {

  private final Hero hero;
  private final List<Spell> spellList = new ArrayList<Spell>();

  public HeroSpellcaster(Hero hero) {
    this.hero = hero;
  }

  @Override
  public List<Spell> getSpellList() {
    return spellList;
  }

  @Override
  public boolean knowsSpell(Spell spell) {
    return spellList.contains(spell);
  }

  @Override
  public void learnSpell(Spell spell) {
    if (knowsSpell(spell)) {
      DungeonLogger.warning("called learnSpell with " + spell.getName() + " which is already known.");
    } else {
      DungeonLogger.info("Learned " + spell.getName() + ".");
      spellList.add(spell);
    }
  }

  @Override
  public void parseCast(String[] arguments) {
    if (arguments.length > 0) {
      ParsingUtils.SplitResult splitResult = ParsingUtils.splitOnOn(arguments);
      String[] spellMatcher = splitResult.before;
      String[] targetMatcher = splitResult.after;
      Matches<Spell> matches = Utils.findBestCompleteMatches(spellList, spellMatcher);
      if (matches.size() == 0) {
        Writer.write("That did not match any spell you know.");
      }
      if (matches.getDifferentNames() == 1) {
        Spell spell = matches.getMatch(0);
        DungeonLogger.info("Casted " + spell.getName().getSingular() + ".");
        spell.operate(hero, targetMatcher);
      } else if (matches.getDifferentNames() > 1) {
        Writer.write("Provided input is ambiguous in respect to spell.");
      }
    } else {
      Writer.write("Cast what?");
    }
  }

}
