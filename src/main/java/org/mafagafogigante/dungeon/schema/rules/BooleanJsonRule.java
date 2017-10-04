package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.JsonValue;

class BooleanJsonRule implements JsonRule {

  @Override
  public void validate(JsonValue value) {
    if (!value.isBoolean()) {
      throw new IllegalArgumentException(value + " is not a boolean.");
    }
  }

}
