/*
 * Copyright (C) 2014 Bernardo Sulzbach
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

import org.dungeon.io.IO;
import org.dungeon.util.Messenger;
import org.dungeon.util.Utils;

import java.util.List;

/**
 * CommandHelp class that provides useful information about a single Command.
 */
final class CommandHelp {

  private static final int COMMAND_NAME_COLUMN_WIDTH = 20;

  private CommandHelp() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  private static String noCommandStartsWith(String providedString) {
    return "No command starts with '" + providedString + "'.";
  }

  /**
   * Prints a short help message with information about a command specified in the last issued command.
   *
   * @param issuedCommand an IssuedCommand
   */
  public static void printHelp(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      List<CommandDescription> descriptions = CommandCollection.getDefaultCommandCollection().getCommandDescriptions();
      CommandDescription selectedCommand = null;
      for (CommandDescription description : descriptions) {
        if (Utils.startsWithIgnoreCase(description.getName(), issuedCommand.getFirstArgument())) {
          if (selectedCommand == null) {
            selectedCommand = description;
          } else {
            Messenger.printAmbiguousSelectionMessage();
            return;
          }
        }
      }
      if (selectedCommand == null) {
        IO.writeString(noCommandStartsWith(issuedCommand.getFirstArgument()));
      } else {
        IO.writeString(selectedCommand.toString());
      }
    } else {
      Messenger.printMissingArgumentsMessage();
    }
  }

  /**
   * Prints a list of all the available commands.
   *
   * @param issuedCommand an IssuedCommand
   */
  public static void printCommandList(IssuedCommand issuedCommand) {
    String filter = null;
    if (issuedCommand.hasArguments()) {
      filter = issuedCommand.getFirstArgument();
    }
    List<CommandDescription> descriptions = CommandCollection.getDefaultCommandCollection().getCommandDescriptions();
    StringBuilder builder = new StringBuilder();
    for (CommandDescription description : descriptions) {
      if (filter == null || Utils.startsWithIgnoreCase(description.getName(), filter)) {
        builder.append(Utils.padString(description.getName(), COMMAND_NAME_COLUMN_WIDTH));
        builder.append(description.getInfo());
        builder.append('\n');
      }
    }
    if (builder.length() == 0) {
      IO.writeString(noCommandStartsWith(issuedCommand.getFirstArgument()));
    } else {
      IO.writeString(builder.toString());
    }
  }

}
