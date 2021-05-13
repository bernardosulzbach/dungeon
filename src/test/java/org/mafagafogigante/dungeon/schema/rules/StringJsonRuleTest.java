package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringJsonRuleTest {

  private static final JsonRule stringJsonRule = new StringJsonRule();

  @Test
  public void stringJsonRuleShouldFailNonStringType() {
    JsonValue jsonValue = Json.value(true);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      stringJsonRule.validate(jsonValue);
    });
  }

  @Test
  public void stringJsonRuleShouldPassStringType() {
    JsonValue jsonValue = Json.value("string");
    stringJsonRule.validate(jsonValue);
  }

}
