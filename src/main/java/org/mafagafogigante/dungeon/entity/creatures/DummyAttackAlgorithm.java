package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.game.RichStringSequence;
import org.mafagafogigante.dungeon.io.Writer;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

/**
 * An implementation of AttackAlgorithm that just writes to the screen.
 */
public class DummyAttackAlgorithm implements AttackAlgorithm {

  @Override
  public void renderAttack(@NotNull Creature attacker, @NotNull Creature defender) {
    RichStringSequence string = new RichStringSequence(attacker.getName() + " stands still.\n", Color.YELLOW);
    Writer.getDefaultWriter().writeAndWait(string);
  }

}
