package org.mafagafogigante.dungeon.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StandardRichText implements RichText {

  private final List<RichString> richStrings;

  public StandardRichText(List<RichString> richStrings) {
    this.richStrings = new ArrayList<>(richStrings);
  }

  @Override
  public List<RichString> toRichStrings() {
    return Collections.unmodifiableList(richStrings);
  }

  @Override
  public String toJavaString() {
    int size = 0;
    for (RichString richString : richStrings) {
      size += richString.getString().length();
    }
    StringBuilder builder = new StringBuilder(size);
    for (RichString richString : richStrings) {
      builder.append(richString.getString());
    }
    return builder.toString();
  }

  @Override
  public String toString() {
    return toJavaString();
  }

}
