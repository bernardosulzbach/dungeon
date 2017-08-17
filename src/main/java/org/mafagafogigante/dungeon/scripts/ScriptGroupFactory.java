package org.mafagafogigante.dungeon.scripts;

public class ScriptGroupFactory {

  private ScriptGroupFactory() {
    throw new AssertionError();
  }

  public static ScriptGroup makeScriptGroup() {
    ScriptGroup scriptGroup = new ScriptGroup();
    scriptGroup.addScript(new FindEnchantedScript());
    scriptGroup.addScript(new FindWellEnchantedScript());
    return scriptGroup;
  }

}
