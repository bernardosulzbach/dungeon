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

package org.dungeon.skill;

import org.dungeon.game.ID;
import org.dungeon.game.Name;
import org.dungeon.game.NameFactory;

import java.io.Serializable;

/**
 * SkillDefinition class that is what the name says: the definition of a Skill.
 * <p/>
 * Skills should be created based on already existing SkillDefinitions.
 * <p/>
 * A Skill differs from a SkillDefinition as it has Creature-specific data. For instance, all Fireballs share the same
 * definition, but each Creature's SkillList should have a specific Skill object for the Fireball, such that its
 * remaining cool down is not shared among different Creatures.
 */
public final class SkillDefinition implements Serializable {

  public final ID id;
  public final Name name;
  public final int damage;
  public final int repair;
  public final int coolDown;

  public SkillDefinition(String id, String name, int damage, int repair, int coolDown) {
    this.id = new ID(id);
    this.name = NameFactory.newInstance(name);
    this.damage = damage;
    this.repair = repair;
    this.coolDown = coolDown;
  }

}
