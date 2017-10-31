package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.util.RichText;
import org.mafagafogigante.dungeon.util.StandardRichTextBuilder;

import java.awt.Color;
import java.util.Locale;

/**
 * This class is uninstantiable and provides utility IO methods for AttackAlgorithm implementations.
 */
final class AttackAlgorithmWriter {

  private AttackAlgorithmWriter() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Writes a message about the inflicted damage based on the parameters.
   *
   * @param attacker the Creature that performed the attack
   * @param hitDamage the damage inflicted by the attacker
   * @param defender the target of the attack
   * @param criticalHit a boolean indicating if the attack was a critical hit or not
   */
  static void writeInflictedDamage(Creature attacker, int hitDamage, Creature defender, boolean criticalHit) {
    StandardRichTextBuilder builder = new StandardRichTextBuilder();
    builder.setColor(attacker.getId().equals(Game.getGameState().getHero().getId()) ? Color.GREEN : Color.RED);
    builder.append(attacker.getName().getSingular());
    builder.append(" inflicted ");
    builder.append(String.valueOf(hitDamage));
    builder.append(" damage points to ");
    builder.append(defender.getName().getSingular());
    if (criticalHit) {
      builder.append(" with a critical hit");
    }
    builder.append(".");
    builder.append(" It looks ");
    builder.append(defender.getHealth().getHealthState().toString().toLowerCase(Locale.ENGLISH));
    builder.append(".\n");
    Writer.getDefaultWriter().writeAndWait(builder.toRichText());
  }

  /**
   * Writes a miss message.
   *
   * @param attacker the attacker creature
   */
  static void writeMiss(Creature attacker) {
    StandardRichTextBuilder builder = new StandardRichTextBuilder();
    builder.setColor(Color.YELLOW);
    RichText text = builder.append(attacker.getName().toString()).append(" missed.\n").toRichText();
    Writer.getDefaultWriter().writeAndWait(text);
  }

  /**
   * Writes a weapon breakage message.
   *
   * @param weapon the weapon that broke, should be broken
   */
  static void writeWeaponBreak(Item weapon) {
    if (!weapon.isBroken()) {
      throw new IllegalArgumentException("weapon is not broken.");
    }
    StandardRichTextBuilder builder = new StandardRichTextBuilder();
    builder.setColor(Color.RED);
    RichText text = builder.append(weapon.getName().toString()).append(" broke!\n").toRichText();
    Writer.getDefaultWriter().write(text);
  }

}
