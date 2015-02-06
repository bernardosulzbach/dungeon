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

package org.dungeon.wiki;

import org.dungeon.game.IssuedCommand;
import org.dungeon.io.IO;
import org.dungeon.util.Utils;

/**
 * The Wiki class. Loads the contents of the wiki.txt file and manages wiki articles.
 * <p/>
 * Created by Bernardo on 06/02/2015.
 */
public class Wiki {

  private Wiki() {
  }

  /**
   * Searches the wiki and prints the matching contents to the screen.
   *
   * @param issuedCommand an IssuedCommand
   */
  public static void search(IssuedCommand issuedCommand) {
    if (issuedCommand.hasArguments()) {
      IO.writeString("Wiki not yet implemented.");
    } else {
      Utils.printMissingArgumentsMessage();
    }
  }

}
