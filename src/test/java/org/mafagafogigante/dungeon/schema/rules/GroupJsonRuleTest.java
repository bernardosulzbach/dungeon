package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.JsonArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GroupJsonRuleTest {

  private static final int MIN_ELEMENT_VALUE = 0;
  private static final int MAX_ELEMENT_VALUE = 5;
  private static final int REQUIRED_ARRAY_SIZE = 2;
  private static final JsonRule groupRule = new GroupJsonRule(
          JsonRuleFactory.makeVariableArrayRule(
                  JsonRuleFactory.makeBoundIntegerRule(MIN_ELEMENT_VALUE, MAX_ELEMENT_VALUE)),
          JsonRuleFactory.makeArraySizeRule(REQUIRED_ARRAY_SIZE));

  @Test
  public void groupRuleShouldFailOnBoundIntegerRule() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(6);
    jsonArray.add(6);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      groupRule.validate(jsonArray);
    });
  }

  @Test
  public void groupRuleShouldFailOnArraySizeRule() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(1);
    jsonArray.add(2);
    jsonArray.add(3);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      groupRule.validate(jsonArray);
    });
  }

  @Test
  public void groupRuleShouldPassOnValidObject() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(1);
    jsonArray.add(2);
    groupRule.validate(jsonArray);
  }

}
