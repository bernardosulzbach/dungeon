package org.mafagafogigante.dungeon.commands;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * Allows processing of IssuedCommands to produce PreparedIssuedCommands.
 */
public class IssuedCommandProcessor {

  private IssuedCommandProcessor() {
    throw new AssertionError();
  }

  /**
   * Evaluates an IssuedCommand. This method will check if the IssuedCommand is valid or not and provide suggestions if
   * it is not.
   */
  public static IssuedCommandEvaluation evaluateIssuedCommand(@NotNull IssuedCommand issuedCommand) {
    CommandSet collection;
    String commandToken;
    if (issuedCommand.getTokens().size() > 1 && CommandSets.hasCommandSet(issuedCommand.getTokens().get(0))) {
      collection = CommandSets.getCommandSet(issuedCommand.getTokens().get(0));
      commandToken = issuedCommand.getTokens().get(1);
    } else {
      collection = CommandSets.getCommandSet("default");
      commandToken = issuedCommand.getTokens().get(0);
    }
    // At this point. Both collection and commandToken are not null.
    if (collection.getCommand(commandToken) == null) {
      return new IssuedCommandEvaluation(false, collection.getClosestCommands(commandToken));
    } else {
      return new IssuedCommandEvaluation(true, Collections.<String>emptyList());
    }
  }

  /**
   * Prepares an IssuedCommand. As a precondition, evaluateIssueCommand should have considered this IssuedCommand
   * valid.
   */
  public static PreparedIssuedCommand prepareIssuedCommand(@NotNull IssuedCommand issuedCommand) {
    CommandSet collection;
    String commandToken;
    int indexOfFirstArgument;
    if (issuedCommand.getTokens().size() > 1 && CommandSets.hasCommandSet(issuedCommand.getTokens().get(0))) {
      collection = CommandSets.getCommandSet(issuedCommand.getTokens().get(0));
      commandToken = issuedCommand.getTokens().get(1);
      indexOfFirstArgument = 2;
    } else {
      collection = CommandSets.getCommandSet("default");
      commandToken = issuedCommand.getTokens().get(0);
      indexOfFirstArgument = 1;
    }
    String[] arguments = makeArgumentArray(issuedCommand, indexOfFirstArgument);
    Command selectedCommand = collection.getCommand(commandToken);
    return new PreparedIssuedCommand(selectedCommand, arguments);
  }

  private static String[] makeArgumentArray(IssuedCommand issuedCommand, int indexOfFirstArgument) {
    String[] tokenArray = issuedCommand.getTokens().toArray(new String[issuedCommand.getTokens().size()]);
    int argumentCount = issuedCommand.getTokens().size() - indexOfFirstArgument;
    String[] arguments = new String[issuedCommand.getTokens().size() - indexOfFirstArgument];
    System.arraycopy(tokenArray, indexOfFirstArgument, arguments, 0, argumentCount);
    return arguments;
  }

}
