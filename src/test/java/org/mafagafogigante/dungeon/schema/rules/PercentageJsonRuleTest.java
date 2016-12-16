package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

public class PercentageJsonRuleTest {

  private static final JsonRule percentJsonRule = new PercentageJsonRule();

  @Test(expected = IllegalArgumentException.class)
  public void percentageJsonRuleShouldFailOnInvalidPercentageFormat() {
    JsonValue jsonValue = Json.value("A%");
    percentJsonRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void percentageJsonRuleShouldFailOnValueBelowValidRange() {
    JsonValue jsonValue = Json.value("-1.0%");
    percentJsonRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void percentageJsonRuleShouldFailOnValueAboveValidRange() {
    JsonValue jsonValue = Json.value("101.0%");
    percentJsonRule.validate(jsonValue);
  }

  @Test
  public void percentageJsonRuleShouldPassOnValidFormatAndRange() {
    JsonValue jsonValue = Json.value("2%");
    percentJsonRule.validate(jsonValue);
  }

}
