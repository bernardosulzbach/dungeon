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
    RichText text = new StandardRichTextBuilder().append("This command requires arguments.").toRichText();
    Writer.getDefaultWriter().write(text);
  }

  public static void printAmbiguousSelectionMessage() {
    RichText text = new StandardRichTextBuilder().append("Provided input is ambiguous.").toRichText();
    Writer.getDefaultWriter().write(text);
  }

  /**
   * Prints a warning that a directory creation failed.
   */
  public static void printFailedToCreateDirectoryMessage(String directory) {
    StandardRichTextBuilder builder = new StandardRichTextBuilder();
    Writer.getDefaultWriter().write(builder.append("Failed to create the '" + directory + "' directory.").toRichText());
  }

}
