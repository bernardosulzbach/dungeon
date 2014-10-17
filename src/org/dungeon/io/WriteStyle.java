package org.dungeon.io;

/**
 * Enumerated type that provides a set of different writing styles that dictate how an output string will be formatted.
 * 
 * @author Bernardo Sulzbach
 */
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
    WARNING,
    /**
     * Does not modify the provided string before printing it.
     */
    NONE;
}
