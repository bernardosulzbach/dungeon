package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.io.Writer;

/**
 * Messenger helper class that defines several static methods to print uniform warning messages.
 */
public class Messenger {

  private Messenger() { // Ensure that this class cannot be instantiated.
    throw new AssertionError();
  }

  /**
   * Prints a warning that a command requires arguments.
   */
  public static void printMissingArgumentsMessage() {
    Writer.write("This command requires arguments.");
  }

  public static void printAmbiguousSelectionMessage() {
    Writer.write("Provided input is ambiguous.");
  }

  /**
   * Prints a warning that a directory creation failed.
   */
  public static void printFailedToCreateDirectoryMessage(String directory) {
    Writer.write("Failed to create the '" + directory + "' directory.");
  }

}
