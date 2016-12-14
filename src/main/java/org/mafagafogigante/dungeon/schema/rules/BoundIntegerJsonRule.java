package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.JsonValidationResult;
import org.mafagafogigante.dungeon.schema.TypeOfJsonValidationResult;

import com.eclipsesource.json.JsonValue;

public final class BoundIntegerJsonRule implements JsonRule {

  private final int minValue;
  private final int maxValue;

  public BoundIntegerJsonRule(int minValue, int maxValue) {
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  @Override
  public JsonValidationResult validate(JsonValue validationElement) {
    int intValue = validationElement.asInt();
    if (intValue >= minValue && intValue <= maxValue) {
      return new JsonValidationResult(Integer.toString(intValue), TypeOfJsonValidationResult.VALID);
    } else {
      return new JsonValidationResult(Integer.toString(intValue), TypeOfJsonValidationResult.ELEMENT_NOT_IN_BOUND);
    }
  }

}
