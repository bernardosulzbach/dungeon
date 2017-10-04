package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.io.Version;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

public class QualifiedName extends Name implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final Name name;
  private final List<String> prefixes;
  private final List<String> suffixes;

  /**
   * Constructs a QualifiedName from the provided Name and prefix and suffix lists.
   *
   * <p>The strings in these lists should not have any padding.
   */
  public QualifiedName(Name name, List<String> prefixes, List<String> suffixes) {
    this.name = name;
    this.prefixes = prefixes;
    this.suffixes = suffixes;
  }

  private String getConcatenatedPrefixes() {
    String string = StringUtils.join(prefixes, " ");
    if (!string.isEmpty()) {
      string = string + " ";
    }
    return string;
  }

  private String getConcatenatedSuffixes() {
    String string = StringUtils.join(suffixes, " ");
    if (!string.isEmpty()) {
      string = " " + string;
    }
    return string;
  }

  @Override
  public String getSingular() {
    return getConcatenatedPrefixes() + name.getSingular() + getConcatenatedSuffixes();
  }

  @Override
  public String getPlural() {
    return getConcatenatedPrefixes() + name.getPlural() + getConcatenatedSuffixes();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    QualifiedName that = (QualifiedName) other;
    if (name != null ? !name.equals(that.name) : that.name != null) {
      return false;
    }
    if (prefixes != null ? !prefixes.equals(that.prefixes) : that.prefixes != null) {
      return false;
    }
    return suffixes != null ? suffixes.equals(that.suffixes) : that.suffixes == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (prefixes != null ? prefixes.hashCode() : 0);
    result = 31 * result + (suffixes != null ? suffixes.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return getSingular();
  }

}
