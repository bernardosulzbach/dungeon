package org.mafagafogigante.dungeon.schema.rules;

import com.eclipsesource.json.JsonValue;

import java.util.Locale;

final class UppercaseStringJsonRule extends StringJsonRule {

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    if (!isAllCharsInUppercase(value.asString())) {
      throw new IllegalArgumentException(value + " is not uppercase.");
    }
  }

  private boolean isAllCharsInUppercase(String value) {
    return value.equals(value.toUpperCase(Locale.ENGLISH));
  }

}
