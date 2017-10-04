package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.List;

class FixedArrayJsonRule extends ArrayJsonRule {

  private final List<JsonRule> rules;

  FixedArrayJsonRule(List<JsonRule> rules) {
    this.rules = new ArrayList<>(rules);
  }

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    JsonArray array = value.asArray();
    if (rules.size() != array.size()) {
      throw new IllegalArgumentException("Array is not of the right size.");
    }
    for (int i = 0; i < rules.size(); i++) {
      rules.get(i).validate(array.values().get(i));
    }
  }

}
