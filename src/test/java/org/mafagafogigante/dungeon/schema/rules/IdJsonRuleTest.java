package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

public class IdJsonRuleTest {

  private static final JsonRule idJsonRule = new IdJsonRule();

  @Test(expected = IllegalArgumentException.class)
  public void idJsonRuleShouldFailNonStringType() {
    JsonValue jsonValue = Json.value(true);
    idJsonRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void idJsonRuleShouldFailLowercaseChar() {
    JsonValue jsonValue = Json.value("mONSTER");
    idJsonRule.validate(jsonValue);
  }

  @Test
  public void idJsonRuleShouldPassNumberChar() {
    JsonValue jsonValue = Json.value("1");
    idJsonRule.validate(jsonValue);
  }

  @Test
  public void stringJsonRuleShouldPassUnderscoreChar() {
    JsonValue jsonValue = Json.value("MONSTER_A");
    idJsonRule.validate(jsonValue);
  }

}
