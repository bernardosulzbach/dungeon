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

import org.dungeon.date.DungeonTimeParser;
import org.dungeon.date.Period;
import org.dungeon.game.DungeonStringBuilder;
import org.dungeon.game.Engine;
import org.dungeon.game.Game;
import org.dungeon.game.PartOfDay;
import org.dungeon.io.Writer;
import org.dungeon.util.DungeonMath;
import org.dungeon.util.Matches;
import org.dungeon.util.Messenger;
import org.dungeon.util.Utils;

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
    DungeonStringBuilder builder = new DungeonStringBuilder();
    builder.append("Usage: wait ");
    final Color HIGHLIGHT_COLOR = Color.ORANGE;
    builder.setColor(HIGHLIGHT_COLOR);
    builder.append("for");
    builder.resetColor();
    builder.append(" [amount of time] or wait ");
    builder.setColor(HIGHLIGHT_COLOR);
    builder.append("until next");
    builder.resetColor();
    builder.append(" [part of the day].");
    Writer.write(builder);
  }

  static void parseDebugWait(@NotNull String[] arguments) {
    Syntax syntax = evaluateSyntax(arguments);
    if (syntax == Syntax.INVALID) {
      writeDebugWaitSyntax();
    } else {
      if (syntax == Syntax.FOR) {
        String timeString = StringUtils.join(arguments, " ", 1, arguments.length);
        try {
          Period period = DungeonTimeParser.parsePeriod(timeString);
          rollDate(DungeonMath.safeCastLongToInteger(period.getSeconds()));
        } catch (IllegalArgumentException badArgument) {
          Writer.writeString("Provide small positive multipliers and units such as: '2 minutes and 10 seconds'");
        }
      } else if (syntax == Syntax.UNTIL) {
        Matches<PartOfDay> matches = Utils.findBestCompleteMatches(Arrays.asList(PartOfDay.values()), arguments[2]);
        if (matches.size() == 0) {
          Writer.writeString("That did not match any part of the day.");
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
    Writer.writeString("Waited for " + seconds + " seconds.");
  }

  private enum Syntax {FOR, UNTIL, INVALID}

}
