package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SpecificIdJsonRuleTest {

  private static final List<Id> TEST_IDS = Arrays.asList(new Id("ONE"), new Id("TWO"), new Id("TWO"));
  private static final JsonRule specificIdJsonRule = new SpecificIdJsonRule(TEST_IDS);

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
