package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BoundIntegerJsonRuleTest {

  private static final int MIN_BOUND_VALUE = 2;
  private static final int MAX_BOUND_VALUE = 5;
  private static final JsonRule boundIntegerJsonRule = new BoundIntegerJsonRule(MIN_BOUND_VALUE, MAX_BOUND_VALUE);

  @Test
  public void boundIntegerJsonRuleShouldFailWhenValueOutOfLowerBound() {
    final int lowerThanLowerBound = 1;
    JsonValue jsonValue = Json.value(lowerThanLowerBound);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      boundIntegerJsonRule.validate(jsonValue);
    });
  }

  @Test
  public void boundIntegerJsonRuleShouldFailWhenValueOutOfUpperBound() {
    final int greaterThanUpperBound = 6;
    JsonValue jsonValue = Json.value(greaterThanUpperBound);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      boundIntegerJsonRule.validate(jsonValue);
    });
  }

  @Test
  public void boundIntegerJsonRuleShouldPassWhenValueInBound() {
    final int valid = 3;
    JsonValue jsonValue = Json.value(valid);
    boundIntegerJsonRule.validate(jsonValue);
  }

}
