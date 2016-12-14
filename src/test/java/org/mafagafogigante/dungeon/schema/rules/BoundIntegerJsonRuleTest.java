package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.JsonValidationResult;
import org.mafagafogigante.dungeon.schema.TypeOfJsonValidationResult;

import com.eclipsesource.json.Json;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BoundIntegerJsonRuleTest {

  private static final int LOWER_BOUND_VALUE = 3;
  private static final int UPPER_BOUND_VALUE = 6;

  private JsonRule boundIntegerJsonRule;

  @Before
  public void setUp() {
    boundIntegerJsonRule = new BoundIntegerJsonRule(LOWER_BOUND_VALUE, UPPER_BOUND_VALUE);
  }

  @Test
  public void testIfBoundIntegerJsonRuleReturnsValidResultTypeWhenJsonObjectIsInBound() {
    final int validationValue = 5;
    JsonValidationResult validationResult = boundIntegerJsonRule.validate(Json.value(validationValue));
    Assert.assertEquals(TypeOfJsonValidationResult.VALID, validationResult.getTypeOfJsonValidationResult());
    Assert.assertEquals(String.valueOf(validationValue), validationResult.getElementValue());
  }

  @Test
  public void testIfBoundIntegerJsonRuleReturnsOutOfBoundResultTypeWhenJsonObjectOutOfLowerBound() {
    final int validationValue = 2;
    JsonValidationResult validationResult = boundIntegerJsonRule.validate(Json.value(validationValue));
    Assert.assertEquals(TypeOfJsonValidationResult.ELEMENT_NOT_IN_BOUND,
        validationResult.getTypeOfJsonValidationResult());
  }

  @Test
  public void testIfBoundIntegerJsonRuleReturnsOutOfBoundResultTypeWhenJsonObjectOutOfUpperBound() {
    final int validationValue = 7;
    JsonValidationResult validationResult = boundIntegerJsonRule.validate(Json.value(validationValue));
    Assert.assertEquals(TypeOfJsonValidationResult.ELEMENT_NOT_IN_BOUND,
        validationResult.getTypeOfJsonValidationResult());
  }

  @Test
  public void testIfBoundIntegerJsonRuleReturnsValidResultTypeWhenJsonObjectEqualsUpperBound() {
    final int validationValue = 6;
    JsonValidationResult validationResult = boundIntegerJsonRule.validate(Json.value(validationValue));
    Assert.assertEquals(TypeOfJsonValidationResult.VALID, validationResult.getTypeOfJsonValidationResult());
  }

}
