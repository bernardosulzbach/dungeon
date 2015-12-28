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

package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.game.ColoredString;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Writable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * SystemInformation class used to retrieve human readable information about the system on which the application is
 * running.
 *
 * <p>The information is generated at construction time, therefore the caller should not cache objects of this class.
 */
public final class SystemInformation extends Writable {

  private final DungeonString information = compileInformation();

  private static String getUserString() {
    return System.getProperty("user.name");
  }

  private static String getJavaVersionString() {
    return "Java version " + System.getProperty("java.version") + " by " + System.getProperty("java.vendor");
  }

  private static String getOsVersionString() {
    String name = System.getProperty("os.name");
    String arch = System.getProperty("os.arch");
    String version = System.getProperty("os.version");
    return String.format("%s (%s) %s", name, arch, version);
  }

  private DungeonString compileInformation() {
    Date currentDate = new Date();
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String time = timeFormat.format(currentDate);
    String date = dateFormat.format(currentDate);
    DungeonString dungeonString = new DungeonString();
    dungeonString.append("Time: ", time, "\n");
    dungeonString.append("Date: ", date, "\n");
    dungeonString.append("User: ", getUserString(), "\n");
    dungeonString.append(getJavaVersionString(), "\n");
    dungeonString.append(getOsVersionString());
    return dungeonString;
  }

  @Override
  public List<ColoredString> toColoredStringList() {
    return information.toColoredStringList();
  }

  @Override
  public String toString() {
    return toJavaString();
  }

}
