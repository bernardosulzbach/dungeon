package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VariableArrayJsonRuleTest {

  private static final JsonRule rule = new VariableArrayJsonRule(new BooleanJsonRule());

  @Test
  public void variableArrayJsonRuleShouldFailOnNonArrayType() {
    JsonValue jsonValue = Json.value(true);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      rule.validate(jsonValue);
    });
  }

  @Test
  public void variableArrayJsonRuleShouldPassOnEmptyArray() {
    JsonArray jsonArray = new JsonArray();
    rule.validate(jsonArray);
  }

  @Test
  public void variableArrayJsonRuleShouldPassOnSingleElementArray() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(true);
    rule.validate(jsonArray);
  }

  @Test
  public void variableArrayJsonRuleShouldFailOnInvalidSingleElementArray() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("A");
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      rule.validate(jsonArray);
    });
  }

  @Test
  public void variableArrayJsonRuleShouldPassOnMultipleElementArray() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(true);
    jsonArray.add(false);
    rule.validate(jsonArray);
  }

  @Test
  public void variableArrayJsonRuleShouldFailOnInvalidFirstElement() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("A");
    jsonArray.add(false);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      rule.validate(jsonArray);
    });
  }

  @Test
  public void variableArrayJsonRuleShouldFailOnInvalidSecondElement() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(true);
    jsonArray.add("B");
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      rule.validate(jsonArray);
    });
  }

  @Test
  public void variableArrayJsonRuleShouldFailOnInvalidElements() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("A");
    jsonArray.add("B");
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      rule.validate(jsonArray);
    });
  }

}
