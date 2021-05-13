package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringLengthJsonRuleTest {

  private static final int VALIDATION_STRING_LENGTH = 2;
  private static final JsonRule stringLengthJsonRule = new StringLengthJsonRule(VALIDATION_STRING_LENGTH);

  @Test
  public void stringLengthJsonRuleShouldFailLowerLength() {
    String ltRequired = "a";
    JsonValue jsonValue = Json.value(ltRequired);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      stringLengthJsonRule.validate(jsonValue);
    });
  }

  @Test
  public void stringLengthJsonRuleShouldFailUpperLength() {
    String gtRequired = "abc";
    JsonValue jsonValue = Json.value(gtRequired);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      stringLengthJsonRule.validate(jsonValue);
    });
  }

  @Test
  public void stringLengthJsonRuleShouldPassValidLength() {
    String eqRequired = "ab";
    JsonValue jsonValue = Json.value(eqRequired);
    stringLengthJsonRule.validate(jsonValue);
  }

}

