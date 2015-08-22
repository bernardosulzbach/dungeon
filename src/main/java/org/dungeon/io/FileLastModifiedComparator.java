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

package org.dungeon.io;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/**
 * A Comparator function that compares the last modification times of Files. This comparator orders files from newest to
 * oldest, which is, in terms of last modified timestamps, from largest to smallest.
 *
 * This is not consistent with equals.
 *
 * Serializable is not implemented by design. This Comparator was devised to be used by Arrays.sort and nothing else.
 */
class FileLastModifiedComparator implements Comparator<File>, Serializable {

  @Override
  public int compare(File a, File b) {
    return Long.valueOf(b.lastModified()).compareTo(a.lastModified());
  }

}
