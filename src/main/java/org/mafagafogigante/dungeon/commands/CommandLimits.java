package org.mafagafogigante.dungeon.commands;

import org.jetbrains.annotations.NotNull;

class CommandLimits {

  /**
   * An arbitrary maximum command length.
   *
   * <p>This value is used for input validation and to prevent that slower and memory-consuming methods ever get feed
   * too much input.
   */
  private static final int MAXIMUM_COMMAND_LENGTH = 2048;

  static boolean isWithinMaximumCommandLength(@NotNull final String candidate) {
    return candidate.length() <= MAXIMUM_COMMAND_LENGTH;
  }

  static boolean isValidSource(@NotNull final String source) {
    return isWithinMaximumCommandLength(source);
  }

}
