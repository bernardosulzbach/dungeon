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

package org.mafagafogigante.dungeon.entity.creatures;

import org.jetbrains.annotations.NotNull;

/**
 * An interface that defines a single renderAttack method that is invoked when a creature attacks another.
 */
interface AttackAlgorithm {

  /**
   * Renders an attack of the attacker on the defender.
   *
   * <p>If any creature dies due to the invocation of this method, it will have its causeOfDeath field set.
   *
   * @param attacker the attacker
   * @param defender the defender
   */
  void renderAttack(@NotNull Creature attacker, @NotNull Creature defender);

}
