package org.mafagafogigante.dungeon.logging;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * The Formatter used by the Logger to pretty format dates.
 */
class DungeonFormatter extends Formatter {

  private static final String LINE_SEPARATOR = System.lineSeparator();

  @Override
  public String format(LogRecord record) {
    final SimpleDateFormat timestampFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
    String timestamp = timestampFormat.format(record.getMillis());
    return timestamp + " (" + record.getLevel() + "): " + record.getMessage() + LINE_SEPARATOR;
  }

}
