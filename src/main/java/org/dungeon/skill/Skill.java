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

import org.dungeon.creatures.Creature;
import org.dungeon.game.ID;
import org.dungeon.game.Name;
import org.dungeon.game.Selectable;
import org.dungeon.io.IO;
import org.dungeon.items.Item;
import org.dungeon.util.Constants;

import java.awt.Color;
import java.io.Serializable;

/**
 * The Skill class.
 * <p/>
 * Created by Bernardo Sulzbach on 07/01/15.
 */
public class Skill implements Selectable, Serializable {

  private final SkillDefinition definition;
  private int remainingCoolDown;

  public Skill(SkillDefinition definition) {
    this.definition = definition;
  }

  /**
   * Prints a message about the inflicted damage due to a casted Skill.
   *
   * @param skill  the Skill casted.
   * @param caster the Creature that performed the attack.
   * @param target the target of the attack.
   */
  public static void printSkillCast(Skill skill, Creature caster, Creature target) {
    StringBuilder builder = new StringBuilder();
    builder.append(caster.getName()).append(" casted ").append(skill.getName());
    if (skill.getDamage() > 0) {
      builder.append(" and inflicted ").append(skill.getDamage()).append(" damage points to ").append(target.getName());
    }
    builder.append(".");
    IO.writeBattleString(builder.toString(), caster.getID().equals(Constants.HERO_ID) ? Color.GREEN : Color.RED);
  }

  public ID getID() {
    return definition.id;
  }

  /**
   * Returns the damage of this Skill.
   *
   * @return the damage of this Skill.
   */
  public int getDamage() {
    return definition.damage;
  }

  public int getRepair() {
    return definition.repair;
  }

  /**
   * Starts the cool down of this Skill.
   */
  private void startCoolDown() {
    remainingCoolDown = definition.coolDown;
  }

  public void cast(Creature caster, Creature target) {
    target.takeDamage(getDamage());
    printSkillCast(this, caster, target);
    Item casterWeapon = caster.getWeapon();
    if (casterWeapon != null && casterWeapon.hasTag(Item.Tag.REPAIRABLE) && getRepair() > 0) {
      casterWeapon.incrementIntegrity(getRepair());
      IO.writeString(casterWeapon.getName() + " was repaired.");
    }
    startCoolDown();
  }

  /**
   * Verifies if this Skill is ready to be casted or not.
   */
  public boolean isReady() {
    return remainingCoolDown == 0;
  }

  /**
   * Refreshes the remaining cool down of this Skill.
   * <p/>
   * This method should be invoked after each turn.
   */
  void refresh() {
    if (remainingCoolDown > 0) {
      remainingCoolDown--;
    }
  }

  /**
   * Resets the remaining cool down of this Skill.
   * <p/>
   * This method should be invoked after the battle ends.
   */
  void reset() {
    remainingCoolDown = 0;
  }

  @Override
  public Name getName() {
    // Delegate to SkillDefinition.
    return definition.name;
  }

}
