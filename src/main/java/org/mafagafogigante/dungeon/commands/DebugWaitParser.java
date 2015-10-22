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

package org.mafagafogigante.dungeon.commands;

import org.mafagafogigante.dungeon.date.DungeonTimeParser;
import org.mafagafogigante.dungeon.date.Duration;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Engine;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.PartOfDay;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.util.DungeonMath;
import org.mafagafogigante.dungeon.util.Matches;
import org.mafagafogigante.dungeon.util.Messenger;
import org.mafagafogigante.dungeon.util.Utils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Arrays;

/**
 * The parser of the debugging Wait command.
 */
class DebugWaitParser {

  private DebugWaitParser() {
    throw new AssertionError();
  }

  /**
   * Evaluates and returns a constant representing which syntax was used.
   */
  private static Syntax evaluateSyntax(String[] arguments) {
    if (isForSyntax(arguments)) {
      return Syntax.FOR;
    } else if (isUntilNextSyntax(arguments)) {
      return Syntax.UNTIL;
    } else {
      return Syntax.INVALID;
    }
  }

  private static boolean isForSyntax(String[] arguments) {
    return arguments.length > 1 && "for".equalsIgnoreCase(arguments[0]);
  }

  private static boolean isUntilNextSyntax(String[] arguments) {
    return arguments.length > 2 && "until".equalsIgnoreCase(arguments[0]) && "next".equalsIgnoreCase(arguments[1]);
  }

  private static void writeDebugWaitSyntax() {
    DungeonString string = new DungeonString();
    string.append("Usage: wait ");
    final Color highlightColor = Color.ORANGE;
    string.setColor(highlightColor);
    string.append("for");
    string.resetColor();
    string.append(" [amount of time] or wait ");
    string.setColor(highlightColor);
    string.append("until next");
    string.resetColor();
    string.append(" [part of the day].");
    Writer.write(string);
  }

  static void parseDebugWait(@NotNull String[] arguments) {
    Syntax syntax = evaluateSyntax(arguments);
    if (syntax == Syntax.INVALID) {
      writeDebugWaitSyntax();
    } else {
      if (syntax == Syntax.FOR) {
        String timeString = StringUtils.join(arguments, " ", 1, arguments.length);
        try {
          Duration duration = DungeonTimeParser.parsePeriod(timeString);
          rollDate(DungeonMath.safeCastLongToInteger(duration.getSeconds()));
        } catch (IllegalArgumentException badArgument) {
          Writer.write("Provide small positive multipliers and units such as: '2 minutes and 10 seconds'");
        }
      } else if (syntax == Syntax.UNTIL) {
        Matches<PartOfDay> matches = Utils.findBestCompleteMatches(Arrays.asList(PartOfDay.values()), arguments[2]);
        if (matches.size() == 0) {
          Writer.write("That did not match any part of the day.");
        } else if (matches.size() == 1) {
          rollDate(PartOfDay.getSecondsToNext(Game.getGameState().getWorld().getWorldDate(), matches.getMatch(0)));
        } else {
          Messenger.printAmbiguousSelectionMessage();
        }
      }
    }
  }

  private static void rollDate(int seconds) {
    Engine.rollDateAndRefresh(seconds);
    Writer.write("Waited for " + seconds + " seconds.");
  }

  private enum Syntax {FOR, UNTIL, INVALID}

}
