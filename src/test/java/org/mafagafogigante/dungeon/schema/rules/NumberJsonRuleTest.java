package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

public class NumberJsonRuleTest {

  private static final JsonRule numberJsonRule = new NumberJsonRule();

  @Test(expected = IllegalArgumentException.class)
  public void numberJsonRuleShouldFailNonNumberType() {
    JsonValue jsonValue = Json.value("string");
    numberJsonRule.validate(jsonValue);
  }

  @Test
  public void numberJsonRuleShouldPassNumberType() {
    JsonValue jsonValue = Json.value(1);
    numberJsonRule.validate(jsonValue);
  }

}
