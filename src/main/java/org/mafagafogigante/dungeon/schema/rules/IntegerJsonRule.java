package org.mafagafogigante.dungeon.schema.rules;

import com.eclipsesource.json.JsonValue;

class IntegerJsonRule extends NumberJsonRule {

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    try {
      value.asInt();
    } catch (NumberFormatException exception) {
      throw new IllegalArgumentException(value + " is not an integer.");
    }
  }

}
