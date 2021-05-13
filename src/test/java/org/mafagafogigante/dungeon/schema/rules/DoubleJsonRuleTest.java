package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DoubleJsonRuleTest {

  private static final JsonRule doubleJsonRule = new DoubleJsonRule();

  @Test
  public void doubleJsonRuleShouldFailNonDoubleType() {
    JsonValue jsonValue = Json.value("string");
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      doubleJsonRule.validate(jsonValue);
    });
  }

  @Test
  public void doubleJsonRuleShouldPassValidType() {
    JsonValue jsonValue = Json.value(1.0);
    doubleJsonRule.validate(jsonValue);
  }

}
