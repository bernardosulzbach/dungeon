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

import java.io.Serializable;
import java.util.ArrayList;

/**
 * SkillRotation class that represents a rotation of Skills.
 * <p/>
 * Created by Bernardo Sulzbach on 07/01/15.
 */
public class SkillRotation implements Serializable {

  private final ArrayList<Skill> skillList = new ArrayList<Skill>();
  private int indexOfNextSkill;

  public void addSkill(Skill skill) {
    skillList.add(skill);
  }

  public boolean hasReadySkill() {
    return true;
  }

  /**
   * Returns the next Skill of the Rotation.
   *
   * @return a Skill.
   */
  public Skill getNextSkill() {
    Skill selectedSkill;
    if (skillList.isEmpty()) {
      selectedSkill = null;
    } else {
      selectedSkill = skillList.get(indexOfNextSkill);
      incrementIndexOfNextSkill();
    }
    return selectedSkill;
  }

  /**
   * Increments the index of the next Skill to be returned by getNextSkill().
   */
  private void incrementIndexOfNextSkill() {
    if (!skillList.isEmpty()) {
      indexOfNextSkill = (indexOfNextSkill + 1) % skillList.size();
    }
  }

  public void restartRotation() {
    indexOfNextSkill = 0;
  }

}
