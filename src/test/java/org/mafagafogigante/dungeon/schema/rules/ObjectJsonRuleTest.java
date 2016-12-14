package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ObjectJsonRuleTest {

  @Test
  public void shouldWorkForSimpleObjects() throws Exception {
    Map<String, JsonRule> rules = new HashMap<>();
    rules.put("favoriteInteger", JsonRuleFactory.makeIntegerRule());
    rules.put("count", JsonRuleFactory.makeBoundIntegerRule(0, 1000));
    rules.put("optionalInteger", new OptionalJsonRule(new IntegerJsonRule()));
    JsonObject object = new JsonObject();
    object.add("favoriteInteger", 10);
    object.add("count", 1000);
    object.add("optionalInteger", 10);
    JsonRule rule = JsonRuleFactory.makeObjectRule(rules);
    rule.validate(object);
  }

}