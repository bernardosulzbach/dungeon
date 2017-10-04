package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

class VariableArrayJsonRule extends ArrayJsonRule {

  private final JsonRule rule;

  VariableArrayJsonRule(JsonRule rule) {
    this.rule = rule;
  }

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    JsonArray array = value.asArray();
    for (JsonValue arrayValue : array.values()) {
      rule.validate(arrayValue);
    }
  }

}
