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

import org.dungeon.game.Id;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Constants class is a general-purpose constant storing class.
 */
public class Constants {

  public static final int COLS = 100;

  // The string used to alert the player about invalid input.
  public static final String INVALID_INPUT = "Invalid input.";

  // DateFormats for time and date printing.
  public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
  public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
  public static final Color FORE_COLOR_NORMAL = Color.LIGHT_GRAY;

  // IDs
  public static final Id HERO_ID = new Id("HERO");

}
