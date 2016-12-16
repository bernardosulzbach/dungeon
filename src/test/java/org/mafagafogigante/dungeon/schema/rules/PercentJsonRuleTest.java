package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Before;
import org.junit.Test;

public class PercentJsonRuleTest {

  private JsonRule percentJsonRule;

  @Before
  public void setUp() {
    percentJsonRule = new PercentJsonRule();
  }

  @Test(expected = IllegalArgumentException.class)
  public void  percentJsonRuleShouldFailInvalidPercentFormat() {
    JsonValue jsonValue = Json.value("two%");
    percentJsonRule.validate(jsonValue);
  }

  @Test
  public void percentJsonRuleShouldPassValidFormat() {
    JsonValue jsonValue = Json.value("2%");
    percentJsonRule.validate(jsonValue);
  }

}
