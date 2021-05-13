package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BooleanJsonRuleTest {

  private static final JsonRule booleanJsonRule = new BooleanJsonRule();

  @Test
  public void booleanJsonRuleShouldFailNonBooleanType() {
    JsonValue jsonValue = Json.value("string");
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      booleanJsonRule.validate(jsonValue);
    });
  }

  @Test
  public void booleanJsonRuleShouldPassBooleanType() {
    JsonValue jsonValue = Json.value(false);
    booleanJsonRule.validate(jsonValue);
  }

}
