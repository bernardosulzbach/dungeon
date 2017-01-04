package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.entity.Luminosity;
import org.mafagafogigante.dungeon.entity.creatures.Observer;
import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

/**
 * A visibility criterion based on luminosity.
 */
public class LuminosityVisibilityCriterion implements Serializable, VisibilityCriterion {

  private static final long serialVersionUID = Version.MAJOR;
  private final Luminosity minimumLuminosity;

  public LuminosityVisibilityCriterion(Luminosity minimumLuminosity) {
    this.minimumLuminosity = minimumLuminosity;
  }

  @Override
  public boolean isMetBy(Observer observer) {
    double observerLuminosity = observer.getObserverLocation().getLuminosity().toPercentage().toDouble();
    return Double.compare(observerLuminosity, minimumLuminosity.toPercentage().toDouble()) >= 0;
  }

}
