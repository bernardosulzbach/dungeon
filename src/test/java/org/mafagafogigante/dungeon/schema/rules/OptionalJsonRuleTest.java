package org.mafagafogigante.dungeon.schema.rules;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OptionalJsonRuleTest {

  private JsonRule optionalJsonRule;
  private JsonRule jsonRuleStub;

  @Before
  public void setUp() {
    jsonRuleStub = Mockito.mock(JsonRule.class);
    optionalJsonRule = new OptionalJsonRule(jsonRuleStub);
  }

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

