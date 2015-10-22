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

package org.mafagafogigante.dungeon.wiki;

import org.mafagafogigante.dungeon.game.Name;
import org.mafagafogigante.dungeon.game.NameFactory;
import org.mafagafogigante.dungeon.util.Selectable;
import org.mafagafogigante.dungeon.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.TreeSet;

/**
 * Article class that represents a wiki article.
 */
final class Article implements Selectable {

  private final Name title;
  private final String content;
  private final Set<String> seeAlso = new TreeSet<String>();

  public Article(@NotNull String title, @NotNull String content) {
    this.title = NameFactory.newInstance(title);
    this.content = content;
  }

  boolean hasReference(String reference) {
    return seeAlso.contains(reference);
  }

  /**
   * Adds a reference to the See Also section of this article.
   *
   * @param reference the reference, must not have already been added
   */
  void addReference(@NotNull final String reference) {
    if (hasReference(reference)) {
      throw new IllegalStateException("article " + getName() + " already has a reference to " + reference);
    }
    seeAlso.add(reference);
  }

  /**
   * Returns the title of this Article.
   */
  @Override
  public Name getName() {
    return title;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(title);
    stringBuilder.append("\n\n");
    stringBuilder.append(content);
    if (!seeAlso.isEmpty()) {
      stringBuilder.append("\n\n");
      stringBuilder.append("See also: ");
      stringBuilder.append(Utils.enumerate(seeAlso));
      stringBuilder.append(";");
    }
    return stringBuilder.toString();
  }

}
