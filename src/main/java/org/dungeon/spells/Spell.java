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

package org.dungeon.spells;

import org.dungeon.entity.creatures.Hero;
import org.dungeon.game.Id;
import org.dungeon.game.Name;
import org.dungeon.util.Selectable;

import java.io.Serializable;

/**
 * The class that represents a spell.
 */
public abstract class Spell implements Selectable, Serializable {

  private final SpellDefinition definition;

  Spell(String id, String name) {
    this.definition = new SpellDefinition(id, name);
  }

  public abstract void operate(Hero hero, String[] targetMatcher);

  public Id getId() {
    return definition.id; // Delegate to SpellDefinition.
  }

  @Override
  public Name getName() {
    return definition.name; // Delegate to SpellDefinition.
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Spell spell = (Spell) o;
    return definition.equals(spell.definition);
  }

  @Override
  public int hashCode() {
    return definition.hashCode();
  }

  @Override
  public String toString() {
    return getName().getSingular();
  }

}
