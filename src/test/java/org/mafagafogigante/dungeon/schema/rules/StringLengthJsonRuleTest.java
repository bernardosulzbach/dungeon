package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

public class StringLengthJsonRuleTest {

  private static final int VALIDATION_STRING_LENGTH = 2;
  private static final JsonRule stringLengthJsonRule = new StringLengthJsonRule(VALIDATION_STRING_LENGTH);

  @Test(expected = IllegalArgumentException.class)
  public void stringLengthJsonRuleShouldFailLowerLength() {
    String ltRequired = "a";
    JsonValue jsonValue = Json.value(ltRequired);
    stringLengthJsonRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void stringLengthJsonRuleShouldFailUpperLength() {
    String gtRequired = "abc";
    JsonValue jsonValue = Json.value(gtRequired);
    stringLengthJsonRule.validate(jsonValue);
  }

  @Test
  public void stringLengthJsonRuleShouldPassValidLength() {
    String eqRequired = "ab";
    JsonValue jsonValue = Json.value(eqRequired);
    stringLengthJsonRule.validate(jsonValue);
  }

}

