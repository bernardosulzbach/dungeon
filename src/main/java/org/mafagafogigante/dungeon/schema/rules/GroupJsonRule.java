package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.JsonValue;

import java.util.Arrays;
import java.util.List;

class GroupJsonRule implements JsonRule {

  private final List<JsonRule> rules;

  GroupJsonRule(JsonRule... rules) {
    this.rules = Arrays.asList(rules);
  }

  @Override
  public void validate(JsonValue value) {
    for (JsonRule rule : rules) {
      rule.validate(value);
    }
  }

}
