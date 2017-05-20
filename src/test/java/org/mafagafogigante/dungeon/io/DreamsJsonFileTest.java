package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DreamsJsonFileTest extends ResourcesTypeTest {

  private static final String STRINGS_FIELD = "strings";

  @Test
  public void testIsFileHasValidStructure() {
    JsonRule dreamsFileRuleObject = getDreamsFileRuleObject();
    String filename = ResourceNameResolver.resolveName(DungeonResource.DREAMS);
    JsonObject dreamsFileJsonObject = getJsonObjectByJsonFilename(filename);
    dreamsFileRuleObject.validate(dreamsFileJsonObject);
  }

  private JsonRule getDreamsFileRuleObject() {
    Map<String, JsonRule> dreamsFileRules = new HashMap<>();
    JsonRule stringJsonRule = JsonRuleFactory.makeStringRule();
    dreamsFileRules.put(STRINGS_FIELD, JsonRuleFactory.makeVariableArrayRule(stringJsonRule));
    return JsonRuleFactory.makeObjectRule(dreamsFileRules);
  }

}
