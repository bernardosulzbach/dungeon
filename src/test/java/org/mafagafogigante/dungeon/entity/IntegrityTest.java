package org.mafagafogigante.dungeon.entity;

import org.junit.Assert;
import org.junit.Test;

public class IntegrityTest {

  @Test
  public void testIsMaximum() throws Exception {
    Assert.assertTrue(new Integrity(2, 2).isMaximum());
    Assert.assertFalse(new Integrity(2, 1).isMaximum());
    Assert.assertFalse(new Integrity(2, 0).isMaximum());
  }

  @Test
  public void testIsZero() throws Exception {
    Assert.assertTrue(new Integrity(2, 0).isZero());
    Assert.assertFalse(new Integrity(2, 1).isZero());
    Assert.assertFalse(new Integrity(2, 2).isZero());
  }

  @Test
  public void incrementByShouldWorkWithPositiveValues() throws Exception {
    Integrity integrity = new Integrity(1, 0);
    Assert.assertEquals(0, integrity.getCurrent());
    integrity.incrementBy(1);
    Assert.assertEquals(1, integrity.getCurrent());
    integrity.incrementBy(1);
    Assert.assertEquals(1, integrity.getCurrent());
  }

  @Test
  public void incrementByShouldWorkWithNegativeValues() throws Exception {
    Integrity integrity = new Integrity(1, 1);
    Assert.assertEquals(1, integrity.getCurrent());
    integrity.incrementBy(-1);
    Assert.assertEquals(0, integrity.getCurrent());
    integrity.incrementBy(-1);
    Assert.assertEquals(0, integrity.getCurrent());
  }

  @Test
  public void decrementByShouldWorkWithPositiveValues() throws Exception {
    Integrity integrity = new Integrity(1, 1);
    Assert.assertEquals(1, integrity.getCurrent());
    integrity.decrementBy(1);
    Assert.assertEquals(0, integrity.getCurrent());
    integrity.decrementBy(1);
    Assert.assertEquals(0, integrity.getCurrent());
  }

  @Test
  public void decrementByShouldWorkWithNegativeValues() throws Exception {
    Integrity integrity = new Integrity(1, 0);
    Assert.assertEquals(0, integrity.getCurrent());
    integrity.decrementBy(-1);
    Assert.assertEquals(1, integrity.getCurrent());
    integrity.decrementBy(-1);
    Assert.assertEquals(1, integrity.getCurrent());
  }

}
