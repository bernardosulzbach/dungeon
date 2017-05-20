package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PoemsJsonFileTest extends ResourcesTypeTest {

  private static final String POEMS_FIELD = "poems";
  private static final String TITLE_FIELD = "title";
  private static final String AUTHOR_FIELD = "author";
  private static final String CONTENT_FIELD = "content";

  @Test
  public void testIsFileHasValidStructure() {
    JsonRule poemsRuleObject = getPoemsRuleObject();
    JsonRule poemsFileJsonRule = getPoemsFileRuleObject(poemsRuleObject);
    String filename = ResourceNameResolver.resolveName(DungeonResource.POEMS);
    JsonObject poemsFileJsonObject = getJsonObjectByJsonFilename(filename);
    poemsFileJsonRule.validate(poemsFileJsonObject);
  }

  private JsonRule getPoemsFileRuleObject(JsonRule poemsRuleObject) {
    Map<String, JsonRule> poemsFileRules = new HashMap<>();
    poemsFileRules.put(POEMS_FIELD, JsonRuleFactory.makeVariableArrayRule(poemsRuleObject));
    return JsonRuleFactory.makeObjectRule(poemsFileRules);
  }

  private JsonRule getPoemsRuleObject() {
    Map<String, JsonRule> poemsRules = new HashMap<>();
    JsonRule stringRule = JsonRuleFactory.makeStringRule();
    poemsRules.put(TITLE_FIELD, stringRule);
    poemsRules.put(AUTHOR_FIELD, stringRule);
    poemsRules.put(CONTENT_FIELD, stringRule);
    return JsonRuleFactory.makeObjectRule(poemsRules);
  }

}
