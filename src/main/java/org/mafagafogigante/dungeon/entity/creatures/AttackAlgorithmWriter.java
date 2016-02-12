package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.io.Writer;

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
    DungeonString string = new DungeonString();
    string.setColor(attacker.getId().equals(Game.getGameState().getHero().getId()) ? Color.GREEN : Color.RED);
    string.append(attacker.getName().getSingular());
    string.append(" inflicted ");
    string.append(String.valueOf(hitDamage));
    string.append(" damage points to ");
    string.append(defender.getName().getSingular());
    if (criticalHit) {
      string.append(" with a critical hit");
    }
    string.append(".");
    string.append(" It looks ");
    string.append(defender.getHealth().getHealthState().toString().toLowerCase(Locale.ENGLISH));
    string.append(".\n");
    Writer.writeAndWait(string);
  }

  /**
   * Writes a miss message.
   *
   * @param attacker the attacker creature
   */
  static void writeMiss(Creature attacker) {
    Writer.writeAndWait(new DungeonString(attacker.getName() + " missed.\n", Color.YELLOW));
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
    Writer.write(new DungeonString(weapon.getName() + " broke!\n", Color.RED));
  }

}
