package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.util.Percentage;

import com.eclipsesource.json.JsonValue;

class PercentJsonRule extends StringJsonRule {

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    try {
      Percentage.fromString(value.asString());
    } catch (Exception exception) {
      throw new IllegalArgumentException(value + " is not a dungeon percentage.");
    }
  }

}
