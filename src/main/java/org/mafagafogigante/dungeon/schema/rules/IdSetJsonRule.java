package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.game.Id;

import com.eclipsesource.json.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IdSetJsonRule extends StringJsonRule {

  private final Set<Id> idSet;

  IdSetJsonRule(@NotNull Collection<Id> ids) {
    idSet = new HashSet<>(ids);
  }

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    try {
      Id id = new Id(value.asString());
      if (!idSet.contains(id)) {
        throw new IllegalArgumentException(value + " is not in the set of valid ids.");
      }
    } catch (IllegalArgumentException invalidValue) {
      throw new IllegalArgumentException(value + " is not a valid Dungeon id.");
    }
  }

}
