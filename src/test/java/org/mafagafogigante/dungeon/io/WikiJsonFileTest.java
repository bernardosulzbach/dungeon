package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class WikiJsonFileTest extends ResourcesTypeTest {

  private static final String TITLE_FIELD = "title";
  private static final String CONTENT_FIELD = "content";
  private static final String SEE_ALSO_FIELD = "seeAlso";
  private static final String ARTICLES_FIELD = "articles";

  @Test
  public void testIsFileHasValidStructure() {
    JsonRule variableStringArrayRule = JsonRuleFactory.makeVariableArrayRule(JsonRuleFactory.makeStringRule());
    JsonRule seeAlsoOptionalRule = JsonRuleFactory.makeOptionalRule(variableStringArrayRule);
    JsonRule articleRuleObject = getArticleRuleObject(seeAlsoOptionalRule);
    JsonRule wikiFileRuleObject = getWikiFileRuleObject(articleRuleObject);
    String wikiFilename = ResourceNameResolver.resolveName(DungeonResource.WIKI);
    JsonObject wikiFileJsonObject = getJsonObjectByJsonFilename(wikiFilename);
    wikiFileRuleObject.validate(wikiFileJsonObject);
  }

  private JsonRule getWikiFileRuleObject(JsonRule articleRuleObject) {
    Map<String, JsonRule> wikiFileRules = new HashMap<>();
    wikiFileRules.put(ARTICLES_FIELD, JsonRuleFactory.makeVariableArrayRule(articleRuleObject));
    return JsonRuleFactory.makeObjectRule(wikiFileRules);
  }

  private JsonRule getArticleRuleObject(JsonRule seeAlsoJsonRule) {
    Map<String, JsonRule> articleRules = new HashMap<>();
    JsonRule stringRule = JsonRuleFactory.makeStringRule();
    articleRules.put(TITLE_FIELD, stringRule);
    articleRules.put(CONTENT_FIELD, stringRule);
    articleRules.put(SEE_ALSO_FIELD, seeAlsoJsonRule);
    return JsonRuleFactory.makeObjectRule(articleRules);
  }

}
