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
 * SkillSet class.
 * <p/>
 * Created by Bernardo Sulzbach on 07/01/15.
 */
public class SkillList implements Serializable {

  private final ArrayList<Skill> list = new ArrayList<Skill>();

  public void addSkill(Skill skill) {
    list.add(skill);
  }

  public boolean hasSkill() {
    return !list.isEmpty();
  }

  public Skill getFirstSkill() {
    if (hasSkill()) {
      return list.get(0);
    }
    return null;
  }

}
