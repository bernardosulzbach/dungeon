package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Before;
import org.junit.Test;

public class BooleanJsonRuleTest {

  private JsonRule booleanJsonRule;

  @Before
  public void setUp() {
    booleanJsonRule = new BooleanJsonRule();
  }

  @Test(expected = IllegalArgumentException.class)
  public void booleanJsonRuleShouldFailNonBooleanType() {
    JsonValue jsonValue = Json.value("string");
    booleanJsonRule.validate(jsonValue);
  }

  @Test
  public void booleanJsonRuleShouldPassBooleanType() {
    JsonValue jsonValue = Json.value(false);
    booleanJsonRule.validate(jsonValue);
  }

}
