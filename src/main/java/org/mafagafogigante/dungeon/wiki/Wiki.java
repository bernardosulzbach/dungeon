package org.mafagafogigante.dungeon.wiki;

import org.mafagafogigante.dungeon.io.DungeonResource;
import org.mafagafogigante.dungeon.io.JsonObjectFactory;
import org.mafagafogigante.dungeon.io.ResourceNameResolver;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Uninstantiable Wiki class that loads the Wiki when it is required.
 */
final class Wiki {

  private static List<Article> articleList;

  private Wiki() {
    throw new AssertionError();
  }

  /**
   * Initializes the Wiki. Build a map with all the See Also references until all the articles have been loaded, so it
   * is possible to check that these references are valid.
   */
  private static void initialize() {
    // The field cannot be initialized in the field declaration as a comparison to null is used to determine whether or
    // not it has already been initialized.
    articleList = new ArrayList<>();
    Map<Article, Collection<String>> seeAlsoMap = new HashMap<>();
    String filename = ResourceNameResolver.resolveName(DungeonResource.WIKI);
    JsonObject wikiJsonObject = JsonObjectFactory.makeJsonObject(filename);
    for (JsonValue jsonValue : wikiJsonObject.get("articles").asArray()) {
      JsonObject jsonObject = jsonValue.asObject();
      Article article = new Article(jsonObject.get("title").asString(), jsonObject.get("content").asString());
      if (jsonObject.get("seeAlso") != null) {
        seeAlsoMap.put(article, new ArrayList<String>());
        for (JsonValue referenceJsonValue : jsonObject.get("seeAlso").asArray()) {
          seeAlsoMap.get(article).add(referenceJsonValue.asString());
        }
      }
      articleList.add(article);
    }
    // Validate the references and add them.
    addReferences(seeAlsoMap);
  }

  private static void addReferences(Map<Article, Collection<String>> seeAlsoMap) {
    // To speed up, make a set with all the valid article names.
    Set<String> validReferences = new HashSet<>();
    for (Article article : articleList) {
      validReferences.add(article.getName().getSingular());
    }
    for (Entry<Article, Collection<String>> entry : seeAlsoMap.entrySet()) {
      for (String reference : entry.getValue()) {
        if (validReferences.contains(reference)) {
          String articleName = entry.getKey().getName().getSingular();
          if (entry.getKey().hasReference(reference)) {
            DungeonLogger.warning(String.format("Found repeated reference (%s) in %s.", reference, articleName));
          } else if (articleName.equals(reference)) {
            DungeonLogger.warning(String.format("Found reference to self in %s.", articleName));
          } else {
            entry.getKey().addReference(reference);
          }
        } else {
          DungeonLogger.warning("Got invalid wiki reference: " + reference + ".");
        }
      }
    }
  }

  /**
   * Returns an unmodifiable view of the collection of articles.
   */
  static Collection<Article> getArticles() {
    if (articleList == null) {
      initialize();
    }
    return Collections.unmodifiableCollection(articleList);
  }

  @Override
  public String toString() {
    return "Wiki{articleList=" + articleList + '}';
  }

}
