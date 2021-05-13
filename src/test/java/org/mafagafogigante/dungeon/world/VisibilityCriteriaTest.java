package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.entity.creatures.Observer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class VisibilityCriteriaTest {

  VisibilityCriterion metCriterion = Mockito.mock(VisibilityCriterion.class);
  VisibilityCriterion unmetCriterion = Mockito.mock(VisibilityCriterion.class);

  @BeforeEach
  public void initialize() {
    Mockito.when(metCriterion.isMetBy(Mockito.any(Observer.class))).thenReturn(true);
    Mockito.when(unmetCriterion.isMetBy(Mockito.any(Observer.class))).thenReturn(false);
  }

  @Test
  public void testIsMetByShouldReturnTrueWhenThereAreNoVisibilityCriteria() throws Exception {
    VisibilityCriteria criteria = new VisibilityCriteria();
    Assertions.assertTrue(criteria.isMetBy(Mockito.mock(Observer.class)));
  }

  @Test
  public void testIsMetByShouldReturnTrueWhenThereIsASingleMetCriterion() throws Exception {
    VisibilityCriteria criteria = new VisibilityCriteria(metCriterion);
    Assertions.assertTrue(criteria.isMetBy(Mockito.mock(Observer.class)));
  }

  @Test
  public void testIsMetByShouldReturnFalseWhenThereIsASingleUnmetCriterion() throws Exception {
    VisibilityCriteria criteria = new VisibilityCriteria(unmetCriterion);
    Assertions.assertFalse(criteria.isMetBy(Mockito.mock(Observer.class)));
  }

  @Test
  public void testIsMetByShouldReturnTrueWhenAllCriteriaAreMet() throws Exception {
    VisibilityCriteria criteria = new VisibilityCriteria(metCriterion, metCriterion);
    Assertions.assertTrue(criteria.isMetBy(Mockito.mock(Observer.class)));
  }

  @Test
  public void testIsMetByShouldReturnFalseWhenThereIsAtLeastOneUnmetCriterion() throws Exception {
    VisibilityCriteria criteria = new VisibilityCriteria(metCriterion, unmetCriterion);
    Assertions.assertFalse(criteria.isMetBy(Mockito.mock(Observer.class)));
  }

}
