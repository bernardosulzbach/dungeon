package org.mafagafogigante.dungeon.schema;

import org.mafagafogigante.dungeon.schema.rules.BoundIntegerJsonRule;
import org.mafagafogigante.dungeon.schema.rules.StringUppercaseJsonRule;

import java.util.Arrays;
import java.util.List;

public final class JsonRulesFactory {

  private final List<JsonRulePair> jsonRulePairs;

  private JsonRulesFactory(List<JsonRulePair> jsonRulePairs) {
    this.jsonRulePairs = jsonRulePairs;
  }

  public static JsonRulesFactory object(JsonRulePair... jsonRules) {
    return new JsonRulesFactory(Arrays.asList(jsonRules));
  }

  public static JsonRulePair pair(String elementName, JsonRule jsonRule) {
    return new JsonRulePair(elementName, jsonRule);
  }

  public static JsonRule boundIntegerJsonRule(int minValue, int maxValue) {
    return new BoundIntegerJsonRule(minValue, maxValue);
  }

  public static JsonRule stringUppercaseJsonRule() {
    return new StringUppercaseJsonRule();
  }

  public List<JsonRulePair> getJsonRulePairs() {
    return jsonRulePairs;
  }

}
