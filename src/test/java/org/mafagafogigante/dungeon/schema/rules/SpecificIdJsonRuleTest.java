package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

import java.util.Arrays;

public class SpecificIdJsonRuleTest {

  private static final JsonRule specificIdJsonRule =
      new SpecificIdJsonRule(Arrays.asList(new Id("ONE"), new Id("TWO"), new Id("TWO")));

  @Test(expected = IllegalArgumentException.class)
  public void specificIdJsonRuleShouldFailOnNonExistsId() {
    JsonValue invalidValue = Json.value("THREE");
    specificIdJsonRule.validate(invalidValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void specificIdJsonRuleShouldFailOnInvalidValue() {
    JsonValue invalidValue = Json.value(false);
    specificIdJsonRule.validate(invalidValue);
  }

  @Test
  public void specificIdJsonRuleShouldPassIfIdExists() {
    JsonValue value = Json.value("ONE");
    specificIdJsonRule.validate(value);
  }

}
