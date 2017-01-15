package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

abstract class Condition implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;

  abstract Date getExpirationDate();

  final boolean hasExpired(Date date) {
    return getExpirationDate().compareTo(date) < 0;
  }

  abstract int modifyAttack(int currentAttack);

}
