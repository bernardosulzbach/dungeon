package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.game.Random;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.util.RichString;
import org.mafagafogigante.dungeon.util.StandardRichString;
import org.mafagafogigante.dungeon.util.StandardRichText;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

/**
 * An implementation of AttackAlgorithm that just writes to the screen.
 */
public class CritterAttackAlgorithm implements AttackAlgorithm {

  private static final Color COLOR = Color.YELLOW;

  @Override
  public void renderAttack(@NotNull Creature attacker, @NotNull Creature defender) {
    if (Random.nextBoolean()) {
      String message = attacker.getName() + " does nothing.\n";
      List<RichString> richStringList = Collections.<RichString>singletonList(new StandardRichString(message, COLOR));
      StandardRichText writable = new StandardRichText(richStringList);
      Writer.getDefaultWriter().writeAndWait(writable);
    } else {
      String message = attacker.getName() + " tries to run away.\n";
      List<RichString> richStringList = Collections.<RichString>singletonList(new StandardRichString(message, COLOR));
      StandardRichText writable = new StandardRichText(richStringList);
      Writer.getDefaultWriter().writeAndWait(writable);
    }
  }

}
