package org.mafagafogigante.dungeon.commands;

import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.logging.DungeonLogger;
import org.mafagafogigante.dungeon.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A set of Commands.
 */
public final class CommandSet {

  private static final int COMMAND_NAME_COLUMN_WIDTH = 20;

  private final List<Command> commands = new ArrayList<>();
  private final List<CommandDescription> commandDescriptions = new ArrayList<>();

  private CommandSet() {
  }

  /**
   * Constructs an empty CommandSet containing only the "commands" Command.
   */
  public static CommandSet emptyCommandSet() {
    final CommandSet commandSet = new CommandSet();
    commandSet.addCommand(new Command("commands", "Lists all commands in this command set.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        String filter = arguments.length == 0 ? null : arguments[0];
        List<CommandDescription> descriptions = commandSet.getCommandDescriptions();
        DungeonString dungeonString = new DungeonString();
        int count = 0;
        for (CommandDescription description : descriptions) {
          if (filter == null || Utils.startsWithIgnoreCase(description.getName(), filter)) {
            count++;
            dungeonString.append(Utils.padString(description.getName(), COMMAND_NAME_COLUMN_WIDTH));
            dungeonString.append(description.getInfo());
            dungeonString.append("\n");
          }
        }
        if (count == 0 && filter != null) {
          Writer.write("No command starts with '" + filter + "'.");
        } else {
          if (count > 1) {
            dungeonString.append("\nListed ");
            dungeonString.append(String.valueOf(count));
            dungeonString.append(" commands.");
            if (filter == null) {
              dungeonString.append("\nYou can filter the output of this command by typing the beginning of a command.");
            }
          }
          Writer.write(dungeonString);
        }
      }
    });
    return commandSet;
  }

  /**
   * Retrieves a Command corresponding to the specified token or null if no command matches the token.
   */
  public Command getCommand(String token) {
    for (Command command : commands) {
      if (command.getDescription().getName().equalsIgnoreCase(token)) {
        return command;
      }
    }
    return null;
  }

  /**
   * Adds a Command to this CommandSet.
   *
   * @param command a Command object, not null
   */
  void addCommand(Command command) {
    if (command == null) {
      DungeonLogger.warning("Passed null to CommandSet.addCommand().");
    } else if (commands.contains(command)) {
      DungeonLogger.warning("Attempted to add the same Command to a CommandSet twice.");
    } else {
      commands.add(command);
      commandDescriptions.add(command.getDescription());
    }
  }

  /**
   * Returns an unmodifiable view of the List of CommandDescriptions.
   *
   * @return an unmodifiable List of CommandDescriptions
   */
  private List<CommandDescription> getCommandDescriptions() {
    return Collections.unmodifiableList(commandDescriptions);
  }

  @Override
  public String toString() {
    return String.format("CommandSet of size %d.", commands.size());
  }

  /**
   * Retrieves a list of the names of the commands closest to the provided token according to their Levenshtein
   * distance.
   */
  List<String> getClosestCommands(final String token) {
    List<String> closestCommands = new ArrayList<>();
    int best = Integer.MAX_VALUE;
    for (Command command : commands) {
      String commandName = command.getDescription().getName();
      int distance = StringDistanceMetrics.levenshteinDistance(token, commandName);
      if (distance < best) {
        closestCommands.clear();
        best = distance;
      }
      if (distance == best) {
        closestCommands.add(commandName);
      }
    }
    return closestCommands;
  }

}
