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
