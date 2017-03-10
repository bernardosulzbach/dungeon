package org.mafagafogigante.dungeon.schema.rules;

import com.eclipsesource.json.JsonValue;

import java.util.HashSet;
import java.util.Set;

class EnumJsonRule<T extends Enum<T>> extends StringJsonRule {

  private final Set<String> enumValues = new HashSet<>();

  EnumJsonRule(Class<T> enumClass) {
    for (Enum<T> enumConstant : enumClass.getEnumConstants()) {
      enumValues.add(enumConstant.toString());
    }
  }

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    String valueAsString = value.asString();
    if (!enumValues.contains(valueAsString)) {
      throw new IllegalArgumentException(valueAsString + " is not a valid enum value.");
    }
  }

}
