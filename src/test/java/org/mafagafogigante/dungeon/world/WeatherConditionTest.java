package org.mafagafogigante.dungeon.world;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WeatherConditionTest {

  @Test
  public void testGetLighterReturnsTheNextLighterWeather() {
    WeatherCondition condition = WeatherCondition.STORM;
    Assertions.assertEquals(WeatherCondition.RAIN, condition.getLighter());
    Assertions.assertEquals(WeatherCondition.OVERCAST, condition.getLighter().getLighter());
    Assertions.assertEquals(WeatherCondition.CLOUDY, condition.getLighter().getLighter().getLighter());
    Assertions.assertEquals(WeatherCondition.CLEAR, condition.getLighter().getLighter().getLighter().getLighter());
  }

  @Test
  public void testGetLighterReturnsTheLighestWeatherWhenCalledOnTheLightestWeather() {
    Assertions.assertEquals(WeatherCondition.CLEAR, WeatherCondition.CLEAR.getLighter());
  }

  @Test
  public void testGetHeavierReturnsTheNextHeavierWeather() {
    WeatherCondition condition = WeatherCondition.CLEAR;
    Assertions.assertEquals(WeatherCondition.CLOUDY, condition.getHeavier());
    Assertions.assertEquals(WeatherCondition.OVERCAST, condition.getHeavier().getHeavier());
    Assertions.assertEquals(WeatherCondition.RAIN, condition.getHeavier().getHeavier().getHeavier());
    Assertions.assertEquals(WeatherCondition.STORM, condition.getHeavier().getHeavier().getHeavier().getHeavier());
  }

  @Test
  public void testGetHeavierReturnsTheHeaviestWeatherWhenCalledOnTheHeaviestWeather() {
    Assertions.assertEquals(WeatherCondition.STORM, WeatherCondition.STORM.getHeavier());
  }


  @Test
  public void testIsHeavierThanWorksAsIntended() {
    WeatherCondition condition = WeatherCondition.CLEAR;
    Assertions.assertFalse(condition.isHeavierThan(condition));
    Assertions.assertTrue(condition.getHeavier().isHeavierThan(condition));
    Assertions.assertTrue(condition.getHeavier().getHeavier().isHeavierThan(condition));
    Assertions.assertTrue(condition.getHeavier().getHeavier().getHeavier().isHeavierThan(condition));
    Assertions.assertTrue(condition.getHeavier().getHeavier().getHeavier().getHeavier().isHeavierThan(condition));
  }

  @Test
  public void testIsLighterThanWorksAsIntended() {
    WeatherCondition condition = WeatherCondition.STORM;
    Assertions.assertFalse(condition.isLighterThan(condition));
    Assertions.assertTrue(condition.getLighter().isLighterThan(condition));
    Assertions.assertTrue(condition.getLighter().isLighterThan(condition));
    Assertions.assertTrue(condition.getLighter().getLighter().isLighterThan(condition));
    Assertions.assertTrue(condition.getLighter().getLighter().getLighter().isLighterThan(condition));
    Assertions.assertTrue(condition.getLighter().getLighter().getLighter().getLighter().isLighterThan(condition));
  }

}
