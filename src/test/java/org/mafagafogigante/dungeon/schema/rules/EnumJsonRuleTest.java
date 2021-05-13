package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnumJsonRuleTest {

  private static final String INVALID_ENUM_VALUE_UPPERCASE = "TWO";
  private static final String INVALID_ENUM_VALUE_LOWERCASE = "one";
  private static final JsonRule enumJsonRule = new EnumJsonRule<>(TestEnum.class);

  @Test
  public void enumJsonRuleShouldPassValidEnumValue() {
    JsonValue jsonValue = Json.value(TestEnum.ONE.toString());
    enumJsonRule.validate(jsonValue);
  }

  @Test
  public void enumJsonRuleShouldFailOnNonExistentEnumValue() {
    JsonValue jsonValue = Json.value(INVALID_ENUM_VALUE_UPPERCASE);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      enumJsonRule.validate(jsonValue);
    });
  }

  @Test
  public void enumJsonRuleShouldFailOnNonStringValue() {
    JsonValue jsonValue = Json.value(true);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      enumJsonRule.validate(jsonValue);
    });
  }

  @Test
  public void enumJsonRuleShouldFailOnLowercaseValue() {
    JsonValue jsonValue = Json.value(INVALID_ENUM_VALUE_LOWERCASE);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      enumJsonRule.validate(jsonValue);
    });
  }

  @Test
  public void enumJsonRuleShouldFailOnEmptyValue() {
    JsonValue jsonValue = Json.value("");
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      enumJsonRule.validate(jsonValue);
    });
  }

  private enum TestEnum {
    ONE
  }

}
