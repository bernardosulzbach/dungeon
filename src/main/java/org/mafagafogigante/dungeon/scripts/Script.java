package org.mafagafogigante.dungeon.scripts;

import org.mafagafogigante.dungeon.game.DungeonString;

public interface Script {

  ScriptIdentifier getIdentifier();

  DungeonString execute(CommandExecutor executor);

}
