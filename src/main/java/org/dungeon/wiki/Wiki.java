/*
 * Copyright (C) 2014 Bernardo Sulzbach
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

import org.dungeon.game.IssuedCommand;
import org.dungeon.io.IO;
import org.dungeon.io.ResourceReader;
import org.dungeon.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * The Wiki class. Loads the contents of the wiki.txt file and manages wiki articles.
 * <p/>
 * Created by Bernardo on 06/02/2015.
 */
public abstract class Wiki {

  private static List<Article> articleList;

  private Wiki() {
  }

  private static void initialize() {
    articleList = new ArrayList<Article>();
    ResourceReader reader = new ResourceReader("wiki.txt");
    while (reader.readNextElement()) {
      articleList.add(new Article(reader.getValue("ARTICLE"), reader.getValue("CONTENT")));
    }
  }

  /**
   * Searches the wiki and prints the matching contents to the screen.
   *
   * @param issuedCommand an IssuedCommand
   */
  public static void search(IssuedCommand issuedCommand) {
    if (articleList == null) {
      initialize();
    }
    if (issuedCommand.hasArguments()) {
      List<Article> matches = findMatches(issuedCommand.getArguments());
      if (matches.isEmpty()) {
        IO.writeString("No matches were found.");
      } else if (matches.size() == 1) {
        IO.writeString(matches.get(0).toString());
      } else {
        StringBuilder builder = new StringBuilder();
        builder.append("The following articles match your query:\n");
        for (Article match : matches) {
          builder.append(toArticleListingEntry(match)).append("\n");
        }
        builder.append("Be more specific.");
        IO.writeString(builder.toString());
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
    builder.append("The wiki has the following ").append(articleList.size()).append(" articles:\n");
    for (Article article : articleList) {
      builder.append(toArticleListingEntry(article)).append("\n");
    }
    IO.writeString(builder.toString());
  }

  private static String toArticleListingEntry(Article article) {
    return "  " + article.title;
  }

  private static List<Article> findMatches(String[] searchArguments) {
    List<Article> matches = new ArrayList<Article>();
    for (Article article : articleList) {
      if (Utils.checkQueryMatch(searchArguments, Utils.split(article.title))) {
        matches.add(article);
      }
    }
    return matches;
  }

}
