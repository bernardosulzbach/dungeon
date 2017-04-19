package org.mafagafogigante.dungeon.entity.creatures;

/**
 * The class responsible for inflicting damage from one creature onto another.
 */
class DamageHandler {

  private DamageHandler() {
  }

  static void inflictDamage(Creature attacker, Creature defender, int damage) {
    defender.getHealth().decrementBy(damage);
    attacker.getBattleLog().incrementInflicted(damage);
    defender.getBattleLog().incrementTaken(damage);
  }

}
