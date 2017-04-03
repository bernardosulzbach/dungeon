package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.game.Id;

import com.eclipsesource.json.JsonValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class SpecificIdJsonRule extends IdJsonRule {

  private final Set<Id> ids = new HashSet<>();

  SpecificIdJsonRule(Collection<String> stringIds) {
    for (String stringId : stringIds) {
      ids.add(new Id(stringId));
    }
  }

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    Id testId = new Id(value.asString());
    boolean isValueExist = ids.contains(testId);
    if (!isValueExist) {
      throw new IllegalArgumentException(testId + " is not exist in next rule id list: " + ids + ".");
    }
  }

}
