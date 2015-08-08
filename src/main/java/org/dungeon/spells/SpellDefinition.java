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

import org.dungeon.game.Id;
import org.dungeon.game.Name;
import org.dungeon.game.NameFactory;

import java.io.Serializable;

/**
 * SpellDefinition class that contains immutable data that may be shared by multiple Spell objects.
 *
 * Equality is tested based on the Id field.
 */
final class SpellDefinition implements Serializable {

  public final Id id;
  // Use a name because in the future we may want to write stuff like "you casted 10 fireballs so far."
  public final Name name;

  public SpellDefinition(String id, String name) {
    this.id = new Id(id);
    this.name = NameFactory.newInstance(name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpellDefinition that = (SpellDefinition) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "SpellDefinition{" +
        "id=" + id +
        ", name=" + name +
        '}';
  }

}
