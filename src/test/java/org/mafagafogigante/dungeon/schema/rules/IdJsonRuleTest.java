package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IdJsonRuleTest {

  private static final JsonRule idJsonRule = new IdJsonRule();

  @Test
  public void idJsonRuleShouldFailNonStringType() {
    JsonValue jsonValue = Json.value(true);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      idJsonRule.validate(jsonValue);
    });
  }

  @Test
  public void idJsonRuleShouldFailLowercaseChar() {
    JsonValue jsonValue = Json.value("mONSTER");
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      idJsonRule.validate(jsonValue);
    });
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
