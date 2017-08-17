package org.mafagafogigante.dungeon.scripts;

import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Writable;

import java.util.List;

public interface Script {

  ScriptIdentifier getIdentifier();

  DungeonString execute(CommandExecutor executor);

}
