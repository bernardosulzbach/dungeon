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

package org.dungeon.util;

import org.dungeon.game.IssuedCommand;

import java.io.Serializable;

/**
 * CommandHistory class that is used to keep track of all the commands issued by the player.
 * <p/>
 * Created by Bernardo Sulzbach on 23/09/2014.
 */
public class CommandHistory implements Serializable {

  // Let the optimal size of a String s be 8 * (int) (s.length() * 2 + 45) / 8) bytes.
  // Thus, 200 Strings of 8 characters would cost 1640 bytes (a bit more than 1.6 kB).
  // Besides the obvious cost of memory, having too many Strings in the history would also slow down the TAB search.
  private static final int HISTORY_MAXIMUM_SIZE = 200;
  private final CircularList<String> commands;
  private transient Cursor cursor;

  public CommandHistory() {
    commands = new CircularList<String>(HISTORY_MAXIMUM_SIZE);
    cursor = new Cursor(this);
  }

  /**
   * @return a CommandHistory.Cursor to provide access to the stored commands.
   */
  public Cursor getCursor() {
    if (cursor == null) {
      cursor = new Cursor(this);
    }
    return cursor;
  }

  /**
   * @return the number of commands in this CommandHistory.
   */
  int size() {
    return commands.size();
  }

  /**
   * @return true if this CommandHistory contains no commands.
   */
  public boolean isEmpty() {
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
   * @return a String representation of the last similar command or {@code null} if no similar command was found.
   */
  public String getLastSimilarCommand(String command) {
    for (int i = commands.size() - 1; i >= 0; i--) {
      if (Utils.startsWithIgnoreCase(commands.get(i), command)) {
        return commands.get(i);
      }
    }
    return null;
  }

  /**
   * Cursor inner class of CommandHistory that provides a set of methods for browsing and querying a CommandHistory.
   */
  public class Cursor implements Serializable {

    private final CommandHistory history;
    private int index;

    Cursor(CommandHistory history) {
      this.history = history;
      moveToEnd();
    }

    /**
     * @return the selected command or {@code null} if the CommandHistory does not have any commands or the
     * cursor is at the end of the CommandHistory.
     */
    public String getSelectedCommand() {
      if (!history.isEmpty() && index < history.size()) {
        return history.commands.get(index);
      } else {
        return null;
      }
    }

    /**
     * Move the cursor up one command. If the cursor is at the beginning of the history, it does not move.
     *
     * @return the cursor.
     */
    public Cursor moveUp() {
      if (index != 0) {
        index--;
      }
      return this;
    }

    /**
     * Move the cursor down one command. If the cursor is at one past the end of the history, it does not move.
     *
     * @return the cursor.
     */
    public Cursor moveDown() {
      if (index < history.size()) {
        index++;
      }
      return this;
    }

    /**
     * Sets the cursor to one past the last command of the CommandHistory. Retrieving the selected entry after
     * calling this method will return {@code null}.
     */
    public Cursor moveToEnd() {
      index = history.size();
      return this;
    }

  }

}
