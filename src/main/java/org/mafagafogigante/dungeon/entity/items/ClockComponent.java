package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.Random;
import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

/**
 * The clock component.
 */
public class ClockComponent implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final Item master;

  /**
   * Used to store the date the clock had when it was broken.
   */
  private Date lastTime;

  ClockComponent(Item master) {
    this.master = master;
  }

  public void setLastTime(Date lastTime) {
    // Create a new Date object so that this field is not affected by changes in the rest of the program.
    this.lastTime = lastTime;
  }

  /**
   * Returns a string that represents a clock reading.
   */
  public String getTimeString() {
    if (master.isBroken()) {
      if (lastTime == null) {
        if (Random.nextBoolean()) {
          return "The clock is pure junk.";
        } else {
          return "The clock is completely smashed.";
        }
      } else {
        return "The clock is broken. Still, it displays " + lastTime.toTimeString() + ".";
      }
    } else {
      String timeString = Game.getGameState().getWorld().getWorldDate().toTimeString();
      return "The clock displays " + timeString + ".";
    }
  }

}
