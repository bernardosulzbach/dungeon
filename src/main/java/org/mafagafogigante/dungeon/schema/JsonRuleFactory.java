package org.mafagafogigante.dungeon.schema;

import org.mafagafogigante.dungeon.schema.rules.BoundIntegerJsonRule;
import org.mafagafogigante.dungeon.schema.rules.IntegerJsonRule;
import org.mafagafogigante.dungeon.schema.rules.ObjectJsonRule;
import org.mafagafogigante.dungeon.schema.rules.OptionalJsonRule;

import java.util.Map;

public final class JsonRuleFactory {

  private JsonRuleFactory() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  public static JsonRule makeObjectRule(Map<String, JsonRule> rules) {
    return new ObjectJsonRule(rules);
  }

  public static JsonRule makeIntegerRule() {
    return new IntegerJsonRule();
  }

  public static JsonRule makeBoundIntegerRule(int minimumValue, int maximumValue) {
    return new BoundIntegerJsonRule(minimumValue, maximumValue);
  }

  public static JsonRule makeOptionalRule(JsonRule jsonRule) {
    return new OptionalJsonRule(jsonRule);
  }

}
