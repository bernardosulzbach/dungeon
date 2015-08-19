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

package org.dungeon.io;

import org.dungeon.date.EarthTimeUnit;
import org.dungeon.date.TimeStringBuilder;
import org.dungeon.util.Table;

import org.joda.time.Period;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class provides a method to write to the screen a table with the existing saves.
 */
public final class SavesTableWriter {

  private static final SimpleDateFormat LAST_MODIFIED_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private SavesTableWriter() {
    throw new AssertionError();
  }

  public static void writeSavesFolderTable() {
    File[] files = Loader.getSortedArrayOfSavedFiles();
    if (files != null) {
      if (files.length != 0) {
        Table table = new Table("Name", "Size", "Last modified");
        int fileCount = 0;
        int byteCount = 0;
        for (File file : files) {
          fileCount += 1;
          byteCount += file.length();
          Date lastModified = new Date(file.lastModified());
          String periodString = makePeriodString(lastModified.getTime(), System.currentTimeMillis());
          String lastModifiedString = String.format("%s (%s)", LAST_MODIFIED_FORMAT.format(lastModified), periodString);
          table.insertRow(file.getName(), IOUtils.bytesToHuman(file.length()), lastModifiedString);
        }
        if (fileCount > 1) {
          table.insertSeparator();
          table.insertRow("Sum of these " + fileCount + " files", IOUtils.bytesToHuman((byteCount)), "");
        }
        Writer.write(table);
      } else {
        Writer.writeString("Saves folder is empty.");
      }
    } else {
      Writer.writeString("Saves folder does not exist.");
    }
  }

  private static String makePeriodString(long start, long end) {
    Period period = new Period(start, end);
    TimeStringBuilder builder = new TimeStringBuilder();
    builder.set(EarthTimeUnit.YEAR, period.getYears());
    builder.set(EarthTimeUnit.MONTH, period.getMonths());
    builder.set(EarthTimeUnit.DAY, period.getDays());
    builder.set(EarthTimeUnit.HOUR, period.getHours());
    builder.set(EarthTimeUnit.MINUTE, period.getMinutes());
    builder.set(EarthTimeUnit.SECOND, period.getSeconds());
    return builder.toString(2) + " ago";
  }

}
