package org.mafagafogigante.dungeon.util;

import org.jetbrains.annotations.NotNull;

/**
 * RichText semantically is a sequence of RichStrings.
 */
public interface RichText extends Writable {

  @NotNull
  String toJavaString();

}
