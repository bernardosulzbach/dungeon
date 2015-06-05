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

import org.dungeon.game.Name;
import org.dungeon.util.Selectable;

/**
 * Article class that represents a wiki article.
 */
final class Article implements Selectable {

  private final Name title;
  private final String content;

  public Article(String title, String content) {
    this.title = Name.newInstance(title);
    this.content = content;
  }

  /**
   * Returns the title of this Article.
   */
  @Override
  public Name getName() {
    return title;
  }

  @Override
  public String toString() {
    return title + "\n\n" + content;
  }

}
