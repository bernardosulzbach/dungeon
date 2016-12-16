package org.mafagafogigante.dungeon.schema.rules;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;
import org.mockito.Mockito;

public class OptionalJsonRuleTest {

  private static final JsonRule jsonRuleStub = Mockito.mock(JsonRule.class);
  private static final JsonRule optionalJsonRule = new OptionalJsonRule(jsonRuleStub);

  @Test
  public void optionalJsonRuleShouldIgnoreNullValue() {
    optionalJsonRule.validate(null);
    Mockito.verify(jsonRuleStub, never()).validate(null);
  }

  @Test
  public void optionalJsonRuleShouldValidateNonNullValue() {
    JsonValue jsonValue = Json.value(1);
    optionalJsonRule.validate(jsonValue);
    Mockito.verify(jsonRuleStub, only()).validate(jsonValue);
  }

}
