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

package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.game.ColoredString;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Writable;
import org.mafagafogigante.dungeon.io.JsonObjectFactory;

import java.util.List;

/**
 * Tutorial class that Created by Bernardo on 23/10/2015.
 */
public class Tutorial implements Writable {

  private static final String text = JsonObjectFactory.makeJsonObject("tutorial.json").get("tutorial").asString();

  @Override
  public List<ColoredString> toColoredStringList() {
    return new DungeonString(text).toColoredStringList();
  }

}