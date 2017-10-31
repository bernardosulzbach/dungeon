package org.mafagafogigante.dungeon.scripts;

import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.StandardRichTextBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptGroup {

  private final Map<ScriptIdentifier, Script> scripts = new HashMap<>();

  void addScript(Script script) {
    scripts.put(script.getIdentifier(), script);
  }

  public boolean hasScript(ScriptIdentifier identifier) {
    return scripts.containsKey(identifier);
  }

  public StandardRichTextBuilder executeScript(ScriptIdentifier identifier, CommandExecutor executor) {
    return scripts.get(identifier).execute(executor);
  }

  public Collection<ScriptIdentifier> getIdentifiers() {
    List<ScriptIdentifier> identifiers = new ArrayList<>(scripts.keySet());
    Collections.sort(identifiers, new ScriptIdentifierComparator());
    return identifiers;
  }

  private static final class ScriptIdentifierComparator implements Comparator<ScriptIdentifier>, Serializable {

    private static final long serialVersionUID = Version.MAJOR;

    @Override
    public int compare(ScriptIdentifier o1, ScriptIdentifier o2) {
      return o1.asString().compareTo(o2.asString());
    }
  }

}
