package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.game.World;

/**
 * The class responsible by creating skies.
 */
public class SkyFactory {

  /**
   * Makes and returns the sky of Darrowmere.
   */
  public static Sky makeDarrowmereSky(World world) {
    Sky sky = new Sky();
    String sunDescription = "the Sun, a large, golden, spherical body";
    TimeVisibilityCriterion sunTime = new TimeVisibilityCriterion(6, 18);
    WeatherConditionVisibilityCriterion
        sunWeather = new WeatherConditionVisibilityCriterion(WeatherCondition.CLEAR, WeatherCondition.CLOUDY);
    SimpleAstronomicalBody sun = new SimpleAstronomicalBody(sunDescription, sunTime, sunWeather);
    sky.addAstronomicalBody(sun);
    return sky;
  }

}
