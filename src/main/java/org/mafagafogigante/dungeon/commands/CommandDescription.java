package org.mafagafogigante.dungeon.commands;

import org.mafagafogigante.dungeon.logging.DungeonLogger;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Simple wrapper for a name and info of a Command.
 */
public class CommandDescription {

  private final String name;
  private final String info;

  /**
   * Creates a CommandDescription with the provided name and info.
   *
   * @param name a String for name, not null, lowercase
   * @param info a String for info, nullable
   */
  CommandDescription(@NotNull String name, String info) {
    if (isNotLowercase(name)) {
      DungeonLogger.warning("Passed a String that was not lowercase as name for a CommandDescription.");
      name = name.toLowerCase(Locale.ENGLISH);
    }
    this.name = name;
    this.info = info;
  }

  /**
   * Checks if a String is not lowercase.
   *
   * @param string the String to be tested, not null
   * @return true if the String is not lowercase, false if it is lowercase
   */
  private static boolean isNotLowercase(String string) {
    for (char c : string.toCharArray()) {
      if (!Character.isLowerCase(c)) {
        return true;
      }
    }
    return false;
  }

  public String getName() {
    return name;
  }

  public String getInfo() {
    return info;
  }

  @Override
  public String toString() {
    return getName() + " : " + getInfo();
  }

}
