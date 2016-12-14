package org.mafagafogigante.dungeon.schema.rules;

import com.eclipsesource.json.JsonValue;

public final class BoundIntegerJsonRule extends IntegerJsonRule {

  private final int minValue;
  private final int maxValue;

  public BoundIntegerJsonRule(int minValue, int maxValue) {
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    int intValue = value.asInt();
    if (intValue < minValue) {
      throw new IllegalArgumentException("value is below the allowed minimum.");
    }
    if (intValue > maxValue) {
      throw new IllegalArgumentException("value is above the allowed maximum.");
    }
  }

}
