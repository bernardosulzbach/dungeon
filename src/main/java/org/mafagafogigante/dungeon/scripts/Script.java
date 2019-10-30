package org.mafagafogigante.dungeon.scripts;

import org.mafagafogigante.dungeon.game.RichStringSequence;

public interface Script {

  ScriptIdentifier getIdentifier();

  RichStringSequence execute(CommandExecutor executor);

}
