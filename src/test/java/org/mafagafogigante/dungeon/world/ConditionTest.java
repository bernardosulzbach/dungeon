package org.mafagafogigante.dungeon.world;

import org.junit.Assert;
import org.junit.Test;

public class ConditionTest {

  @Test
  public void testGetLighterReturnsTheNextLighterWeather() {
    Condition condition = Condition.STORM;
    Assert.assertEquals(Condition.RAIN, condition.getLighter());
    Assert.assertEquals(Condition.OVERCAST, condition.getLighter().getLighter());
    Assert.assertEquals(Condition.CLOUDY, condition.getLighter().getLighter().getLighter());
    Assert.assertEquals(Condition.CLEAR, condition.getLighter().getLighter().getLighter().getLighter());
  }

  @Test
  public void testGetLighterReturnsTheLighestWeatherWhenCalledOnTheLightestWeather() {
    Assert.assertEquals(Condition.CLEAR, Condition.CLEAR.getLighter());
  }

  @Test
  public void testGetHeavierReturnsTheNextHeavierWeather() {
    Condition condition = Condition.CLEAR;
    Assert.assertEquals(Condition.CLOUDY, condition.getHeavier());
    Assert.assertEquals(Condition.OVERCAST, condition.getHeavier().getHeavier());
    Assert.assertEquals(Condition.RAIN, condition.getHeavier().getHeavier().getHeavier());
    Assert.assertEquals(Condition.STORM, condition.getHeavier().getHeavier().getHeavier().getHeavier());
  }

  @Test
  public void testGetHeavierReturnsTheHeaviestWeatherWhenCalledOnTheHeaviestWeather() {
    Assert.assertEquals(Condition.STORM, Condition.STORM.getHeavier());
  }


  @Test
  public void testIsHeavierThanWorksAsIntended() {
    Condition condition = Condition.CLEAR;
    Assert.assertFalse(condition.isHeavierThan(condition));
    Assert.assertTrue(condition.getHeavier().isHeavierThan(condition));
    Assert.assertTrue(condition.getHeavier().getHeavier().isHeavierThan(condition));
    Assert.assertTrue(condition.getHeavier().getHeavier().getHeavier().isHeavierThan(condition));
    Assert.assertTrue(condition.getHeavier().getHeavier().getHeavier().getHeavier().isHeavierThan(condition));
  }

  @Test
  public void testIsLighterThanWorksAsIntended() {
    Condition condition = Condition.STORM;
    Assert.assertFalse(condition.isLighterThan(condition));
    Assert.assertTrue(condition.getLighter().isLighterThan(condition));
    Assert.assertTrue(condition.getLighter().isLighterThan(condition));
    Assert.assertTrue(condition.getLighter().getLighter().isLighterThan(condition));
    Assert.assertTrue(condition.getLighter().getLighter().getLighter().isLighterThan(condition));
    Assert.assertTrue(condition.getLighter().getLighter().getLighter().getLighter().isLighterThan(condition));
  }

}