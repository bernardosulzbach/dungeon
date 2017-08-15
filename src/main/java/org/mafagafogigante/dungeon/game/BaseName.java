package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.io.Version;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * BaseName immutable class that stores the singular and plural forms of a name.
 */
public final class BaseName extends Name implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final String singular;
  private final String plural;

  /**
   * Constructs a name based on the specified forms. Names should be created through NameFactory and not through this
   * constructor.
   *
   * @param singular the singular form, not null
   * @param plural the plural form, not null
   */
  BaseName(@NotNull String singular, @NotNull String plural) {
    this.singular = singular;
    this.plural = plural;
  }

  @Override
  public String getSingular() {
    return singular;
  }

  @Override
  public String getPlural() {
    return plural;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    BaseName name = (BaseName) object;

    return singular.equals(name.singular) && plural.equals(name.plural);
  }

  @Override
  public int hashCode() {
    int result = singular.hashCode();
    result = 31 * result + plural.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return getSingular();
  }

}
