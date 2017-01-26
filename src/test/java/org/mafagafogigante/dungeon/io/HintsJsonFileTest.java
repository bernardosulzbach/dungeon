package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HintsJsonFileTest extends ResourcesTypeTest {

  private static final String STRINGS_FIELD = "strings";
  private static final String HINTS_JSON_FILE_NAME = "hints.json";

  @Test
  public void testIsFileHasValidStructure() {
    final JsonRule hintsFileRuleObject = getHintsFileRuleObject();
    JsonObject hitsFileJsonObject = getJsonObjectByJsonFile(HINTS_JSON_FILE_NAME);
    hintsFileRuleObject.validate(hitsFileJsonObject);
  }

  private JsonRule getHintsFileRuleObject() {
    Map<String, JsonRule> hintsFileRules = new HashMap<>();
    final JsonRule stringJsonRule = JsonRuleFactory.makeStringRule();
    hintsFileRules.put(STRINGS_FIELD, JsonRuleFactory.makeVariableArrayRule(stringJsonRule));
    return JsonRuleFactory.makeObjectRule(hintsFileRules);
  }

}
