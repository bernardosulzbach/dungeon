package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.JsonValue;

class NumberJsonRule implements JsonRule {

  @Override
  public void validate(JsonValue value) {
    if (!value.isNumber()) {
      throw new IllegalArgumentException(value + " is not a number.");
    }
  }

}
