package org.mafagafogigante.dungeon.stats;

import org.mafagafogigante.dungeon.commands.IssuedCommand;
import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

/**
 * CommandStatistics class that is a component of Statistics.
 */
final class CommandStatistics implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private int commands;
  private int chars;
  private int words;

  /**
   * Adds an issued command to the statistics.
   *
   * @param issuedCommand the command to be added.
   */
  public void addCommand(IssuedCommand issuedCommand) {
    commands++;
    words += issuedCommand.getTokens().size();
    for (char c : issuedCommand.getStringRepresentation().toCharArray()) {
      if (!Character.isWhitespace(c)) {
        chars++;
      }
    }
  }

  /**
   * Returns how many commands the user has issued so far.
   *
   * @return the command count
   */
  public int getCommandCount() {
    return commands;
  }

  /**
   * Returns how many printable characters the user has entered so far.
   *
   * @return the character count
   */
  public int getChars() {
    return chars;
  }

  /**
   * Returns how many words the user has entered so far.
   *
   * @return the word count
   */
  public int getWords() {
    return words;
  }

}
