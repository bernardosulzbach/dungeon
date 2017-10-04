package org.mafagafogigante.dungeon.wiki;

import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.util.CounterMap;
import org.mafagafogigante.dungeon.util.Matches;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * Uninstantiable WikiSearcher class used to retrieve articles from the Wiki.
 */
public final class WikiSearcher {

  private WikiSearcher() {
    throw new AssertionError();
  }

  /**
   * Searches the wiki and writes the matching contents to the screen. This method triggers the wiki initialization.
   *
   * @param arguments an array of arguments that will determine the search
   */
  public static void search(String[] arguments) {
    if (arguments.length != 0) {
      Matches<Article> matches = Matches.findBestMatches(Wiki.getArticles(), arguments);
      if (matches.size() == 0) {
        deepSearch(arguments);
      } else if (matches.size() == 1) {
        Writer.write(matches.getMatch(0).toString());
      } else {
        DungeonString string = new DungeonString();
        string.append("The following article titles match your query:\n");
        for (int i = 0; i < matches.size(); i++) {
          string.append(toArticleListingEntry(matches.getMatch(i)));
          string.append("\n");
        }
        string.append("Be more specific.");
        Writer.write(string);
      }
    } else {
      writeArticleList();
    }
  }

  /**
   * Searches the wiki by looking at the content of the articles.
   *
   * @param arguments an array of arguments that will determine the search
   */
  private static void deepSearch(String[] arguments) {
    CounterMap<Article> counter = new CounterMap<>();
    for (Article article : Wiki.getArticles()) {
      int matches = 0;
      for (String argument : arguments) {
        final String lowerCaseArticleContent = article.getContent().toLowerCase(Locale.ENGLISH);
        final String lowerCaseArgument = argument.toLowerCase(Locale.ENGLISH);
        matches += StringUtils.countMatches(lowerCaseArticleContent, lowerCaseArgument);
      }
      if (matches != 0) {
        counter.incrementCounter(article, matches);
      }
    }
    DungeonString string = new DungeonString();
    if (counter.isNotEmpty()) {
      string.append("The following articles contain text that matches your query:\n");
      for (Article article : counter) {
        String matchCount = counter.getCounter(article) + (counter.getCounter(article) > 1 ? " matches" : " match");
        string.append(toArticleListingEntry(article) + " (" + matchCount + ")\n");
      }
    } else {
      string.append("No article matches your query.");
    }
    Writer.write(string);
  }

  /**
   * Writes the article count and a list with the titles of the {@code Articles} in the {@code articleList}.
   */
  private static void writeArticleList() {
    DungeonString string = new DungeonString();
    string.append("The wiki has the following ");
    string.append(String.valueOf(Wiki.getArticles().size()));
    string.append(" articles:\n");
    for (Article article : Wiki.getArticles()) {
      string.append(toArticleListingEntry(article));
      string.append("\n");
    }
    Writer.write(string);
  }

  private static String toArticleListingEntry(Article article) {
    return "  " + article.getName();
  }

}
