package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.JsonArray;
import org.junit.Test;

public class GroupJsonRuleTest {

  private static final int MIN_ELEMENT_VALUE = 0;
  private static final int MAX_ELEMENT_VALUE = 5;
  private static final int REQUIRED_ARRAY_SIZE = 2;
  private static final JsonRule groupRule = new GroupJsonRule(
      JsonRuleFactory.makeVariableArrayRule(JsonRuleFactory.makeBoundIntegerRule(MIN_ELEMENT_VALUE, MAX_ELEMENT_VALUE)),
      JsonRuleFactory.makeArraySizeRule(REQUIRED_ARRAY_SIZE));

  @Test(expected = IllegalArgumentException.class)
  public void groupRuleShouldFailOnBoundIntegerRule() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(6);
    jsonArray.add(6);
    groupRule.validate(jsonArray);
  }

  @Test(expected = IllegalArgumentException.class)
  public void groupRuleShouldFailOnArraySizeRule() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(1);
    jsonArray.add(2);
    jsonArray.add(3);
    groupRule.validate(jsonArray);
  }

  @Test
  public void groupRuleShouldPassOnValidObject() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(1);
    jsonArray.add(2);
    groupRule.validate(jsonArray);
  }

}
