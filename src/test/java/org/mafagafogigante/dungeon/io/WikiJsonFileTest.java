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
  private static final String WIKI_JSON_FILE_NAME = "wiki.json";

  @Test
  public void testIsFileHasValidStructure() {
    final JsonRule seeAlsoOptionalRule =
        JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeVariableArrayRule(JsonRuleFactory.makeStringRule()));
    final JsonRule articleRuleObject = getArticleRuleObject(seeAlsoOptionalRule);
    final JsonRule wikiFileRuleObject = getWikiFileRuleObject(articleRuleObject);
    JsonObject wikiFileJsonObject = getJsonObjectByJsonFile(WIKI_JSON_FILE_NAME);
    wikiFileRuleObject.validate(wikiFileJsonObject);
  }

  private JsonRule getWikiFileRuleObject(JsonRule articleRuleObject) {
    Map<String, JsonRule> wikiFileRules = new HashMap<>();
    wikiFileRules.put(ARTICLES_FIELD, JsonRuleFactory.makeVariableArrayRule(articleRuleObject));
    return JsonRuleFactory.makeObjectRule(wikiFileRules);
  }

  private JsonRule getArticleRuleObject(JsonRule seeAlsoJsonRule) {
    Map<String, JsonRule> articleRules = new HashMap<>();
    final JsonRule stringRule = JsonRuleFactory.makeStringRule();
    articleRules.put(TITLE_FIELD, stringRule);
    articleRules.put(CONTENT_FIELD, stringRule);
    articleRules.put(SEE_ALSO_FIELD, seeAlsoJsonRule);
    return JsonRuleFactory.makeObjectRule(articleRules);
  }

}
