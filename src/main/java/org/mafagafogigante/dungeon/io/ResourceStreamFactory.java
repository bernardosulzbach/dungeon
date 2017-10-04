package org.mafagafogigante.dungeon.io;

import java.io.InputStreamReader;

class ResourceStreamFactory {

  private static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();

  static InputStreamReader getInputStreamReader(String name) {
    return new InputStreamReader(CLASS_LOADER.getResourceAsStream(name), DungeonCharset.DEFAULT_CHARSET);
  }

}
