package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.io.Writer;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

/**
 * An implementation of AttackAlgorithm that just writes to the screen.
 */
public class DummyAttackAlgorithm implements AttackAlgorithm {

  @Override
  public void renderAttack(@NotNull Creature attacker, @NotNull Creature defender) {
    Writer.writeAndWait(new DungeonString(attacker.getName() + " stands still.\n", Color.YELLOW));
  }

}
