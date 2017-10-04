package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

public class ArraySizeJsonRuleTest {

  private static final int ARRAY_VALIDATION_SIZE = 2;
  private static final JsonRule arraySizeJsonRule = new ArraySizeJsonRule(ARRAY_VALIDATION_SIZE);

  @Test(expected = IllegalArgumentException.class)
  public void arrayJsonSizeRuleShouldFailNonArrayType() {
    JsonValue jsonValue = Json.value(true);
    arraySizeJsonRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void arrayJsonSizeRuleShouldFailInvalidArraySize() {
    JsonArray jsonArray = new JsonArray();
    arraySizeJsonRule.validate(jsonArray);
  }

  @Test
  public void arrayJsonSizeRuleShouldPassValidArraySize() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(Json.value(true));
    jsonArray.add(Json.value(true));
    arraySizeJsonRule.validate(jsonArray);
  }

}
