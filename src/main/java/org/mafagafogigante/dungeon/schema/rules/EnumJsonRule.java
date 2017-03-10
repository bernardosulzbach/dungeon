package org.mafagafogigante.dungeon.schema.rules;

import com.eclipsesource.json.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class EnumJsonRule<T extends Enum<T>> extends StringJsonRule {

  private final List<Enum<T>> enumValues = new ArrayList<>();

  EnumJsonRule(Class<T> enumClass) {
    Collections.addAll(enumValues, enumClass.getEnumConstants());
  }

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    String valueAsString = value.asString();
    if (!isValueExist(valueAsString)) {
      throw new IllegalArgumentException(valueAsString + " is not exist in next enum values: " + enumValues + ".");
    }
  }

  private boolean isValueExist(String value) {
    for (Enum<T> enumValue : enumValues) {
      if (StringUtils.equals(enumValue.toString(), value)) {
        return true;
      }
    }
    return false;
  }

}
