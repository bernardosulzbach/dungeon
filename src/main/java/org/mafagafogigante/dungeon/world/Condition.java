package org.mafagafogigante.dungeon.world;

/**
 * Condition class that indicates a weather condition.
 */
public enum Condition {

  CLEAR("the sky is clear"),
  CLOUDY("the sky is cloudy"),
  OVERCAST("the sky is overcast"),
  RAIN("it is raining"),
  STORM("it is storming");

  private final String descriptivePhrase;

  Condition(String descriptivePhrase) {
    this.descriptivePhrase = descriptivePhrase;
  }

  Condition getLighter() {
    return Condition.values()[Math.max(0, ordinal() - 1)];
  }

  boolean isLighterThan(Condition condition) {
    return ordinal() < condition.ordinal();
  }

  Condition getHeavier() {
    return Condition.values()[Math.min(Condition.values().length - 1, ordinal() + 1)];
  }

  boolean isHeavierThan(Condition condition) {
    return ordinal() > condition.ordinal();
  }

  public String toDescriptiveString() {
    return descriptivePhrase;
  }

}
