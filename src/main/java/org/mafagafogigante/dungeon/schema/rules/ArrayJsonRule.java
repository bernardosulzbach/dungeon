package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.JsonValue;

class ArrayJsonRule implements JsonRule {

  @Override
  public void validate(JsonValue value) {
    if (!value.isArray()) {
      throw new IllegalArgumentException(value + " is not an array.");
    }
  }

}
