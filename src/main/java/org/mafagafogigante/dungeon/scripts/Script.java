package org.mafagafogigante.dungeon.scripts;

import org.mafagafogigante.dungeon.util.StandardRichTextBuilder;

public interface Script {

  ScriptIdentifier getIdentifier();

  StandardRichTextBuilder execute(CommandExecutor executor);

}
