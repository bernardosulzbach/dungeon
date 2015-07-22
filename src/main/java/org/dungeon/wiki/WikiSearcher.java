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

import org.dungeon.commands.IssuedCommand;
import org.dungeon.io.Writer;
import org.dungeon.util.Matches;
import org.dungeon.util.Utils;

/**
 * Uninstantiable WikiSearcher class used to retrieve articles from the Wiki.
 */
public final class WikiSearcher {

  private WikiSearcher() {
    throw new AssertionError();
  }

  /**
   * Searches the wiki and prints the matching contents to the screen. This method triggers the wiki initialization.
   *
   * @param issuedCommand an IssuedCommand object
   */
  public static void search(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      Matches<Article> matches = Utils.findBestMatches(Wiki.getArticles(), issuedCommand.getArguments());
      if (matches.size() == 0) {
        Writer.writeString("No matches were found.");
      } else if (matches.size() == 1) {
        Writer.writeString(matches.getMatch(0).toString());
      } else {
        StringBuilder builder = new StringBuilder();
        builder.append("The following articles match your query:\n");
        for (int i = 0; i < matches.size(); i++) {
          builder.append(toArticleListingEntry(matches.getMatch(i))).append("\n");
        }
        builder.append("Be more specific.");
        Writer.writeString(builder.toString());
      }
    } else {
      writeArticleList();
    }
  }

  /**
   * Writes the article count and a list with the titles of the {@code Articles} in the {@code articleList}.
   */
  private static void writeArticleList() {
    StringBuilder builder = new StringBuilder();
    builder.append("The wiki has the following ").append(Wiki.getArticles().size()).append(" articles:\n");
    for (Article article : Wiki.getArticles()) {
      builder.append(toArticleListingEntry(article)).append("\n");
    }
    Writer.writeString(builder.toString());
  }

  private static String toArticleListingEntry(Article article) {
    return "  " + article.getName();
  }

}
