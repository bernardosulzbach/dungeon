/*
 * Copyright (C) 2015 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dungeon.commands;

/**
 * Allows processing of IssuedCommands to produce PreparedIssuedCommands.
 */
public class IssuedCommandProcessor {

  public static PreparedIssuedCommand prepareIssuedCommand(IssuedCommand issuedCommand) {
    CommandSet collection;
    String commandToken;
    int indexOfFirstArgument;
    if (issuedCommand.getTokens().length > 1 && CommandSets.hasCommandSet(issuedCommand.getTokens()[0])) {
      collection = CommandSets.getCommandSet(issuedCommand.getTokens()[0]);
      commandToken = issuedCommand.getTokens()[1];
      indexOfFirstArgument = 2;
    } else {
      collection = CommandSets.getCommandSet("default");
      commandToken = issuedCommand.getTokens()[0];
      indexOfFirstArgument = 1;
    }
    if (collection.getCommand(commandToken) == null) {
      throw new InvalidCommandException(commandToken);
    }
    String[] arguments = makeArgumentArray(issuedCommand, indexOfFirstArgument);
    Command selectedCommand = collection.getCommand(commandToken);
    return new PreparedIssuedCommand(selectedCommand, arguments);
  }

  private static String[] makeArgumentArray(IssuedCommand issuedCommand, int indexOfFirstArgument) {
    int argumentCount = issuedCommand.getTokens().length - indexOfFirstArgument;
    String[] arguments = new String[issuedCommand.getTokens().length - indexOfFirstArgument];
    System.arraycopy(issuedCommand.getTokens(), indexOfFirstArgument, arguments, 0, argumentCount);
    return arguments;
  }

}
