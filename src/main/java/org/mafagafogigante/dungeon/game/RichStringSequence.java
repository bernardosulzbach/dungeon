package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.logging.DungeonLogger;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RichStringSequence extends Writable {

  private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;

  private final List<RichString> richStringList = new ArrayList<>();
  private final StringBuilder builder = new StringBuilder();
  private Color currentColor = DEFAULT_COLOR;

  /**
   * Constructs an empty RichStringSequence.
   */
  public RichStringSequence() {
  }

  /**
   * Constructs a RichStringSequence that starts with the specified text.
   */
  public RichStringSequence(String text) {
    append(text);
  }

  /**
   * Constructs a RichStringSequence that starts with the specified text and color.
   */
  public RichStringSequence(String text, Color color) {
    setColor(color);
    append(text);
    resetColor();
  }

  /**
   * Returns only the text of the string.
   */
  public String getString() {
    StringBuilder builder = new StringBuilder();
    for (RichString richString : toRichStrings()) {
      builder.append(richString.getString());
    }
    return builder.toString();
  }

  /**
   * Returns the total length of the string.
   */
  public int getLength() {
    int sum = 0;
    for (RichString richString : toRichStrings()) {
      sum += richString.getString().length();
    }
    sum += builder.length();
    return sum;
  }

  public List<RichString> toRichStrings() {
    addBuilderContentToList();
    return Collections.unmodifiableList(richStringList);
  }

  private void addBuilderContentToList() {
    if (builder.length() != 0) {
      richStringList.add(new RichString(builder.toString(), currentColor));
      builder.setLength(0);
    }
  }

  /**
   * Appends one or more strings to this RichStringSequence.
   *
   * <p>Logs a warning if called with an empty array.
   *
   * @param strings one or more Strings
   */
  public void append(@NotNull String... strings) {
    if (strings.length == 0) {
      DungeonLogger.warning("Called RichStringSequence.append with empty vararg");
    }
    for (String string : strings) {
      builder.append(string);
    }
  }

  /**
   * Changes the current color of this RichStringSequence. This will only impact future calls to <code>append</code>.
   *
   * <p>Passing the current color of this RichStringSequence is a no-op.
   *
   * @param color a Color object
   */
  public void setColor(@NotNull Color color) {
    if (currentColor != color) {
      addBuilderContentToList();
      currentColor = color;
    }
  }

  /**
   * Resets the color of this RichStringSequence to the default color.
   */
  public void resetColor() {
    setColor(DEFAULT_COLOR);
  }

  @Override
  public String toString() {
    return "RichStringSequence{" + "richStringList=" + toRichStrings() + ", currentColor=" + currentColor + '}';
  }

}
