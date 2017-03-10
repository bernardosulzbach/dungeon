package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

public class EnumJsonRuleTest {

  private static final String INVALID_ENUM_VALUE_UPPERCASE = "TWO";
  private static final String INVALID_ENUM_VALUE_LOWERCASE = "one";
  private static final JsonRule enumJsonRule = new EnumJsonRule<>(TestEnum.class);

  @Test
  public void enumJsonRuleShouldPassValidEnumValue() {
    JsonValue jsonValue = Json.value(TestEnum.ONE.toString());
    enumJsonRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void enumJsonRuleShouldFailOnNonExistentEnumValue() {
    JsonValue jsonValue = Json.value(INVALID_ENUM_VALUE_UPPERCASE);
    enumJsonRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void enumJsonRuleShouldFailOnNonStringValue() {
    JsonValue jsonValue = Json.value(true);
    enumJsonRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void enumJsonRuleShouldFailOnLowercaseValue() {
    JsonValue jsonValue = Json.value(INVALID_ENUM_VALUE_LOWERCASE);
    enumJsonRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void enumJsonRuleShouldFailOnEmptyValue() {
    JsonValue jsonValue = Json.value("");
    enumJsonRule.validate(jsonValue);
  }

  private enum TestEnum {
    ONE
  }

}
