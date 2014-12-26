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

package org.dungeon.game;

import org.dungeon.io.IO;
import org.dungeon.util.Utils;

import java.awt.Color;

/**
 * Some in-game configuration methods.
 * <p/>
 * Created by Bernardo Sulzbach on 04/11/14.
 */
class ConfigTools {

  private static final String[] args = {"bold", "generator", "list", "rows"};

  private static final int MIN_ROWS = 10;
  private static final int MAX_ROWS = 50;

  private static void listAllArguments() {
    IO.writeString("Arguments are: ");
    for (String arg : args) {
      IO.writeString("\n  " + arg);
    }
  }

  private static void toggleBold() {
    boolean newBoldValue = !Game.getGameState().isBold();
    Game.getGameState().setBold(newBoldValue);
    IO.writeString("Bold set to " + newBoldValue + ".");
  }

  private static boolean changeRowCount(String argument) {
    try {
      int rows = Integer.parseInt(argument);
      if (rows < MIN_ROWS || rows > MAX_ROWS) {
        IO.writeString("Row count should be in the range [" + MIN_ROWS + ", " + MAX_ROWS + "].");
      } else {
        // setRows returns true if the number of rows changed.
        if (Game.getGameWindow().setRows(rows)) {
          IO.writeString("Rows set to " + rows + ".");
          return true;
        } else {
          IO.writeString("Row count unchanged.");
        }
      }
    } catch (NumberFormatException exception) {
      IO.writeString("Provide a valid number of rows.", Color.RED);
    }
    return false;
  }

  // Returns true if the chunk side changed.
  private static boolean changeChunkSide(String argument) {
    try {
      int givenSide = Integer.parseInt(argument);
      int oldChunkSide = Game.getGameState().getWorld().getGenerator().getChunkSide();
      // The setter returns the new chunk side.
      int newChunkSide = Game.getGameState().getWorld().getGenerator().setChunkSide(givenSide);
      if (oldChunkSide == newChunkSide) {
        IO.writeString("Chunk side unchanged.");
      } else {
        IO.writeString("Chunk side set to " + newChunkSide + ".");
        return true;
      }
    } catch (NumberFormatException exception) {
      IO.writeString("Provide a valid number.", Color.RED);
    }
    return false;
  }

  // Returns true if a configuration was changed.
  static boolean parseConfigCommand(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      if (issuedCommand.firstArgumentEquals(args[0])) {
        toggleBold();
        return true;
      } else if (issuedCommand.firstArgumentEquals(args[1])) {
        if (issuedCommand.getArguments().length > 1) {
          return changeChunkSide(issuedCommand.getArguments()[1]);
        } else {
          IO.writeString("Provide a numerical argument.");
        }
      } else if (issuedCommand.firstArgumentEquals(args[2])) {
        listAllArguments();
      } else if (issuedCommand.firstArgumentEquals(args[3])) {
        if (issuedCommand.getArguments().length > 1) {
          return changeRowCount(issuedCommand.getArguments()[1]);
        } else {
          IO.writeString("Provide a number of rows.");
        }
      } else {
        IO.writeString("Invalid command. Use 'config list' to see all available configurations.", Color.RED);
      }
    } else {
      Utils.printMissingArgumentsMessage();
    }
    return false;
  }

}
