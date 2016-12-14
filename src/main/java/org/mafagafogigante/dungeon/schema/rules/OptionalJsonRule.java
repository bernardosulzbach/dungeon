package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.JsonValue;

public class OptionalJsonRule implements JsonRule {

  private final JsonRule rule;

  public OptionalJsonRule(JsonRule rule) {
    this.rule = rule;
  }

  @Override
  public void validate(JsonValue value) {
    if (value != null) {
      rule.validate(value);
    }
  }

}
