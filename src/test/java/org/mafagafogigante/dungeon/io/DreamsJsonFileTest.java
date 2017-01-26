package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DreamsJsonFileTest extends ResourcesTypeTest {

  private static final String STRINGS_FIELD = "strings";
  private static final String DREAMS_JSON_FILE_NAME = "dreams.json";

  @Test
  public void testIsFileHasValidStructure() {
    final JsonRule dreamsFileRuleObject = getDreamsFileRuleObject();
    JsonObject dreamsFileJsonObject = getJsonObjectByJsonFile(DREAMS_JSON_FILE_NAME);
    dreamsFileRuleObject.validate(dreamsFileJsonObject);
  }

  private JsonRule getDreamsFileRuleObject() {
    Map<String, JsonRule> dreamsFileRules = new HashMap<>();
    final JsonRule stringJsonRule = JsonRuleFactory.makeStringRule();
    dreamsFileRules.put(STRINGS_FIELD, JsonRuleFactory.makeVariableArrayRule(stringJsonRule));
    return JsonRuleFactory.makeObjectRule(dreamsFileRules);
  }

}
