package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonRule;
import org.mafagafogigante.dungeon.schema.rules.JsonRuleFactory;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class WikiJsonFileTest extends ResourcesTypeTest {

  private static final JsonObject wikiJson = getJsonObjectByJsonFile(JsonFileEnum.WIKI);
  private static final String ARTICLES_FIELD = "articles";
  private static final String TITLE_FIELD = "title";
  private static final String CONTENT_FIELD = "content";
  private static final String SEE_ALSO_FIELD = "seeAlso";

  @Test
  public void testIsFileHasValidStructure() {
    JsonRule seeAlsoOptionalRule =
        JsonRuleFactory.makeOptionalRule(JsonRuleFactory.makeVariableArrayRule(JsonRuleFactory.makeStringRule()));
    JsonRule articleRuleObject = getArticleRuleObject(seeAlsoOptionalRule);
    JsonRule wikiFileRuleObject = getWikiFileRuleObject(articleRuleObject);
    wikiFileRuleObject.validate(wikiJson);
  }

  private JsonRule getWikiFileRuleObject(JsonRule articleRuleObject) {
    Map<String, JsonRule> wikiFileRules = new HashMap<>();
    wikiFileRules.put(ARTICLES_FIELD, JsonRuleFactory.makeVariableArrayRule(articleRuleObject));
    return JsonRuleFactory.makeObjectRule(wikiFileRules);
  }

  private JsonRule getArticleRuleObject(JsonRule seeAlsoJsonRule) {
    Map<String, JsonRule> articleRules = new HashMap<>();
    articleRules.put(TITLE_FIELD, JsonRuleFactory.makeStringRule());
    articleRules.put(CONTENT_FIELD, JsonRuleFactory.makeStringRule());
    articleRules.put(SEE_ALSO_FIELD, seeAlsoJsonRule);
    return JsonRuleFactory.makeObjectRule(articleRules);
  }

}
