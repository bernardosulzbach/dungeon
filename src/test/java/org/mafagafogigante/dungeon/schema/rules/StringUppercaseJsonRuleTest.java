package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

public class StringUppercaseJsonRuleTest {

  private static final JsonRule uppercaseStringJsonRule = new UppercaseStringJsonRule();

  @Test(expected = IllegalArgumentException.class)
  public void uppercaseStringJsonRuleShouldFailLowercase() {
    String lowercase = "a";
    JsonValue jsonValue = Json.value(lowercase);
    uppercaseStringJsonRule.validate(jsonValue);
  }

  @Test
  public void uppercaseStringJsonRuleShouldPassUppercase() {
    String uppercase = "A";
    JsonValue jsonValue = Json.value(uppercase);
    uppercaseStringJsonRule.validate(jsonValue);
  }

}
