package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.util.StandardRichTextBuilder;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

/**
 * An implementation of AttackAlgorithm that just writes to the screen.
 */
public class DummyAttackAlgorithm implements AttackAlgorithm {

  @Override
  public void renderAttack(@NotNull Creature attacker, @NotNull Creature defender) {
    StandardRichTextBuilder builder = new StandardRichTextBuilder();
    builder.setColor(Color.YELLOW).append(attacker.getName().toString()).append(" stands still.\n");
    Writer.getDefaultWriter().writeAndWait(builder.toRichText());
  }

}
