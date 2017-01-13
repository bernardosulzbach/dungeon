package org.mafagafogigante.dungeon.world;

/**
 * The class responsible by creating skies.
 */
public class SkyFactory {

  /**
   * Makes and returns the sky of Darrowmere.
   */
  public static Sky makeDarrowmereSky() {
    Sky sky = new Sky();
    sky.addAstronomicalBody(makeDarrowmereSun());
    sky.addAstronomicalBody(makeDarrowmereMoonMino());
    sky.addAstronomicalBody(makeDarrowmereMoonSario());
    return sky;
  }

  private static SimpleAstronomicalBody makeDarrowmereSun() {
    String sunDescription = "the Sun, a large, golden, spherical body";
    TimeVisibilityCriterion sunTime = new TimeVisibilityCriterion(6, 18);
    WeatherCondition clear = WeatherCondition.CLEAR;
    WeatherCondition cloudy = WeatherCondition.CLOUDY;
    WeatherConditionVisibilityCriterion criterion = new WeatherConditionVisibilityCriterion(clear, cloudy);
    return new SimpleAstronomicalBody(sunDescription, sunTime, criterion);
  }

  private static SimpleAstronomicalBody makeDarrowmereMoonMino() {
    String description = "Mino, a small, dull white spherical body";
    TimeVisibilityCriterion time = new TimeVisibilityCriterion(16, 8);
    WeatherCondition clear = WeatherCondition.CLEAR;
    WeatherCondition cloudy = WeatherCondition.CLOUDY;
    WeatherConditionVisibilityCriterion criterion = new WeatherConditionVisibilityCriterion(clear, cloudy);
    return new SimpleAstronomicalBody(description, time, criterion);
  }

  private static SimpleAstronomicalBody makeDarrowmereMoonSario() {
    String description = "Sario, a very small, green-tinted, perfectly spherical body";
    TimeVisibilityCriterion time = new TimeVisibilityCriterion(20, 2);
    WeatherCondition storm = WeatherCondition.STORM;
    WeatherConditionVisibilityCriterion criterion = new WeatherConditionVisibilityCriterion(storm, storm);
    return new SimpleAstronomicalBody(description, time, criterion);
  }

}
