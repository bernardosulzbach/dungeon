package org.mafagafogigante.dungeon.schema.rules;

import com.eclipsesource.json.JsonValue;

class StringLengthJsonRule extends StringJsonRule {

  private int stringLength;

  StringLengthJsonRule(int stringLength) {
    this.stringLength = stringLength;
  }

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    String stringValue = value.asString();
    if (stringValue.length() < stringLength) {
      throw new IllegalArgumentException(value + " is below the allowed minimum " + stringLength + ".");
    }
    if (stringValue.length() > stringLength) {
      throw new IllegalArgumentException(value + " is below the allowed maximum " + stringLength + ".");
    }
  }

}
