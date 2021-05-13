package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArrayJsonRuleTest {

  private static final JsonRule arrayJsonRule = new ArrayJsonRule();

  @Test
  public void arrayJsonRuleShouldFailNonArrayType() {
    JsonValue jsonValue = Json.value(true);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      arrayJsonRule.validate(jsonValue);
    });
  }

  @Test
  public void arrayJsonRuleShouldPassArrayType() {
    JsonArray jsonArray = new JsonArray();
    arrayJsonRule.validate(jsonArray);
  }

}
