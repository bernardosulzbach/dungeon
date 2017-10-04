package org.mafagafogigante.dungeon.world;

/**
 * WeatherCondition class that indicates a weather condition.
 */
public enum WeatherCondition {

  CLEAR("the sky is clear"),
  CLOUDY("the sky is cloudy"),
  OVERCAST("the sky is overcast"),
  RAIN("it is raining"),
  STORM("it is storming");

  private final String descriptivePhrase;

  WeatherCondition(String descriptivePhrase) {
    this.descriptivePhrase = descriptivePhrase;
  }

  WeatherCondition getLighter() {
    return WeatherCondition.values()[Math.max(0, ordinal() - 1)];
  }

  boolean isLighterThan(WeatherCondition condition) {
    return ordinal() < condition.ordinal();
  }

  WeatherCondition getHeavier() {
    return WeatherCondition.values()[Math.min(WeatherCondition.values().length - 1, ordinal() + 1)];
  }

  boolean isHeavierThan(WeatherCondition condition) {
    return ordinal() > condition.ordinal();
  }

  public String toDescriptiveString() {
    return descriptivePhrase;
  }

}
