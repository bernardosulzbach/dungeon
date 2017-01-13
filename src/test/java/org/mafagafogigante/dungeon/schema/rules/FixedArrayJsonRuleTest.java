package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class FixedArrayJsonRuleTest {

  private static final JsonRule stringRule = new StringJsonRule();
  private static final JsonRule integerRule = new IntegerJsonRule();
  private static final JsonRule emptyArrayRule = new FixedArrayJsonRule(Collections.<JsonRule>emptyList());
  private static final JsonRule stringArrayRule = new FixedArrayJsonRule(Collections.singletonList(stringRule));
  private static final JsonRule integerArrayRule = new FixedArrayJsonRule(Collections.singletonList(integerRule));
  private static final JsonRule stringIntegerArrayRule = new FixedArrayJsonRule(Arrays.asList(stringRule, integerRule));

  @Test(expected = IllegalArgumentException.class)
  public void fixedEmptyArrayRuleShouldFailOnNonArrayType() {
    JsonValue jsonValue = Json.value(true);
    emptyArrayRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fixedStringArrayRuleShouldFailOnNonArrayType() {
    JsonValue jsonValue = Json.value(true);
    stringArrayRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fixedIntegerArrayRuleShouldFailOnNonArrayType() {
    JsonValue jsonValue = Json.value(true);
    integerArrayRule.validate(jsonValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fixedStringIntegerArrayRuleShouldFailOnNonArrayType() {
    JsonValue jsonValue = Json.value(true);
    stringIntegerArrayRule.validate(jsonValue);
  }

  @Test
  public void fixedEmptyArrayRuleShouldPassOnValidArray() {
    JsonArray jsonArray = new JsonArray();
    emptyArrayRule.validate(jsonArray);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fixedEmptyArrayRuleShouldFailOnInvalidArray() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("A");
    emptyArrayRule.validate(jsonArray);
  }

  @Test
  public void fixedStringArrayRuleShouldPassOnValidArray() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("A");
    stringArrayRule.validate(jsonArray);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fixedStringArrayRuleShouldFailOnInvalidArray() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(0);
    stringArrayRule.validate(jsonArray);
  }

  @Test
  public void fixedIntegerArrayRuleShouldPassOnValidArray() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(0);
    integerArrayRule.validate(jsonArray);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fixedIntegerArrayRuleShouldFailOnInvalidArray() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("A");
    integerArrayRule.validate(jsonArray);
  }

  @Test
  public void fixedStringIntegerArrayRuleShouldPassOnValidArray() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("A");
    jsonArray.add(0);
    stringIntegerArrayRule.validate(jsonArray);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fixedStringIntegerArrayRuleShouldFailOnInvalidArray() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(0);
    jsonArray.add("A");
    stringIntegerArrayRule.validate(jsonArray);
  }

}
