package org.mafagafogigante.dungeon.scripts;

import org.mafagafogigante.dungeon.game.RichStringSequence;

import java.util.Locale;

public class FindEnchantedScript implements Script {

  @Override
  public ScriptIdentifier getIdentifier() {
    return new ScriptIdentifier("find-enchanted");
  }

  @Override
  public RichStringSequence execute(CommandExecutor executor) {
    // Go north until we find a enchanted item.
    boolean foundEnchanted;
    do {
      String result = executor.execute("go north").getString();
      foundEnchanted = result.toLowerCase(Locale.ENGLISH).contains("enchanted");
    } while (!foundEnchanted);
    return executor.execute("look");
  }

}
