package org.mafagafogigante.dungeon.commands;

import java.util.Collections;
import java.util.List;

public class IssuedCommandEvaluation {

  private final boolean valid;
  private final List<String> suggestions;

  IssuedCommandEvaluation(boolean valid, List<String> suggestions) {
    this.valid = valid;
    this.suggestions = suggestions;
  }

  public boolean isValid() {
    return valid;
  }

  public List<String> getSuggestions() {
    return Collections.unmodifiableList(suggestions);
  }

  @Override
  public String toString() {
    return "IssuedCommandEvaluation{" +
        "valid=" + valid +
        ", suggestions=" + suggestions +
        '}';
  }

}
