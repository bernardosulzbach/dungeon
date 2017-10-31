package org.mafagafogigante.dungeon.scripts;

import org.mafagafogigante.dungeon.util.StandardRichTextBuilder;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class FindWellEnchantedScript implements Script {

  @Override
  public ScriptIdentifier getIdentifier() {
    return new ScriptIdentifier("find-well-enchanted");
  }

  @Override
  public StandardRichTextBuilder execute(CommandExecutor executor) {
    // Go north until we find a enchanted item.
    boolean foundMultiplyEnchanted = false;
    do {
      String result = executor.execute("go north").getString();
      if (result.toLowerCase(Locale.ENGLISH).contains("enchanted")) {
        String pickResult = executor.execute("pick enchanted").getString();
        if (pickResult.toLowerCase(Locale.ENGLISH).contains("added")) {
          String examinationResult = executor.execute("examine enchanted").getString();
          int lashes = StringUtils.countMatches(examinationResult.toLowerCase(Locale.ENGLISH), "lash");
          if (lashes < 2) {
            executor.execute("drop enchanted");
            continue;
          }
          foundMultiplyEnchanted = true;
        }
      }
    } while (!foundMultiplyEnchanted);
    return executor.execute("examine enchanted");
  }

}
