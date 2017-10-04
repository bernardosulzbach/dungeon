package org.mafagafogigante.dungeon.stats;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.io.Version;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * CauseOfDeath class that defines what kind of death happened and the ID of the related Item or Spell.
 */
public class CauseOfDeath implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private static final CauseOfDeath UNARMED = new CauseOfDeath(TypeOfCauseOfDeath.UNARMED, new Id("UNARMED"));
  private final TypeOfCauseOfDeath type;
  private final Id id;

  /**
   * Constructs a CauseOfDeath with the specified TypeOfCauseOfDeath and ID.
   *
   * @param type a TypeOfCauseOfDeath
   * @param id an ID
   */
  public CauseOfDeath(@NotNull TypeOfCauseOfDeath type, @NotNull Id id) {
    this.type = type;
    this.id = id;
  }

  /**
   * Convenience method that returns a CauseOfDeath that represents an unarmed kill.
   */
  public static CauseOfDeath getUnarmedCauseOfDeath() {
    return UNARMED;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    CauseOfDeath that = (CauseOfDeath) object;

    return id.equals(that.id) && type == that.type;
  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + id.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("%s : %s", type, id);
  }

}
