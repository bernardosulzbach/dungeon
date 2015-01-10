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
    for (Skill skill : skillList) {
      if (skill.isReady()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the next Skill of the Rotation.
   *
   * @return a Skill.
   */
  public Skill getNextSkill() {
    Skill selectedSkill;
    if (skillList.isEmpty() || !hasReadySkill()) {
      selectedSkill = null;
    } else {
      // Get the next Skill that is ready.
      // As we checked that there is at least one ready Skill, this won't loop forever.
      int indexOfSelectedSkill = indexOfNextSkill;
      selectedSkill = skillList.get(indexOfSelectedSkill);
      if (selectedSkill.isReady()) {
        incrementIndexOfNextSkill();
      } else {
        do {
          indexOfSelectedSkill = (indexOfSelectedSkill + 1) % skillList.size();
          selectedSkill = skillList.get(indexOfSelectedSkill);
        }
        while (!selectedSkill.isReady());
      }
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

  /**
   * Restarts the rotation by resetting the remaining cool down of all Skills setting the index to the first Skill.
   * <p/>
   * This method should be invoked after the battle ends.
   */
  public void restartRotation() {
    indexOfNextSkill = 0;
    for (Skill skill : skillList) {
      skill.reset();
    }
  }

  /**
   * Refreshes the remaining cool down of all Skills in this SkillRotation.
   * <p/>
   * This method should be invoked after each turn.
   */
  public void refresh() {
    for (Skill skill : skillList) {
      skill.refresh();
    }
  }

  /**
   * Resets the SkillRotation, clearing the Skills that were added.
   */
  public void resetRotation() {
    skillList.clear();
  }

}
