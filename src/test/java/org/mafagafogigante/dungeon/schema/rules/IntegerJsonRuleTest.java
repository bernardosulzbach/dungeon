package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Before;
import org.junit.Test;

public class IntegerJsonRuleTest {

  private JsonRule integerJsonRule;

  @Before
  public void setUp() {
    integerJsonRule = new IntegerJsonRule();
  }

  @Test(expected = IllegalArgumentException.class)
  public void integerJsonRuleShouldFailNonIntegerType() {
    final double invalidValue = 2.1d;
    JsonValue jsonValue = Json.value(invalidValue);
    integerJsonRule.validate(jsonValue);
  }

  @Test
  public void integerJsonRuleShouldPassValidType() {
    JsonValue jsonValue = Json.value(1);
    integerJsonRule.validate(jsonValue);
  }

}
