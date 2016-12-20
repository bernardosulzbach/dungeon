package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.game.ColoredString;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Writable;
import org.mafagafogigante.dungeon.io.JsonObjectFactory;

import java.util.List;

/**
 * Tutorial class that contains the game tutorial.
 */
public class Tutorial extends Writable {

  private static final String text = JsonObjectFactory.makeJsonObject("tutorial.json").get("tutorial").asString();

  @Override
  public List<ColoredString> toColoredStringList() {
    return new DungeonString(text).toColoredStringList();
  }

}
