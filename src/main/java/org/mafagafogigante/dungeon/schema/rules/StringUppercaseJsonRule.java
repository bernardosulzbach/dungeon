package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.JsonValidationResult;
import org.mafagafogigante.dungeon.schema.TypeOfJsonValidationResult;

import com.eclipsesource.json.JsonValue;

import java.util.Locale;

public final class StringUppercaseJsonRule implements JsonRule {

  @Override
  public JsonValidationResult validate(JsonValue validationElement) {
    String stringValue = validationElement.asString();
    if (isAllCharsInUppercase(stringValue)) {
      return new JsonValidationResult(stringValue, TypeOfJsonValidationResult.VALID);
    } else {
      return new JsonValidationResult(stringValue, TypeOfJsonValidationResult.ELEMENT_NOT_IN_UPPERCASE);
    }
  }

  private boolean isAllCharsInUppercase(String value) {
    return value.equals(value.toUpperCase(Locale.ENGLISH));
  }

}
