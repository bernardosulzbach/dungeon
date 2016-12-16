package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Before;
import org.junit.Test;

public class DoubleJsonRuleTest {

  private JsonRule doubleJsonRule;

  @Before
  public void setUp() {
    doubleJsonRule = new DoubleJsonRule();
  }

  @Test(expected = IllegalArgumentException.class)
  public void doubleJsonRuleShouldFailNonDoubleType() {
    JsonValue jsonValue = Json.value("string");
    doubleJsonRule.validate(jsonValue);
  }

  @Test
  public void doubleJsonRuleShouldPassValidType() {
    JsonValue jsonValue = Json.value(1d);
    doubleJsonRule.validate(jsonValue);
  }

}
