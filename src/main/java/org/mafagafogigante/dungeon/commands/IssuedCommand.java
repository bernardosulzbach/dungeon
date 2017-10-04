package org.mafagafogigante.dungeon.commands;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * IssuedCommand class that processes a command entered by the player and provides useful query methods.
 *
 * <p>IssuedCommands are case-unsensitive and granted to have at least one word (the command).
 */
public final class IssuedCommand {

  private final String stringRepresentation;
  private final List<String> tokens; // An unmodifiable list.

  /**
   * Creates a new IssuedCommand from a string.
   *
   * @param source a string with at least one character that is not whitespace.
   */
  public IssuedCommand(@NotNull String source) {
    if (!isValidSource(source)) {
      throw new IllegalArgumentException("invalid source, command limits violated.");
    }
    tokens = Collections.unmodifiableList(Arrays.asList(StringUtils.split(source)));
    if (tokens.isEmpty()) {
      throw new IllegalArgumentException("invalid source, no tokens obtained.");
    }
    this.stringRepresentation = StringUtils.join(tokens, ' ');
  }

  public static boolean isValidSource(String text) {
    return CommandLimits.isValidSource(text);
  }

  public String getStringRepresentation() {
    return stringRepresentation;
  }

  /**
   * Returns an unmodifiable view of the list of tokens.
   */
  public List<String> getTokens() {
    return tokens; // tokens is already unmodifiable.
  }

  @Override
  public String toString() {
    return getStringRepresentation();
  }

}
