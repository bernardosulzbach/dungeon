package org.mafagafogigante.dungeon.io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * This class provides getters for FilenameFilter implementations used by the application for input and output.
 */
final class DungeonFilenameFilters {

  private static final FilenameFilter extensionFilter = new FilenameFilter() {
    @Override
    public boolean accept(File dir, String name) {
      return name.endsWith(".dungeon");
    }
  };

  private DungeonFilenameFilters() {
    throw new AssertionError();
  }

  public static FilenameFilter getExtensionFilter() {
    return extensionFilter;
  }

}
