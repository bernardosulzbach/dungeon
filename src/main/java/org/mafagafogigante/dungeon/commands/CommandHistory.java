package org.mafagafogigante.dungeon.commands;

import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.CircularList;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * CommandHistory class that is used to keep track of all the commands issued by the player.
 */
public class CommandHistory implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private static final int HISTORY_MAXIMUM_SIZE = 200; // Enough. Small so it doesn't slow down 'TAB' search.
  private final CircularList<String> commands = new CircularList<>(HISTORY_MAXIMUM_SIZE);
  private transient Cursor cursor = new Cursor(this);

  private Object readResolve() {
    cursor = new Cursor(this);
    return this;
  }

  /**
   * Returns a CommandHistory.Cursor to provide access to the stored commands.
   */
  public Cursor getCursor() {
    return cursor;
  }

  /**
   * Returns the number of commands in this CommandHistory.
   */
  private int size() {
    return commands.size();
  }

  /**
   * Returns true if this CommandHistory contains no commands.
   */
  private boolean isEmpty() {
    return commands.isEmpty();
  }

  /**
   * Adds an IssuedCommand to this CommandHistory and moves the cursor to the end.
   */
  public void addCommand(IssuedCommand issuedCommand) {
    commands.add(issuedCommand.getStringRepresentation());
    getCursor().moveToEnd();
  }

  /**
   * Returns a String representation of the last similar command or null if no similar command was found.
   */
  public String getLastSimilarCommand(String command) {
    for (int i = commands.size() - 1; i >= 0; i--) {
      if (StringUtils.startsWithIgnoreCase(commands.get(i), command)) {
        return commands.get(i);
      }
    }
    return null;
  }

  /**
   * Cursor inner class of CommandHistory that provides a set of methods for browsing and querying a CommandHistory.
   */
  public static final class Cursor implements Serializable {

    private static final long serialVersionUID = Version.MAJOR;
    private final CommandHistory history;
    private int index;

    Cursor(CommandHistory history) {
      this.history = history;
      moveToEnd();
    }

    /**
     * Returns the selected command or null if the CommandHistory does not have any commands or if the cursor is at the
     * end of the CommandHistory.
     *
     * @return the selected command
     */
    @Nullable
    public String getSelectedCommand() {
      if (!history.isEmpty() && index < history.size()) {
        return history.commands.get(index);
      } else {
        return null;
      }
    }

    /**
     * Move the cursor up one command if it is not at the beginning of the history.
     *
     * @return the cursor
     */
    public Cursor moveUp() {
      if (index != 0) {
        index--;
      }
      return this;
    }

    /**
     * Move the cursor down one command if it is not at the end of the history.
     *
     * @return the cursor
     */
    public Cursor moveDown() {
      if (index < history.size()) {
        index++;
      }
      return this;
    }

    /**
     * Sets the cursor to one past the last command of the CommandHistory. The selected command after a call to this
     * method is granted to be null.
     */
    public void moveToEnd() {
      index = history.size();
    }

  }

}
