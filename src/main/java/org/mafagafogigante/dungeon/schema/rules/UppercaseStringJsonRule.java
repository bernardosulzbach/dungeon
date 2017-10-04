package org.mafagafogigante.dungeon.schema.rules;

import com.eclipsesource.json.JsonValue;
import org.apache.commons.lang3.StringUtils;

final class UppercaseStringJsonRule extends StringJsonRule {

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    if (!StringUtils.isAllUpperCase(value.asString())) {
      throw new IllegalArgumentException(value + " is not uppercase.");
    }
  }

}
