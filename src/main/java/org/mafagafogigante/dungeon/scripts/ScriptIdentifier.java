package org.mafagafogigante.dungeon.scripts;

import org.apache.commons.lang3.CharUtils;

public class ScriptIdentifier {

  private final String string;

  /**
   * Constructs a ScriptIdentifier from a String, throwing an IllegalArgumentException if it is not a valid identifier.
   */
  public ScriptIdentifier(String string) {
    if (!isValid(string)) {
      throw new IllegalArgumentException('"' + string + '"' + " is not a valid script identifier");
    }
    this.string = string;
  }

  private boolean isValid(String string) {
    if (string.isEmpty()) {
      return false;
    }
    for (char character : string.toCharArray()) {
      if (!CharUtils.isAsciiAlphaLower(character) && character != '-') {
        return false;
      }
    }
    return true;
  }

  public String asString() {
    return string;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    ScriptIdentifier that = (ScriptIdentifier) other;
    return string != null ? string.equals(that.string) : that.string == null;
  }

  @Override
  public int hashCode() {
    return string != null ? string.hashCode() : 0;
  }

}
