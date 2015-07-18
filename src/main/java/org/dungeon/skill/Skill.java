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

import org.dungeon.entity.creatures.Creature;
import org.dungeon.entity.items.Item;
import org.dungeon.game.Game;
import org.dungeon.game.Id;
import org.dungeon.game.Name;
import org.dungeon.io.Writer;
import org.dungeon.util.Selectable;

import java.awt.Color;
import java.io.Serializable;

/**
 * The Skill class.
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
   * @param skill the Skill casted
   * @param caster the Creature that performed the attack
   * @param target the target of the attack
   */
  private static void printSkillCast(Skill skill, Creature caster, Creature target) {
    StringBuilder builder = new StringBuilder();
    builder.append(caster.getName()).append(" casted ").append(skill.getName());
    if (skill.getDamage() > 0) {
      builder.append(" and inflicted ").append(skill.getDamage()).append(" damage points to ").append(target.getName());
    }
    builder.append(".");
    Id heroId = Game.getGameState().getHero().getId();
    Writer.writeBattleString(builder.toString(), caster.getId().equals(heroId) ? Color.GREEN : Color.RED);
  }

  public Id getId() {
    return definition.id;
  }

  /**
   * Returns the damage of this Skill.
   *
   * @return the damage of this Skill.
   */
  private int getDamage() {
    return definition.damage;
  }

  private int getRepair() {
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
      Writer.writeString(casterWeapon.getName() + " was repaired.");
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
   *
   * <p>This method should be invoked after each turn.
   */
  void refresh() {
    if (remainingCoolDown > 0) {
      remainingCoolDown--;
    }
  }

  /**
   * Resets the remaining cool down of this Skill.
   *
   * <p>This method should be invoked after the battle ends.
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
