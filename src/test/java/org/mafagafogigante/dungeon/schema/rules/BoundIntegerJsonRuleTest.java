package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

public class BoundIntegerJsonRuleTest {

  private static final int MIN_BOUND_VALUE = 2;
  private static final int MAX_BOUND_VALUE = 5;
  private static final JsonRule boundIntegerJsonRule = new BoundIntegerJsonRule(MIN_BOUND_VALUE, MAX_BOUND_VALUE);

  @Test(expected = IllegalArgumentException.class)
  public void boundIntegerJsonRuleShouldFailWhenValueOutOfLowerBound() {
    final int ltLowerBound = 1;
    JsonValue jsonValue = Json.value(ltLowerBound);
    boundIntegerJsonRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void boundIntegerJsonRuleShouldFailWhenValueOutOfUpperBound() {
    final int gtLowerBound = 6;
    JsonValue jsonValue = Json.value(gtLowerBound);
    boundIntegerJsonRule.validate(jsonValue);
  }

  @Test
  public void boundIntegerJsonRuleShouldPassWhenValueInBound() {
    final int inBound = 3;
    JsonValue jsonValue = Json.value(inBound);
    boundIntegerJsonRule.validate(jsonValue);
  }

}
