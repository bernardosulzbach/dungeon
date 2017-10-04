package org.mafagafogigante.dungeon.world;

import org.junit.Assert;
import org.junit.Test;

public class WeatherConditionTest {

  @Test
  public void testGetLighterReturnsTheNextLighterWeather() {
    WeatherCondition condition = WeatherCondition.STORM;
    Assert.assertEquals(WeatherCondition.RAIN, condition.getLighter());
    Assert.assertEquals(WeatherCondition.OVERCAST, condition.getLighter().getLighter());
    Assert.assertEquals(WeatherCondition.CLOUDY, condition.getLighter().getLighter().getLighter());
    Assert.assertEquals(WeatherCondition.CLEAR, condition.getLighter().getLighter().getLighter().getLighter());
  }

  @Test
  public void testGetLighterReturnsTheLighestWeatherWhenCalledOnTheLightestWeather() {
    Assert.assertEquals(WeatherCondition.CLEAR, WeatherCondition.CLEAR.getLighter());
  }

  @Test
  public void testGetHeavierReturnsTheNextHeavierWeather() {
    WeatherCondition condition = WeatherCondition.CLEAR;
    Assert.assertEquals(WeatherCondition.CLOUDY, condition.getHeavier());
    Assert.assertEquals(WeatherCondition.OVERCAST, condition.getHeavier().getHeavier());
    Assert.assertEquals(WeatherCondition.RAIN, condition.getHeavier().getHeavier().getHeavier());
    Assert.assertEquals(WeatherCondition.STORM, condition.getHeavier().getHeavier().getHeavier().getHeavier());
  }

  @Test
  public void testGetHeavierReturnsTheHeaviestWeatherWhenCalledOnTheHeaviestWeather() {
    Assert.assertEquals(WeatherCondition.STORM, WeatherCondition.STORM.getHeavier());
  }


  @Test
  public void testIsHeavierThanWorksAsIntended() {
    WeatherCondition condition = WeatherCondition.CLEAR;
    Assert.assertFalse(condition.isHeavierThan(condition));
    Assert.assertTrue(condition.getHeavier().isHeavierThan(condition));
    Assert.assertTrue(condition.getHeavier().getHeavier().isHeavierThan(condition));
    Assert.assertTrue(condition.getHeavier().getHeavier().getHeavier().isHeavierThan(condition));
    Assert.assertTrue(condition.getHeavier().getHeavier().getHeavier().getHeavier().isHeavierThan(condition));
  }

  @Test
  public void testIsLighterThanWorksAsIntended() {
    WeatherCondition condition = WeatherCondition.STORM;
    Assert.assertFalse(condition.isLighterThan(condition));
    Assert.assertTrue(condition.getLighter().isLighterThan(condition));
    Assert.assertTrue(condition.getLighter().isLighterThan(condition));
    Assert.assertTrue(condition.getLighter().getLighter().isLighterThan(condition));
    Assert.assertTrue(condition.getLighter().getLighter().getLighter().isLighterThan(condition));
    Assert.assertTrue(condition.getLighter().getLighter().getLighter().getLighter().isLighterThan(condition));
  }

}
