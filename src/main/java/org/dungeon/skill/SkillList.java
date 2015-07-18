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

import org.dungeon.game.Id;
import org.dungeon.io.DungeonLogger;
import org.dungeon.io.Writer;
import org.dungeon.util.Selectable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SkillList class that stores references to all the Skills a Creature has.
 */
public class SkillList implements Serializable {

  private final List<Skill> skillList = new ArrayList<Skill>();

  /**
   * Add a Skill to this SkillList. If the Skill is already present, a warning will be logged.
   *
   * @param skill the Skill to be added.
   */
  public void addSkill(Skill skill) {
    if (hasSkill(skill.getId())) {
      DungeonLogger.warning("Tried to add an already present Skill to a SkillList!");
    } else {
      skillList.add(skill);
    }
  }

  /**
   * Checks if this SkillList has a Skill with a specified ID.
   *
   * @param skillId the ID.
   * @return a boolean.
   */
  public boolean hasSkill(Id skillId) {
    for (Skill skill : skillList) {
      if (skillId.equals(skill.getId())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the number of elements in this SkillList.
   */
  public int getSize() {
    return skillList.size();
  }

  /**
   * Prints the names of all Skills in this SkillList.
   */
  public void printSkillList() {
    for (Skill skill : skillList) {
      Writer.writeString(skill.getName().getSingular());
    }
  }

  /**
   * Returns a List of Selectable objects.
   *
   * @return a List of Selectable objects.
   */
  public List<Selectable> toListOfSelectable() {
    return new ArrayList<Selectable>(skillList);
  }

}
