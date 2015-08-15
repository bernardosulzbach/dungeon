/*
 * Copyright (C) 2015 Bernardo Sulzbach
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

package org.dungeon.entity.creatures;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class presents a static renderAttack method that handles attack rendering.
 */
final class AttackAlgorithms {

  private static final Map<AttackAlgorithmId, AttackAlgorithm> ATTACK_ALGORITHM_MAP =
      new EnumMap<AttackAlgorithmId, AttackAlgorithm>(AttackAlgorithmId.class);
  private static boolean uninitialized = true;

  private AttackAlgorithms() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Renders an attack of the attacker on the defender.
   *
   * @param attacker the Creature that is attacking
   * @param defender the Creature that is being attacked
   */
  public static void renderAttack(Creature attacker, Creature defender) {
    if (uninitialized) {
      initialize();
    }
    ATTACK_ALGORITHM_MAP.get(attacker.getAttackAlgorithmId()).renderAttack(attacker, defender);
  }

  private static void initialize() {
    registerAttackAlgorithm(AttackAlgorithmId.BAT, new BatAttackAlgorithm());
    registerAttackAlgorithm(AttackAlgorithmId.CRITTER, new CritterAttackAlgorithm());
    registerAttackAlgorithm(AttackAlgorithmId.DUMMY, new DummyAttackAlgorithm());
    registerAttackAlgorithm(AttackAlgorithmId.ORC, new OrcAttackAlgorithm());
    registerAttackAlgorithm(AttackAlgorithmId.SIMPLE, new SimpleAttackAlgorithm());
    validateMap();
    uninitialized = false;
  }

  /**
   * Registers an AttackAlgorithm using specified AttackAlgorithmId.
   */
  private static void registerAttackAlgorithm(AttackAlgorithmId id, AttackAlgorithm algorithm) {
    if (ATTACK_ALGORITHM_MAP.containsKey(id)) {
      throw new IllegalStateException("There is an AttackAlgorithm already defined for this AttackAlgorithmId!");
    }
    ATTACK_ALGORITHM_MAP.put(id, algorithm);
  }

  /**
   * Ensure that all AttackAlgorithmIDs are mapped to an AttackAlgorithm.
   */
  private static void validateMap() {
    for (AttackAlgorithmId id : AttackAlgorithmId.values()) {
      if (!ATTACK_ALGORITHM_MAP.containsKey(id)) {
        throw new AssertionError(id + " is not mapped to an AttackAlgorithm!");
      }
    }
  }

}
