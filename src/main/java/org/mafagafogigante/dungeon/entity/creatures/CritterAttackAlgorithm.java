package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Random;
import org.mafagafogigante.dungeon.io.Writer;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

/**
 * An implementation of AttackAlgorithm that just writes to the screen.
 */
public class CritterAttackAlgorithm implements AttackAlgorithm {

  @Override
  public void renderAttack(@NotNull Creature attacker, @NotNull Creature defender) {
    if (Random.nextBoolean()) {
      Writer.writeAndWait(new DungeonString(attacker.getName() + " does nothing.\n", Color.YELLOW));
    } else {
      Writer.writeAndWait(new DungeonString(attacker.getName() + " tries to run away.\n", Color.YELLOW));
    }
  }

}
