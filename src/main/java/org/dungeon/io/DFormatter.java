package org.dungeon.io;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class DFormatter extends Formatter {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("[dd/mm/yyyy HH:mm:ss.SSS]");

    @Override
    public String format(LogRecord record) {
        return dateFormat.format(record.getMillis()) + " (" + record.getLevel() + ") : " + record.getMessage() + "\n";
    }
}
