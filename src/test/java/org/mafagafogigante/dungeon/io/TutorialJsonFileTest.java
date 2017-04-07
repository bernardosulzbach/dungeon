package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TutorialJsonFileTest {

  private static final String TUTORIAL_FIELD = "tutorial";

  @Test
  public void testIsFileHasValidStructure() {
    Map<String, JsonRule> tutorialFileRules = new HashMap<>();
    tutorialFileRules.put(TUTORIAL_FIELD, JsonRuleFactory.makeStringRule());
    final JsonRule tutorialJsonRule = JsonRuleFactory.makeObjectRule(tutorialFileRules);
    JsonObject tutorialFileJsonObject =
        JsonObjectFactory.makeJsonObject(JsonFileName.TUTORIAL.getStringRepresentation());
    tutorialJsonRule.validate(tutorialFileJsonObject);
  }

}
