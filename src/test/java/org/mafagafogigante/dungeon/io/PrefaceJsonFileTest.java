package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PrefaceJsonFileTest extends ResourcesTypeTest {

  private static final String FORMAT_FIELD = "format";
  private static final String PREFACE_JSON_FILE_NAME = "preface.json";

  @Test
  public void testIsFileHasValidStructure() {
    Map<String, JsonRule> tutorialFileRules = new HashMap<>();
    tutorialFileRules.put(FORMAT_FIELD, JsonRuleFactory.makeStringRule());
    final JsonRule tutorialJsonRule = JsonRuleFactory.makeObjectRule(tutorialFileRules);
    JsonObject prefaceFileJsonObject = getJsonObjectByJsonFile(PREFACE_JSON_FILE_NAME);
    tutorialJsonRule.validate(prefaceFileJsonObject);
  }

}
