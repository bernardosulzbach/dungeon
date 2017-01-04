package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;

/**
 * A source of Luminosity.
 */
public class LightSource implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final Luminosity luminosity;
  private boolean enabled = true;

  public LightSource(Luminosity luminosity) {
    this.luminosity = luminosity;
  }

  /**
   * Enables this source until disable is called.
   */
  public void enable() {
    this.enabled = true;
  }

  /**
   * Disables this source until enable is called.
   */
  public void disable() {
    this.enabled = false;
  }

  /**
   * Returns the luminosity of this light source.
   */
  public Luminosity getLuminosity() {
    if (enabled) {
      return luminosity;
    } else {
      return Luminosity.ZERO;
    }
  }

  @Override
  public String toString() {
    return (enabled ? "Enabled" : "Disabled") + " LightSource of luminosity of " + luminosity.toPercentage();
  }

}
