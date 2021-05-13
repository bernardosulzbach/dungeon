package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BoundDoubleJsonRuleTest {

  private static final double MIN_BOUND_VALUE = -1.5;
  private static final double MAX_BOUND_VALUE = 3.5;
  private static final JsonRule boundDoubleJsonRule = new BoundDoubleJsonRule(MIN_BOUND_VALUE, MAX_BOUND_VALUE);

  @Test
  public void boundDoubleJsonRuleShouldFailValueOutOfLowerBound() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      boundDoubleJsonRule.validate(Json.value(Math.nextAfter(MIN_BOUND_VALUE, Double.NEGATIVE_INFINITY)));
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      boundDoubleJsonRule.validate(Json.value(Double.NEGATIVE_INFINITY));
    });
  }

  @Test
  public void boundDoubleJsonRuleShouldFailValueOutOfUpperBound() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      boundDoubleJsonRule.validate(Json.value(Math.nextAfter(MAX_BOUND_VALUE, Double.POSITIVE_INFINITY)));
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      boundDoubleJsonRule.validate(Json.value(Double.POSITIVE_INFINITY));
    });
  }

  @Test
  public void boundDoubleJsonRuleShouldPassInBoundValue() {
    final double valid = 2.0;
    JsonValue jsonValue = Json.value(valid);
    boundDoubleJsonRule.validate(jsonValue);
  }

}
