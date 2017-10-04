package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.util.ColumnAlignment;
import org.mafagafogigante.dungeon.util.Table;
import org.mafagafogigante.dungeon.util.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class provides a method to write to the screen a table with the existing saves.
 */
public final class SavesTableWriter {

  private SavesTableWriter() {
    throw new AssertionError();
  }

  /**
   * Writes a table of the files found on the saves folder to the screen.
   */
  public static void writeSavesFolderTable() {
    List<File> files = Loader.getSavedFiles();
    List<Version> versions = Loader.getSavedFilesVersions(files);
    if (!files.isEmpty()) {
      List<ColumnAlignment> columnAlignments = new ArrayList<>();
      columnAlignments.add(ColumnAlignment.LEFT);
      columnAlignments.add(ColumnAlignment.RIGHT);
      columnAlignments.add(ColumnAlignment.RIGHT);
      columnAlignments.add(ColumnAlignment.LEFT);
      Table table = new Table("Name", "Size", "Version", "Last modified");
      table.setColumnAlignments(columnAlignments);
      int fileCount = 0;
      int byteCount = 0;
      final SimpleDateFormat lastModifiedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      for (int i = 0; i < files.size(); i++) {
        File file = files.get(i);
        Version version = versions.get(i);
        fileCount += 1;
        byteCount += file.length();
        Date lastModified = new Date(file.lastModified());
        String periodString = Utils.makePeriodString(System.currentTimeMillis() - lastModified.getTime()) + " ago";
        String lastModifiedString = String.format("%s (%s)", lastModifiedFormat.format(lastModified), periodString);
        String versionString = version == null ? "N/A" : version.toString();
        table.insertRow(file.getName(), Converter.bytesToHuman(file.length()), versionString, lastModifiedString);
      }
      if (fileCount > 1) {
        table.insertSeparator();
        table.insertRow("Sum of these " + fileCount + " files", Converter.bytesToHuman((byteCount)), "", "");
      }
      Writer.write(table);
    } else {
      Writer.write("There are no saved files.");
    }
  }

}
