package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

public class DoubleJsonRuleTest {

  private static final JsonRule doubleJsonRule = new DoubleJsonRule();

  @Test(expected = IllegalArgumentException.class)
  public void doubleJsonRuleShouldFailNonDoubleType() {
    JsonValue jsonValue = Json.value("string");
    doubleJsonRule.validate(jsonValue);
  }

  @Test
  public void doubleJsonRuleShouldPassValidType() {
    JsonValue jsonValue = Json.value(1.0);
    doubleJsonRule.validate(jsonValue);
  }

}
