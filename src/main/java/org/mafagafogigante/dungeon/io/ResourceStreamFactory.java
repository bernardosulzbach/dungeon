package org.mafagafogigante.dungeon.io;

import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class ResourceStreamFactory {

  private static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();
  private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

  static InputStreamReader getInputStreamReader(String name) {
    return new InputStreamReader(CLASS_LOADER.getResourceAsStream(name), DEFAULT_CHARSET);
  }

}
