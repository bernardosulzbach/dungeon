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

import org.mafagafogigante.dungeon.io.Writer;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class SystemInfo {

  private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

  private SystemInfo() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Prints some information about the system.
   */
  public static void printSystemInfo() {
    Date currentDate = new Date();
    Writer.write("Time: " + TIME_FORMAT.format(currentDate));
    Writer.write("Date: " + DATE_FORMAT.format(currentDate));
    Writer.write("User: " + System.getProperty("user.name"));
    Writer.write(getJavaVersionString());
    Writer.write(getOsVersionString());
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

}
