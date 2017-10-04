package org.mafagafogigante.dungeon.io;

import org.jetbrains.annotations.NotNull;

public class ResourceNameResolver {

  private ResourceNameResolver() {
  }

  public static String resolveName(@NotNull DungeonResource resource) {
    return resource.getFilename();
  }

}
