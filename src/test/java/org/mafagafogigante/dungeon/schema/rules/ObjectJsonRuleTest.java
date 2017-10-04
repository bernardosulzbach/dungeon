package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ObjectJsonRuleTest {

  @Test
  public void shouldPassObjectWithValidAttributes() {
    JsonObject object = new JsonObject();
    object.add("favoriteInteger", 10);
    object.add("count", 1000);
    object.add("optionalInteger", 10);
    Map<String, JsonRule> rules = new HashMap<>();
    rules.put("favoriteInteger", new IntegerJsonRule());
    rules.put("count", new BoundIntegerJsonRule(0, 1000));
    rules.put("optionalInteger", new OptionalJsonRule(new IntegerJsonRule()));
    JsonRule rule = new ObjectJsonRule(rules);
    rule.validate(object);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailOnNonObjectValue() {
    JsonValue value = Json.value(true);
    Map<String, JsonRule> rules = new HashMap<>();
    JsonRule rule = new ObjectJsonRule(rules);
    rule.validate(value);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailOnObjectWithInvalidAttributes() {
    JsonObject object = new JsonObject();
    object.add("id", "john");
    Map<String, JsonRule> rules = new HashMap<>();
    rules.put("id", new UppercaseStringJsonRule());
    JsonRule rule = new ObjectJsonRule(rules);
    rule.validate(object);
  }

}
