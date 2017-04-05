package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.game.Id;

import com.eclipsesource.json.JsonValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class SpecificIdJsonRule extends IdJsonRule {

  private final Set<Id> searchIds = new HashSet<>();

  SpecificIdJsonRule(Collection<Id> ids) {
    searchIds.addAll(ids);
  }

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    Id testId = new Id(value.asString());
    boolean isValueExists = searchIds.contains(testId);
    if (!isValueExists) {
      throw new IllegalArgumentException(testId + " is not a valid Id");
    }
  }

}
