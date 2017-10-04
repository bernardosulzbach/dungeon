package org.mafagafogigante.dungeon.commands;

import org.jetbrains.annotations.NotNull;

/**
 * Command abstract class that defines a type for command objects.
 *
 * <p>Make anonymous classes from this one to define commands.
 */
public abstract class Command {

  private final CommandDescription description;

  /**
   * Creates a new Command object with the provided name and info.
   *
   * @param name a String for name, lowercase
   * @param info a String for info
   */
  Command(@NotNull String name, @NotNull String info) {
    description = new CommandDescription(name, info);
  }

  public CommandDescription getDescription() {
    return description;
  }

  /**
   * Indicates whether a Command is "equal to" this one.
   *
   * <p>Two Commands are considered to be equal when their names are equal.
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Command command = (Command) object;
    return description.getName().equals(command.description.getName());
  }

  @Override
  public int hashCode() {
    return description.getName().hashCode();
  }

  /**
   * Executes this Command, given an IssuedCommand object.
   */
  public abstract void execute(@NotNull String[] arguments);

  @Override
  public String toString() {
    return description.toString();
  }

}
