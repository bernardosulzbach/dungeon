package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntegerJsonRuleTest {

  private static final JsonRule integerJsonRule = new IntegerJsonRule();

  @Test
  public void integerJsonRuleShouldFailNonIntegerType() {
    final double invalidValue = 2.5;
    JsonValue jsonValue = Json.value(invalidValue);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      integerJsonRule.validate(jsonValue);
    });
  }

  @Test
  public void integerJsonRuleShouldPassValidType() {
    JsonValue jsonValue = Json.value(1);
    integerJsonRule.validate(jsonValue);
  }

}
