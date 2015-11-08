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

package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.util.Table;
import org.mafagafogigante.dungeon.util.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This class provides a method to write to the screen a table with the existing saves.
 */
public final class SavesTableWriter {

  private static final SimpleDateFormat LAST_MODIFIED_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private SavesTableWriter() {
    throw new AssertionError();
  }

  /**
   * Writes a table of the files found on the saves folder to the screen.
   */
  public static void writeSavesFolderTable() {
    List<File> files = Loader.getSavedFiles();
    if (!files.isEmpty()) {
      Table table = new Table("Name", "Size", "Last modified");
      int fileCount = 0;
      int byteCount = 0;
      for (File file : files) {
        fileCount += 1;
        byteCount += file.length();
        Date lastModified = new Date(file.lastModified());
        String periodString = Utils.makePeriodString(System.currentTimeMillis() - lastModified.getTime()) + " ago";
        String lastModifiedString = String.format("%s (%s)", LAST_MODIFIED_FORMAT.format(lastModified), periodString);
        table.insertRow(file.getName(), Converter.bytesToHuman(file.length()), lastModifiedString);
      }
      if (fileCount > 1) {
        table.insertSeparator();
        table.insertRow("Sum of these " + fileCount + " files", Converter.bytesToHuman((byteCount)), "");
      }
      Writer.write(table);
    } else {
      Writer.write("There are no saved files.");
    }
  }

}
