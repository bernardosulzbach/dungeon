package org.mafagafogigante.dungeon.util;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class StandardRichTextBuilder {

  private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;

  private final List<RichString> richStringList = new ArrayList<>();
  private final StringBuilder builder = new StringBuilder();
  private Color currentColor = DEFAULT_COLOR;

  private void addBuilderContentToList() {
    if (builder.length() != 0) {
      richStringList.add(new StandardRichString(builder.toString(), currentColor));
      builder.setLength(0);
    }
  }

  private List<RichString> getRichStringsList() {
    addBuilderContentToList();
    return Collections.unmodifiableList(richStringList);
  }

  /**
   * Returns only the text of the string.
   */
  public String getString() {
    StringBuilder builder = new StringBuilder();
    for (RichString richString : getRichStringsList()) {
      builder.append(richString.getString());
    }
    return builder.toString();
  }

  /**
   * Returns the total length of the string.
   */
  public int getLength() {
    int sum = 0;
    for (RichString richString : getRichStringsList()) {
      sum += richString.getString().length();
    }
    return sum;
  }

  public StandardRichTextBuilder append(@NotNull String string) {
    builder.append(string);
    return this;
  }

  /**
   * Changes the current color of this StandardRichTextBuilder. This will only impact future calls to
   * <code>append</code>.
   *
   * <p>Passing the current color of this StandardRichTextBuilder is a no-op.
   *
   * @param color a Color object
   */
  public StandardRichTextBuilder setColor(@NotNull Color color) {
    if (currentColor != color) {
      addBuilderContentToList();
      currentColor = color;
    }
    return this;
  }

  /**
   * Resets the color of this StandardRichTextBuilder to the default color.
   */
  public void resetColor() {
    setColor(DEFAULT_COLOR);
  }

  public RichText toRichText() {
    return new StandardRichText(getRichStringsList());
  }

  @Override
  public String toString() {
    return "StandardRichTextBuilder{" + "richStringList=" + getRichStringsList() + ", currentColor=" + currentColor +
        '}';
  }

}
