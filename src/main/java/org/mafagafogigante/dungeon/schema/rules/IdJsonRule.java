package org.mafagafogigante.dungeon.schema.rules;

import com.eclipsesource.json.JsonValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class IdJsonRule extends StringJsonRule {

  private static final Pattern pattern = Pattern.compile("^[A-Z_\\d]+$");

  @Override
  public void validate(JsonValue value) {
    super.validate(value);
    Matcher matcher = pattern.matcher(value.asString());
    if (!matcher.matches()) {
      throw new IllegalArgumentException(value + " is not a valid Dungeon id");
    }
  }

}
