package org.mafagafogigante.dungeon.schema.rules;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

class ArraySizeJsonRule extends ArrayJsonRule {

  private final int arraySize;

  ArraySizeJsonRule(int arraySize) {
    this.arraySize = arraySize;
  }

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    JsonArray jsonArray = value.asArray();
    if (jsonArray.size() != arraySize) {
      throw new IllegalArgumentException(jsonArray + " size is not valid.");
    }
  }

}
