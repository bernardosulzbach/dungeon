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

package org.dungeon.items;

import org.dungeon.io.DLogger;
import org.dungeon.items.Item.Tag;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A set of Tags.
 * <p/>
 * Created by Bernardo on 22/05/2015.
 */
public class TagSet implements Serializable {

  private final Set<Tag> set = new HashSet<Tag>();

  /**
   * Checks if this TagSet has a specified Tag.
   *
   * @param tag the Tag object
   * @return true if this set contains the specified Tag
   */
  public boolean hasTag(Tag tag) {
    return set.contains(tag);
  }

  /**
   * Adds a Tag to this TagSet.
   */
  public void addTag(Tag tag) {
    if (!set.add(tag)) {
      DLogger.warning("Tried to add a Tag that was already in the TagSet!");
    }
  }

}
