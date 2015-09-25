/*
 * Copyright (C) 2015 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dungeon.wiki;

import org.dungeon.game.DungeonStringBuilder;
import org.dungeon.io.Writer;
import org.dungeon.util.CounterMap;
import org.dungeon.util.Matches;
import org.dungeon.util.Utils;

import org.apache.commons.lang3.StringUtils;

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
      Matches<Article> matches = Utils.findBestMatches(Wiki.getArticles(), arguments);
      if (matches.size() == 0) {
        deepSearch(arguments);
      } else if (matches.size() == 1) {
        Writer.write(matches.getMatch(0).toString());
      } else {
        DungeonStringBuilder builder = new DungeonStringBuilder();
        builder.append("The following article titles match your query:\n");
        for (int i = 0; i < matches.size(); i++) {
          builder.append(toArticleListingEntry(matches.getMatch(i)));
          builder.append("\n");
        }
        builder.append("Be more specific.");
        Writer.write(builder);
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
    CounterMap<Article> counter = new CounterMap<Article>();
    for (Article article : Wiki.getArticles()) {
      int matches = 0;
      for (String argument : arguments) {
        matches += StringUtils.countMatches(article.getContent().toLowerCase(), argument.toLowerCase());
      }
      if (matches != 0) {
        counter.incrementCounter(article, matches);
      }
    }
    DungeonStringBuilder builder = new DungeonStringBuilder();
    if (counter.isNotEmpty()) {
      builder.append("The following articles contain text that matches your query:\n");
      for (Article article : counter) {
        String matchCount = counter.getCounter(article) + (counter.getCounter(article) > 1 ? " matches" : " match");
        builder.append(toArticleListingEntry(article) + " (" + matchCount + ")\n");
      }
    } else {
      builder.append("No article matches your query.");
    }
    Writer.write(builder);
  }

  /**
   * Writes the article count and a list with the titles of the {@code Articles} in the {@code articleList}.
   */
  private static void writeArticleList() {
    DungeonStringBuilder builder = new DungeonStringBuilder();
    builder.append("The wiki has the following ");
    builder.append(String.valueOf(Wiki.getArticles().size()));
    builder.append(" articles:\n");
    for (Article article : Wiki.getArticles()) {
      builder.append(toArticleListingEntry(article));
      builder.append("\n");
    }
    Writer.write(builder);
  }

  private static String toArticleListingEntry(Article article) {
    return "  " + article.getName();
  }

}
