package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.entity.creatures.Observer;
import org.mafagafogigante.dungeon.game.Location;
import org.mafagafogigante.dungeon.game.World;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class WeatherConditionVisibilityCriterionTest {

  private Observer observer = Mockito.mock(Observer.class);
  private Location location = Mockito.mock(Location.class);
  private World world = Mockito.mock(World.class);

  private Weather weather = Mockito.mock(Weather.class);

  /**
   * Initialize the Mockito stubs.
   */
  @Before
  public void initialize() {
    Mockito.when(observer.getObserverLocation()).thenReturn(location);
    Mockito.when(location.getWorld()).thenReturn(world);
    Mockito.when(world.getWeather()).thenReturn(weather);
  }

  @Test
  public void testWeatherConditionVisibilityCriterionIsUnmetBelowMinimum() {
    for (int i = 1; i < WeatherCondition.values().length; i++) {
      WeatherCondition minimum = WeatherCondition.values()[i];
      WeatherCondition maximum = WeatherCondition.values()[WeatherCondition.values().length - 1];
      WeatherConditionVisibilityCriterion criterion = new WeatherConditionVisibilityCriterion(minimum, maximum);
      for (int j = 0; j < i; j++) {
        WeatherCondition belowMinimum = WeatherCondition.values()[j];
        Mockito.when(weather.getCurrentCondition(Mockito.any(Date.class))).thenReturn(belowMinimum);
        Assert.assertFalse(criterion.isMetBy(observer));
      }
    }
  }

  @Test
  public void testWeatherConditionVisibilityCriterionIsMetAtMinimum() {
    for (int i = 0; i < WeatherCondition.values().length; i++) {
      for (int j = i; j < WeatherCondition.values().length; j++) {
        WeatherCondition minimum = WeatherCondition.values()[i];
        WeatherCondition maximum = WeatherCondition.values()[j];
        WeatherConditionVisibilityCriterion criterion = new WeatherConditionVisibilityCriterion(minimum, maximum);
        Mockito.when(weather.getCurrentCondition(Mockito.any(Date.class))).thenReturn(minimum);
        Assert.assertTrue(criterion.isMetBy(observer));
      }
    }
  }

  @Test
  public void testWeatherConditionVisibilityCriterionIsMetInBetweenMinimumAndMaximum() {
    WeatherConditionVisibilityCriterion criterion =
        new WeatherConditionVisibilityCriterion(WeatherCondition.CLEAR, WeatherCondition.STORM);
    Mockito.when(weather.getCurrentCondition(Mockito.any(Date.class))).thenReturn(WeatherCondition.CLOUDY);
    Assert.assertTrue(criterion.isMetBy(observer));
  }

  @Test
  public void testWeatherConditionVisibilityCriterionIsMetAtMaximum() {
    for (int i = 0; i < WeatherCondition.values().length; i++) {
      for (int j = i; j < WeatherCondition.values().length; j++) {
        WeatherCondition minimum = WeatherCondition.values()[i];
        WeatherCondition maximum = WeatherCondition.values()[j];
        WeatherConditionVisibilityCriterion criterion = new WeatherConditionVisibilityCriterion(minimum, maximum);
        Mockito.when(weather.getCurrentCondition(Mockito.any(Date.class))).thenReturn(maximum);
        Assert.assertTrue(criterion.isMetBy(observer));
      }
    }
  }

  @Test
  public void testWeatherConditionVisibilityCriterionIsUnmetAboveMaximum() {
    for (int i = 0; i < WeatherCondition.values().length - 1; i++) {
      WeatherCondition minimum = WeatherCondition.values()[0];
      WeatherCondition maximum = WeatherCondition.values()[i];
      WeatherConditionVisibilityCriterion criterion = new WeatherConditionVisibilityCriterion(minimum, maximum);
      for (int j = i + 1; j < WeatherCondition.values().length; j++) {
        WeatherCondition aboveMaximum = WeatherCondition.values()[j];
        Mockito.when(weather.getCurrentCondition(Mockito.any(Date.class))).thenReturn(aboveMaximum);
        Assert.assertFalse(criterion.isMetBy(observer));
      }
    }
  }

  @Test
  public void testWeatherConditionVisibilityCriterionConstructorAllowsForMinimumEqualToMaximum() {
    for (WeatherCondition condition : WeatherCondition.values()) {
      new WeatherConditionVisibilityCriterion(condition, condition);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWeatherConditionVisibilityCriterionConstructorDoesNotAllowForMinimumHeavierThanMaximum() {
    new WeatherConditionVisibilityCriterion(WeatherCondition.STORM, WeatherCondition.CLEAR);
  }

}
