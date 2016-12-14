package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.JsonValidationResult;
import org.mafagafogigante.dungeon.schema.TypeOfJsonValidationResult;

import com.eclipsesource.json.Json;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringUppercaseJsonRuleTest {

  private JsonRule stringUppercaseJsonRule;

  @Before
  public void setUp() {
    stringUppercaseJsonRule = new StringUppercaseJsonRule();
  }

  @Test
  public void testIfStringUppercaseJsonRuleReturnsValidResultTypeWhenJsonValueCharsAreInUppercase() {
    final String jsonStringInUppercase = "ABCDF";
    JsonValidationResult validationResult = stringUppercaseJsonRule.validate(Json.value(jsonStringInUppercase));
    Assert.assertEquals(TypeOfJsonValidationResult.VALID, validationResult.getTypeOfJsonValidationResult());
    Assert.assertEquals(jsonStringInUppercase, validationResult.getElementValue());
  }

  @Test
  public void testIfStringUppercaseJsonRuleReturnsElementNotInUppercaseResultTypeWhenJsonValueCharsAreInLowercase() {
    final String jsonStringInLowercase = "abcdf";
    JsonValidationResult validationResult = stringUppercaseJsonRule.validate(Json.value(jsonStringInLowercase));
    Assert.assertEquals(TypeOfJsonValidationResult.ELEMENT_NOT_IN_UPPERCASE,
        validationResult.getTypeOfJsonValidationResult());
  }

  @Test
  public void testIfStringUppercaseJsonRuleReturnsElementNotInUppercaseResultTypeWhenOneJsonValueCharIsInLowercase() {
    final String jsonStringInLowercase = "ABsDF";
    JsonValidationResult validationResult = stringUppercaseJsonRule.validate(Json.value(jsonStringInLowercase));
    Assert.assertEquals(TypeOfJsonValidationResult.ELEMENT_NOT_IN_UPPERCASE,
        validationResult.getTypeOfJsonValidationResult());
  }

}
