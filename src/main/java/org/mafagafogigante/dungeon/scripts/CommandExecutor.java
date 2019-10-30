package org.mafagafogigante.dungeon.scripts;

import org.mafagafogigante.dungeon.game.RichStringSequence;

public interface CommandExecutor {

  RichStringSequence execute(String command);

}
