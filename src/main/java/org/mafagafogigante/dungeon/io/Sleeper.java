package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.logging.DungeonLogger;

/**
 * Sleeper class that handles Thread sleeping.
 */
public final class Sleeper {

  private static final int MILLISECONDS_IN_MINUTE = 60000;

  private Sleeper() {
    throw new AssertionError();
  }

  /**
   * Sleeps for a specified amount of milliseconds.
   *
   * @param milliseconds how many milliseconds should this thread sleep for, between 1 and 60000
   */
  public static void sleep(long milliseconds) {
    if (milliseconds <= 0) {
      DungeonLogger.warning("Tried to sleep a nonpositive amount of milliseconds.");
    } else if (milliseconds > MILLISECONDS_IN_MINUTE) {
      DungeonLogger.warning("Tried to sleep for more than a minute.");
    } else { // If milliseconds is in range, sleep.
      try {
        Thread.sleep(milliseconds);
      } catch (InterruptedException logged) {
        DungeonLogger.warning("Sleeper was interrupted.");
      }
    }
  }

}
