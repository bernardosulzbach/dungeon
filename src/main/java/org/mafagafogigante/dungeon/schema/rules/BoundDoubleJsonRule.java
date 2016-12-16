package org.mafagafogigante.dungeon.schema.rules;

import com.eclipsesource.json.JsonValue;

final class BoundDoubleJsonRule extends DoubleJsonRule {

  private final double minValue;
  private final double maxValue;

  BoundDoubleJsonRule(double minValue, double maxValue) {
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    double doubleValue = value.asDouble();
    if (doubleValue < minValue) {
      throw new IllegalArgumentException(value + " is below the allowed minimum " + minValue + ".");
    }
    if (doubleValue > maxValue) {
      throw new IllegalArgumentException(value + " is above the allowed maximum " + maxValue + ".");
    }
  }

}
