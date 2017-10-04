package org.mafagafogigante.dungeon.io;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/**
 * A Comparator function that compares the last modification times of Files. This comparator orders files from newest to
 * oldest, which is, in terms of last modified timestamps, from largest to smallest.
 *
 * <p>This is not consistent with equals.
 *
 * <p>Serializable is not implemented by design. This Comparator was devised to be used by Arrays.sort and nothing
 * else.
 */
class FileLastModifiedComparator implements Comparator<File>, Serializable {

  private static final long serialVersionUID = Version.MAJOR;

  @Override
  public int compare(File left, File right) {
    return Long.compare(right.lastModified(), left.lastModified());
  }

}
