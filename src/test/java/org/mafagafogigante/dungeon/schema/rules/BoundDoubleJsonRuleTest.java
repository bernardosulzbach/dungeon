package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

public class BoundDoubleJsonRuleTest {

  private static final double MIN_BOUND_VALUE = 1.5;
  private static final double MAX_BOUND_VALUE = 3.5;
  private static final JsonRule boundDoubleJsonRule = new BoundDoubleJsonRule(MIN_BOUND_VALUE, MAX_BOUND_VALUE);

  @Test(expected = IllegalArgumentException.class)
  public void boundDoubleJsonRuleShouldFailValueOutOfLowerBound() {
    final double lowerThanLowerBound = 1.0;
    JsonValue jsonValue = Json.value(lowerThanLowerBound);
    boundDoubleJsonRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void boundDoubleJsonRuleShouldFailValueOutOfUpperBound() {
    final double greaterThanUpperBound = 5.0;
    JsonValue jsonValue = Json.value(greaterThanUpperBound);
    boundDoubleJsonRule.validate(jsonValue);
  }

  @Test
  public void boundDoubleJsonRuleShouldPassInBoundValue() {
    final double valid = 2.0;
    JsonValue jsonValue = Json.value(valid);
    boundDoubleJsonRule.validate(jsonValue);
  }

}
