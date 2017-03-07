package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.Percentage;

import java.io.Serializable;

abstract class Condition implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;

  abstract Date getExpirationDate();

  abstract Effect getEffect();

  abstract String getDescription();

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    return getClass().getCanonicalName().equals(object.getClass().getCanonicalName());
  }

  @Override
  public int hashCode() {
    return getClass().getCanonicalName().hashCode();
  }

  final boolean hasExpired(Date date) {
    return getExpirationDate().compareTo(date) < 0;
  }

  int modifyAttack(int currentAttack) {
    return currentAttack;
  }

  Percentage modifyHitRate(Percentage hitRate) {
    return hitRate;
  }

  Percentage modifyFishingProficiency(Percentage proficiency) {
    return proficiency;
  }

}
