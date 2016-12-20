package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.entity.creatures.Observer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SimpleAstronomicalBodyTest {

  VisibilityCriterion metCriterion = Mockito.mock(VisibilityCriterion.class);
  VisibilityCriterion unmetCriterion = Mockito.mock(VisibilityCriterion.class);

  @Before
  public void initialize() {
    Mockito.when(metCriterion.isMetBy(Mockito.any(Observer.class))).thenReturn(true);
    Mockito.when(unmetCriterion.isMetBy(Mockito.any(Observer.class))).thenReturn(false);
  }

  @Test
  public void testIsVisibleShouldReturnTrueWhenThereAreNoVisibilityCriteria() throws Exception {
    SimpleAstronomicalBody astronomicalBody = new SimpleAstronomicalBody("");
    Assert.assertTrue(astronomicalBody.isVisible(Mockito.mock(Observer.class)));
  }

  @Test
  public void testIsVisibleShouldReturnTrueWhenThereIsASingleMetCriterion() throws Exception {
    SimpleAstronomicalBody astronomicalBody = new SimpleAstronomicalBody("", metCriterion);
    Assert.assertTrue(astronomicalBody.isVisible(Mockito.mock(Observer.class)));
  }

  @Test
  public void testIsVisibleShouldReturnFalseWhenThereIsASingleUnmetCriterion() throws Exception {
    SimpleAstronomicalBody astronomicalBody = new SimpleAstronomicalBody("", unmetCriterion);
    Assert.assertFalse(astronomicalBody.isVisible(Mockito.mock(Observer.class)));
  }

  @Test
  public void testIsVisibleShouldReturnTrueWhenAllCriteriaAreMet() throws Exception {
    SimpleAstronomicalBody astronomicalBody = new SimpleAstronomicalBody("", metCriterion, metCriterion);
    Assert.assertTrue(astronomicalBody.isVisible(Mockito.mock(Observer.class)));
  }

  @Test
  public void testIsVisibleShouldReturnFalseWhenThereIsAtLeastOneUnmetCriterion() throws Exception {
    SimpleAstronomicalBody astronomicalBody = new SimpleAstronomicalBody("", metCriterion, unmetCriterion);
    Assert.assertFalse(astronomicalBody.isVisible(Mockito.mock(Observer.class)));
  }

}
