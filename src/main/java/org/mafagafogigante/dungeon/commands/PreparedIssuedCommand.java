package org.mafagafogigante.dungeon.commands;

/**
 * An issued command that is ready to be executed.
 */
public class PreparedIssuedCommand {

  private final Command specifiedCommand;
  private final String[] arguments;

  PreparedIssuedCommand(Command specifiedCommand, String[] arguments) {
    this.specifiedCommand = specifiedCommand;
    this.arguments = arguments;
  }

  /**
   * Calls this PreparedIssuedCommand to execute its underlying command.
   */
  public void execute() {
    specifiedCommand.execute(arguments);
  }

}
