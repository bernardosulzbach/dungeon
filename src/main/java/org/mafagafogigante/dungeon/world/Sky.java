package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.entity.creatures.Observer;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The sky of a world.
 */
public class Sky implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final Collection<AstronomicalBody> astronomicalBodies = new ArrayList<>();

  /**
   * Constructs an empty sky.
   */
  Sky() {
  }

  /**
   * Adds an astronomical body to the sky.
   */
  void addAstronomicalBody(AstronomicalBody astronomicalBody) {
    astronomicalBodies.add(astronomicalBody);
  }

  /**
   * Returns a description of the sky with all its visible features.
   */
  public String describeYourself(Observer observer) {
    List<String> descriptions = new ArrayList<>();
    for (AstronomicalBody astronomicalBody : astronomicalBodies) {
      if (astronomicalBody.isVisible(observer)) {
        descriptions.add(astronomicalBody.describeYourself());
      }
    }
    return Utils.enumerate(descriptions);
  }

  @Override
  public String toString() {
    return "Sky{astronomicalBodies=" + astronomicalBodies + '}';
  }

}
