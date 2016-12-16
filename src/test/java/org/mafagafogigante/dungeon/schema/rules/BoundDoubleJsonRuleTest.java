package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Before;
import org.junit.Test;

public class BoundDoubleJsonRuleTest {

  private static final double MIN_BOUND_VALUE = 1.1d;
  private static final double MAX_BOUND_VALUE = 3.5d;

  private JsonRule boundDoubleJsonRule;

  @Before
  public void setUp() {
    boundDoubleJsonRule = new BoundDoubleJsonRule(MIN_BOUND_VALUE, MAX_BOUND_VALUE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void boundDoubleJsonRuleShouldFailValueOutOfLowerBound() {
    final double ltLowerBound = 1d;
    JsonValue jsonValue = Json.value(ltLowerBound);
    boundDoubleJsonRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void boundDoubleJsonRuleShouldFailValueOutOfUpperBound() {
    final double gtLowerBound = 5d;
    JsonValue jsonValue = Json.value(gtLowerBound);
    boundDoubleJsonRule.validate(jsonValue);
  }

  @Test
  public void boundDoubleJsonRuleShouldPassInBoundValue() {
    final double inBound = 2d;
    JsonValue jsonValue = Json.value(inBound);
    boundDoubleJsonRule.validate(jsonValue);
  }

}
