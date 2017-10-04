package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.game.Id;

import com.eclipsesource.json.JsonValue;

class IdJsonRule extends StringJsonRule {

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    try {
      new Id(value.asString());
    } catch (IllegalArgumentException invalidValue) {
      throw new IllegalArgumentException(value + " is not a valid Dungeon id.");
    }
  }

}
