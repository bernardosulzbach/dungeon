package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.schema.JsonRule;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class JsonRuleFactory {

  private JsonRuleFactory() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  public static JsonRule makeEmptyRule() {
    return new EmptyRule();
  }

  public static JsonRule makeObjectRule(Map<String, JsonRule> rules) {
    return new ObjectJsonRule(rules);
  }

  public static JsonRule makeStringRule() {
    return new StringJsonRule();
  }

  public static JsonRule makeIntegerRule() {
    return new IntegerJsonRule();
  }

  public static JsonRule makeBooleanRule() {
    return new BooleanJsonRule();
  }

  public static JsonRule makeStringLengthRule(int stringLength) {
    return new StringLengthJsonRule(stringLength);
  }

  public static JsonRule makeFixedArrayRule(List<JsonRule> rules) {
    return new FixedArrayJsonRule(rules);
  }

  public static JsonRule makeVariableArrayRule(JsonRule rule) {
    return new VariableArrayJsonRule(rule);
  }

  public static JsonRule makePercentRule() {
    return new PercentageJsonRule();
  }

  public static JsonRule makeBoundIntegerRule(int minimumValue, int maximumValue) {
    return new BoundIntegerJsonRule(minimumValue, maximumValue);
  }

  public static JsonRule makeBoundDoubleRule(double minimumValue, double maximumValue) {
    return new BoundDoubleJsonRule(minimumValue, maximumValue);
  }

  public static JsonRule makeArraySizeRule(int arraySize) {
    return new ArraySizeJsonRule(arraySize);
  }

  public static JsonRule makeUppercaseStringRule() {
    return new UppercaseStringJsonRule();
  }

  public static JsonRule makeIdRule() {
    return new IdJsonRule();
  }

  public static JsonRule makeIdSetRule(Collection<Id> ids) {
    return new IdSetJsonRule(ids);
  }

  public static JsonRule makeGroupRule(JsonRule... jsonRules) {
    return new GroupJsonRule(jsonRules);
  }

  public static JsonRule makeOptionalRule(JsonRule jsonRule) {
    return new OptionalJsonRule(jsonRule);
  }

  public static JsonRule makePeriodRule() {
    return new PeriodJsonRule();
  }

  public static <T extends Enum<T>> JsonRule makeEnumJsonRule(Class<T> enumClass) {
    return new EnumJsonRule<>(enumClass);
  }

}
