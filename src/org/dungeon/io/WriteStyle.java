package org.dungeon.io;

// TODO: document this.
public enum WriteStyle {
    /**
     * Add a blank line between text lines and use Constants.MARGIN twice before each text line.
     */
    COMMAND,
    /**
     * Add a Constants.MARGIN before all non-empty lines.
     */
    MARGIN,
    /**
     * Add Constants.WARNING before the line (warnings should not have multiple lines).
     */
    WARNING
}
