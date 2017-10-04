package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class ObjectJsonRule implements JsonRule {

  private final Map<String, JsonRule> rules;

  ObjectJsonRule(Map<String, JsonRule> rules) {
    this.rules = rules;
  }

  @Override
  public void validate(JsonValue value) {
    if (!value.isObject()) {
      throw new IllegalArgumentException(value + " is not an object.");
    }
    JsonObject object = value.asObject();
    Set<String> names = new HashSet<>(object.names());
    for (Entry<String, JsonRule> entry : rules.entrySet()) {
      entry.getValue().validate(object.get(entry.getKey()));
      names.remove(entry.getKey());
    }
    if (!names.isEmpty()) {
      throw new IllegalArgumentException(String.format("%s does not have a rule.", names.iterator().next()));
    }
  }

}
