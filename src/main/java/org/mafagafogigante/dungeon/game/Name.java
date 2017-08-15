package org.mafagafogigante.dungeon.game;

public interface Name {

  /**
   * Returns the singular form of this Name.
   */
  String getSingular();

  /**
   * Returns the plural form of this Name.
   */
  String getPlural();

  String getQuantifiedName(int quantity);

}
